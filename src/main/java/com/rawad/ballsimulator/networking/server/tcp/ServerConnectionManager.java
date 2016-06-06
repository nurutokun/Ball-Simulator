package com.rawad.ballsimulator.networking.server.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.RandomPositionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.networking.APacket;
import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;
import com.rawad.ballsimulator.networking.client.tcp.CPacket01Login;
import com.rawad.ballsimulator.networking.client.tcp.CPacket02Logout;
import com.rawad.ballsimulator.networking.client.tcp.CPacket04Message;
import com.rawad.ballsimulator.networking.server.ServerNetworkManager;
import com.rawad.ballsimulator.server.Server;
import com.rawad.ballsimulator.server.entity.NetworkComponent;
import com.rawad.ballsimulator.server.entity.UserComponent;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

/**
 * TCP server for accepting and terminating server's connections with clients. Also deals with TCP packets in general.
 * 
 * @author Rawad
 *
 */
public class ServerConnectionManager {
	
	private ServerNetworkManager networkManager;
	
	private ArrayList<Socket> clients;
	private ArrayList<ClientInputManager> clientInputManagers;
	
	private ServerSocket serverSocket;
	
	private Thread connectionAcceptor;
	
	public ServerConnectionManager(ServerNetworkManager networkManager) {
		this.networkManager = networkManager;
		
		clients = new ArrayList<Socket>();
		clientInputManagers = new ArrayList<ClientInputManager>();
		
		connectionAcceptor = new Thread(new ConnectionAcceptor(), "Connection Acceptor");
		
	}
	
	public void start() {
		
		try {
			// Rather initialize this here than in the constructor...
			serverSocket = new ServerSocket(Server.PORT);
		} catch(Exception ex) {
			Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; server socket for TCP connection couldn't "
					+ "be initialized.");
			
			networkManager.getServer().getGame().requestStop();
			
			return;
		}
		
