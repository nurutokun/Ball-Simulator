package com.rawad.ballsimulator.networking.client.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.game.World;
import com.rawad.ballsimulator.networking.ConnectionState;
import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.ballsimulator.networking.server.tcp.SPacket01Login;
import com.rawad.ballsimulator.networking.server.tcp.SPacket02Logout;
import com.rawad.ballsimulator.networking.server.tcp.SPacket03Message;
import com.rawad.ballsimulator.networking.server.tcp.SPacket04Entity;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;

/**
 * Handles connecting to the server and telling the server when to disconnect/knowing when to get disconnected. Also just 
 * handles TCP packets in general.
 * 
 * @author Rawad
 *
 */
public class ClientConnectionManager {
	
	private ClientNetworkManager networkManager;
	
	private Socket socket;
	
	private Thread connectionHandler;
	
	private String serverAddress;
	
	public ClientConnectionManager(ClientNetworkManager networkManager) {
		this.networkManager = networkManager;
		
	}
	
	public void connectToServer(String address, int port) {
		
		if(!networkManager.isConnectedToServer()) {
			
			this.serverAddress = address;
			
			networkManager.setConnectionState(ConnectionState.CONNECTING);// Sometimes, previous thread doesn't stop fast
			// enough, causing the multiplayer game state to go back to the main menu, thinking that it disconnected or
			// something.
			
			try {// Initializing socket here fixes problem with player being logged in server-side only because of exiting
				// multiplayer too quickly
				
				socket = new Socket(address, port);
				
				if(networkManager.isDisconnectedFromServer()) {// If a disconnect was requested in the mean time
					throw new Exception("Disconnect requested.");
				}
				
				Logger.log(Logger.DEBUG, "Successfully connected to server.");
				
				networkManager.setConnectionState(ConnectionState.CONNECTED);
				networkManager.onConnect();
				
				connectionHandler = new Thread(new ConnectionHandler(socket), "Connection Handler");
				connectionHandler.setDaemon(true);
				connectionHandler.start();
				
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; couldn't connect to \"" + address 
						+ "\", on the port " + port);
				
				networkManager.requestDisconnect();
				
				return;
			}
			
		}
		
	}
	
	public synchronized void disconnect() {
		
		try {
			
			CPacket02Logout logoutPacket = new CPacket02Logout();
			
			sendPacketToServer(logoutPacket);
			
			networkManager.onDisconnect();
			
			socket.close();
			
		} catch (Exception ex) {
			Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; couldn't close client socket.");
		}
		
	}
	
	private void handleServerInput(Socket client, String dataAsString) {
		
		TCPPacketType type = TCPPacket.getPacketTypeFromData(dataAsString);
		
		World world = networkManager.getClient().getWorld();
		
		switch(type) {
		
		case LOGIN:
			
			SPacket01Login loginReplyPacket = new SPacket01Login(dataAsString);
			
			if(!loginReplyPacket.canLogin()) {
				networkManager.requestDisconnect();
				break;
			}
			
			Entity player = networkManager.getClient().getPlayer();
			
			NetworkComponent networkComp = player.getComponent(NetworkComponent.class);
			UserComponent userComp = player.getComponent(UserComponent.class);
			
			if(player.getComponent(UserComponent.class).getUsername().equals(loginReplyPacket.getUsername())) {
				
				networkManager.setLoggedIn(true);
				
			} else {// Player that is logging in isn't the client's player, so create a new player.
				
				player = Entity.createEntity(EEntity.PLAYER);
				
				networkComp = new NetworkComponent();
				userComp = new UserComponent();
				
				player.addComponent(networkComp);
				player.addComponent(userComp);
				
				world.addEntity(player);
				
			}
			
			int receivedEntityId = loginReplyPacket.getEntityId();
			
			networkComp.setId(receivedEntityId);
			userComp.setIp(loginReplyPacket.getIp());
			userComp.setUsername(loginReplyPacket.getUsername());// Could move this down (username 
			// confirmation).
			
			TransformComponent transformComp = player.getComponent(TransformComponent.class);
			
			transformComp.setX(loginReplyPacket.getX());
			transformComp.setY(loginReplyPacket.getY());
			
			transformComp.setScaleX(loginReplyPacket.getScaleX());
			transformComp.setScaleY(loginReplyPacket.getScaleY());
			
			transformComp.setTheta(loginReplyPacket.getTheta());
			
			networkManager.getClient().addPlayer(player);
			
			break;
			
		case LOGOUT:
			
			SPacket02Logout logoutPacket = new SPacket02Logout(dataAsString);
			
			if(logoutPacket.getEntityId() == networkManager.getClient().getPlayer().getComponent(NetworkComponent.class)
					.getId()) {
				networkManager.setLoggedIn(false);
				networkManager.requestDisconnect();
			} else {
				networkManager.getClient().removeEntity(logoutPacket.getEntityId());
			}
			
			break;
			
		case MESSAGE:
			
			SPacket03Message messagePacket = new SPacket03Message(dataAsString);
			
			networkManager.getClient().addUserMessage(messagePacket.getSender(), messagePacket.getMessage());
			
			break;
			
		case ENTITY:
			
			SPacket04Entity entityPacket = new SPacket04Entity(dataAsString);
			
			if(entityPacket.isLast()) {
				networkManager.onEntityLoadFinish();
			} else {
				
				Entity entity = Entity.createEntity(EEntity.getByName(entityPacket.getEntityName()));
				
				TransformComponent entityTransform = entity.getComponent(TransformComponent.class);
				entityTransform.setX(entityPacket.getX());
				entityTransform.setY(entityPacket.getY());
				entityTransform.setScaleX(entityPacket.getScaleX());
				entityTransform.setScaleY(entityPacket.getScaleY());
				entityTransform.setTheta(entityPacket.getTheta());
				
				List<Entity> entities = world.getEntities();
				
				synchronized(entities) {
					world.addEntity(entity);
				}
			}
			
			break;
			
		case INVALID:
		default:
			Logger.log(Logger.WARNING, "Invalid packet: \"" + dataAsString + "\".");
			break;
		
		}
		
	}
	
	public void sendPacketToServer(TCPPacket packet) {
		sendMessageToServer(packet.getDataAsString());
	}
	
	private void sendMessageToServer(String message) {
		
		try {
			
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			
			writer.println(message);
			
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; couldn't send packet: \"" + message + "\".");
		}
		
	}
	
	public class ConnectionHandler implements Runnable {
		
		private Socket client;
		
		public ConnectionHandler(Socket client) {
			this.client = client;
		}
		
		@Override
		public void run() {
			
			try {
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				
				while(!client.isClosed()) {
					
					String input = reader.readLine();
					
					if(input != null) {
						handleServerInput(client, input);
					}
					
				}
				
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; client lost connection to server or"
						+ " socket was closed by other means.");
			} finally {
				networkManager.requestDisconnect();
			}
			
		}
		
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String getServerAddress() {
		return serverAddress;
	}
	
}