		connectionAcceptor.start();
		
	}
	
	public void stop() {
		
		ArrayList<Entity> entities = networkManager.getServer().getGame().getWorld().getEntitiesAsList();
		
		synchronized(clientInputManagers) {
			for(Entity e: entities) {
				
				SPacket02Logout logoutPlayer = new SPacket02Logout(e.getComponent(NetworkComponent.class).getId());
				
				sendPacketToAllClients(null, logoutPlayer);
				
			}
		}
		
		synchronized(clients) {
			for(Socket client: clients) {
				
				Util.silentClose(client);
				
			}
		}
		
		Util.silentClose(serverSocket);
		
	}
	
	private synchronized void handleClientInput(Socket client, String dataAsString) {
		
		TCPPacketType type = TCPPacket.getPacketTypeFromData(dataAsString);
		
		World world =  networkManager.getServer().getGame().getWorld();
		
		switch(type) {
		
		case LOGIN:
			
			CPacket01Login clientLoginPacket = new CPacket01Login(dataAsString);
			
			if(networkManager.getServer().getEntityById(clientLoginPacket.getEntityId()) != null) {// Entity
				// already exists
				Logger.log(Logger.DEBUG, "Player with id \"" + clientLoginPacket.getEntityId() + "\" is already logged in"
						+ ", disconnecting new player.");
				
				sendPacketToClient(client, new SPacket01Login(new NetworkComponent(), new UserComponent(),  
						new TransformComponent(), "", false));
				
				break;
				
			}
			
			Entity player = Entity.createEntity(EEntity.PLAYER);
			
			NetworkComponent networkComp = new NetworkComponent();
			UserComponent userComp = new UserComponent();
			
			userComp.setIp(clientLoginPacket.getIp());
			userComp.setUsername(clientLoginPacket.getUsername());
			
			player.addComponent(networkComp);
			player.addComponent(userComp);
			
			world.addEntity(player);// Assigns id (WorldMP).
			
			RandomPositionComponent randomPosComp = player.getComponent(RandomPositionComponent.class);
			randomPosComp.setGenerateNewPosition(true);
			
			TransformComponent transformComp = player.getComponent(TransformComponent.class);
			
			String loginMessage = userComp.getUsername() + " has joined the game...";
			Logger.log(Logger.DEBUG, loginMessage);
			
			sendPacketToAllClients(null, new SPacket04Message(Server.SIMPLE_NAME, loginMessage));
			
			SPacket01Login serverLoginResponsePacket = new SPacket01Login(networkComp, userComp, transformComp, 
					Server.TERRAIN_NAME, true);
			
			// Inform all current players of this new player's login.
			sendPacketToAllClients(null, serverLoginResponsePacket);
			
			ArrayList<Entity> players = world.getEntitiesAsList();
			
			// Informs player that just logged in of previously logged-in players.
			for(Entity playerInWorld: players) {
				
				int playerInWorldId = playerInWorld.getComponent(NetworkComponent.class).getId();
				
				if(networkComp.getId() == playerInWorldId) continue;
				
				serverLoginResponsePacket = new SPacket01Login(playerInWorld.getComponent(NetworkComponent.class),
						playerInWorld.getComponent(UserComponent.class), playerInWorld
						.getComponent(TransformComponent.class), Server.TERRAIN_NAME, true);
				
				sendPacketToClient(client, serverLoginResponsePacket);
				
			}
			
			clients.add(client);// Client is now officially added, mainly so datagram isn't sending data to
			// clients that haven't logged in yet and so that all other players are registered on the client so 
			// the client isn't trying to update non-logged in players
			
			break;
			
		case LOGOUT:
			
			CPacket02Logout logoutPacket = new CPacket02Logout(dataAsString);
			
			disconnectClient(client, world, logoutPacket.getEntityId());
			
			SPacket02Logout clientInformerPacket = new SPacket02Logout(logoutPacket.getEntityId());
			
			sendPacketToAllClients(null, clientInformerPacket);
			
			player = networkManager.getServer().getEntityById(logoutPacket.getEntityId());
			
			String username = player.getComponent(UserComponent.class).getUsername();
			
			String logoutMessage = username + " has left the game...";
			
			Logger.log(Logger.DEBUG, logoutMessage);
			
			sendPacketToAllClients(client, new SPacket04Message(username, logoutMessage));
			
			break;
			
		case MESSAGE:
			
			CPacket04Message messagePacket = new CPacket04Message(dataAsString);
			
			username = messagePacket.getSender();
			
			String message = messagePacket.getMessage();
			
			Logger.log(Logger.DEBUG, username + " sent a message: " + message);
			
			SPacket04Message replyPacket = new SPacket04Message(username, message);
			
			sendPacketToAllClients(client, replyPacket);// Don't need to send it back to the client that sent it.
			
			break;
			
		case INVALID:
		default:
			Logger.log(Logger.WARNING, "Invalid packet: \"" + dataAsString + "\".");
			break;
		}
		
	}
	
	private void disconnectClient(Socket client, World world, int entityId) {
		
			world.removeEntity(networkManager.getServer().getEntityById(entityId));
			
			clients.remove(client);
			
			Util.silentClose(client);
			
	}
	
	private synchronized void startNewClientInputManager(Socket client) {
		
		ClientInputManager clientInputManager = new ClientInputManager(client);
		
		Thread t = new Thread(clientInputManager, client.getInetAddress().getHostName() + " Client Manager");
		t.setDaemon(true);
		t.start();
		
	}
	
	public void sendPacketToAllClients(Socket clientToExclude, APacket packet) {
		
		for(Socket client: clients) {
			
			if(client.equals(clientToExclude)) {
				continue;
			}
			
			sendPacketToClient(client, packet);
			
		}
		
	}
	
	public void sendPacketToClient(Socket client, APacket packet) {
		sendMessageToClient(client, packet.getDataAsString());
	}
	
	private void sendMessageToClient(Socket client, String message) {
		
		try {
			
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
			
			writer.println(message);
			
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; couldn't send message: \"" + message
					+ "\" to client: " + client.getInetAddress().getHostName());
		}
		
	}
	
	public ArrayList<Socket> getClients() {
		return clients;
	}
	
	private class ConnectionAcceptor implements Runnable {
		
		@Override
		public void run() {
			
			while(networkManager.getServer().getGame().isRunning()) {
				
				try {
					Socket client = serverSocket.accept();
					
					startNewClientInputManager(client);
					
				} catch(Exception ex) {
					Logger.log(Logger.WARNING, ex.getMessage());
					
					networkManager.getServer().getGame().requestStop();
					
					break;
				}
				
			}
			
		}
		
	}
	
	private class ClientInputManager implements Runnable {
		
		private final Socket client;
		
		public ClientInputManager(Socket client) {
			this.client = client;
		}
		
		@Override
		public void run() {
			
			try {
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				
				while(!client.isClosed()) {
					
					String input = reader.readLine();
					
					if(input != null) {
						handleClientInput(client, input);
					}
					
				}
				
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; client " + client.getInetAddress()
						.getHostName() + " disconnected.");
			}
			
			/*/
			if(loggedIn) {// Mainly for when client closes game while still connecting (TCP is connected but not logged 
				// in yet)
				
				CPacket02Logout ensureLogout = new CPacket02Logout(clientPlayerId, client.getInetAddress()
						.getHostAddress());
				
				handleClientInput(client, ensureLogout.getDataAsString());
				
			}/**/
			
		}
		
	}
	
}
