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
import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;
import com.rawad.ballsimulator.networking.client.tcp.CPacket01Login;
import com.rawad.ballsimulator.networking.client.tcp.CPacket03Message;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.ballsimulator.networking.server.ServerNetworkManager;
import com.rawad.ballsimulator.server.Server;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

import javafx.collections.ObservableList;

/**
 * TCP server for accepting and terminating server's connections with clients. Also deals with TCP packets in general.
 * 
 * @author Rawad
 *
 */
public class ServerConnectionManager {
	
	private ServerNetworkManager networkManager;
	
	private ArrayList<ClientInputManager> clientInputManagers;
	
	private ServerSocket serverSocket;
	
	private Thread connectionAcceptor;
	
	public ServerConnectionManager(ServerNetworkManager networkManager) {
		this.networkManager = networkManager;
		
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
		
		Util.silentClose(serverSocket);// Prevent any new connections from being made immediately.
		
		synchronized(clientInputManagers) {
			for(ClientInputManager cim: clientInputManagers) {
				
				SPacket02Logout logoutPlayer = new SPacket02Logout(cim.getClientId());
				
				Socket client = cim.getClient();
				
				sendPacketToAllClients(null, logoutPlayer);
				
				Util.silentClose(client);// Don't use helper methods here because we don't want to remove from array
				// while looping through it.
				
			}
			
			clientInputManagers.clear();
			
		}
		
	}
	
	private synchronized void handleClientInput(ClientInputManager cim, String dataAsString) {
		
		TCPPacketType type = TCPPacket.getPacketTypeFromData(dataAsString);
		
		Socket client = cim.getClient();
		
		World world =  networkManager.getServer().getGame().getWorld();
		
		String username;
		
		switch(type) {
		
		case LOGIN:
			
			CPacket01Login clientLoginPacket = new CPacket01Login(dataAsString);
			
			if(networkManager.getServer().getEntityById(clientLoginPacket.getEntityId()) != null) {// Entity
				// already exists
				Logger.log(Logger.DEBUG, "Player with id \"" + clientLoginPacket.getEntityId() + "\" is already logged in"
						+ ", disconnecting new player.");
				
				SPacket01Login denyLoginPacket = new SPacket01Login(new NetworkComponent(), new UserComponent(), 
						new TransformComponent(), false);
				
				sendPacketToClient(client, denyLoginPacket);
				
				disconnectClient(cim);
				
				break;
				
			}
			
			Entity player = Entity.createEntity(EEntity.PLAYER);
			
			RandomPositionComponent randomPosComp = new RandomPositionComponent();
			NetworkComponent networkComp = new NetworkComponent();
			UserComponent userComp = new UserComponent();
			
			randomPosComp.setGenerateNewPosition(true);
			
			userComp.setIp(clientLoginPacket.getIp());
			userComp.setUsername(clientLoginPacket.getUsername());
			
			player.addComponent(randomPosComp);
			player.addComponent(networkComp);
			player.addComponent(userComp);
			
			ObservableList<Entity> entities = world.getEntities();
			
			synchronized(entities) {
				world.addEntity(player);// Assigns id (WorldMP).
			}
			
			String loginMessage = userComp.getUsername() + " has joined the game...";
			Logger.log(Logger.DEBUG, loginMessage);
			
			sendPacketToAllClients(null, new SPacket03Message(Server.SIMPLE_NAME, loginMessage));
			
			TransformComponent transformComp = player.getComponent(TransformComponent.class);
			transformComp.setX(0d);
			transformComp.setY(0d);
			
			SPacket01Login serverLoginResponsePacket = new SPacket01Login(networkComp, userComp, transformComp, true);
			
			// Inform all current players of this new player's login.
			sendPacketToAllClients(null, serverLoginResponsePacket);
			
			synchronized(world.getEntities()) {
				for(Entity e: world.getEntities()) {
					
					UserComponent eUserComp = e.getComponent(UserComponent.class);
					
					if(eUserComp == null) continue;
					
					SPacket01Login playerInWorldPacket = new SPacket01Login(e.getComponent(NetworkComponent.class), 
							eUserComp, e.getComponent(TransformComponent.class), true);
					
					sendPacketToClient(client, playerInWorldPacket);
					
				}
			}
			
			cim.setClientId(networkComp.getId());
			
			break;
			
		case LOGOUT:
			
			handleClientDisconnect(cim);
			
			break;
			
		case MESSAGE:
			
			CPacket03Message messagePacket = new CPacket03Message(dataAsString);
			
			username = messagePacket.getSender();
			
			String message = messagePacket.getMessage();
			
			Logger.log(Logger.DEBUG, username + " sent a message: " + message);
			
			SPacket03Message replyPacket = new SPacket03Message(username, message);
			
			sendPacketToAllClients(client, replyPacket);// Don't need to send it back to the client that sent it.
			
			break;
			
		case ENTITY:
			
			entities = world.getEntities();
			
			synchronized(entities) {
				for(Entity e: entities) {
					if(e.getComponent(NetworkComponent.class) == null) continue;
					
					String entityName = EEntity.STATIC.getName();
					
					if(e.getComponent(UserComponent.class) != null) continue;// Gets sent by login packet.
					
					sendPacketToClient(client, new SPacket04Entity(entityName, 
							e.getComponent(TransformComponent.class), false));
					
				}
				
				sendPacketToClient(client, new SPacket04Entity("", new TransformComponent(), true));
				
			}
			
			break;
			
		case INVALID:
		default:
			Logger.log(Logger.WARNING, "Invalid packet: \"" + dataAsString + "\".");
			break;
		}
		
	}
	
	/**
	 * Used when a {@code ClientInputManager} has to be disconnected <b>after</b> they have logged in.
	 * 
	 * @param cim
	 */
	private void handleClientDisconnect(ClientInputManager cim) {

		Entity player = networkManager.getServer().getEntityById(cim.getClientId());
		
		SPacket02Logout logoutPacket = new SPacket02Logout(cim.getClientId());
		sendPacketToAllClients(cim.getClient(), logoutPacket);
		
		String username = player.getComponent(UserComponent.class).getUsername();
		
		String logoutMessage = username + " has left the game...";
		
		Logger.log(Logger.DEBUG, logoutMessage);
		
		sendPacketToAllClients(cim.getClient(), new SPacket03Message(username, logoutMessage));
		
		synchronized(networkManager.getServer().getGame().getWorld().getEntities()) {
			networkManager.getServer().getGame().getWorld().removeEntity(player);
		}
		
		disconnectClient(cim);
		
	}
	
	/**
	 * Used when a {@code ClientInputManager} has to be disconnected <b>before</b> they have logged in.
	 * 
	 * @param cim
	 */
	private void disconnectClient(ClientInputManager cim) {
		
		Util.silentClose(cim.getClient());
		
		clientInputManagers.remove(cim);
		
	}
	
	private synchronized void startNewClientInputManager(Socket client) {
		
		ClientInputManager cim = new ClientInputManager(client);
		
		clientInputManagers.add(cim);
		
		Thread t = new Thread(cim, client.getInetAddress().getHostName() + " Client Manager");
		t.setDaemon(true);
		t.start();
		
	}
	
	public void sendPacketToAllClients(Socket clientToExclude, TCPPacket packet) {
		
		synchronized(clientInputManagers) {
			for(ClientInputManager cim: clientInputManagers) {
				
				Socket client = cim.getClient();
				
				if(client.equals(clientToExclude)) continue;
				
				sendPacketToClient(client, packet);
				
			}
		}
		
	}
	
	public void sendPacketToClient(Socket client, TCPPacket packet) {
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
	
	public ArrayList<ClientInputManager> getClientInputManagers() {
		return clientInputManagers;
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
	
	public class ClientInputManager implements Runnable {
		
		private final Socket client;
		
		private int clientId;
		private boolean loggedIn;
		
		public ClientInputManager(Socket client) {
			this.client = client;
			
			loggedIn = false;
			
		}
		
		@Override
		public void run() {
			
			try {
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				
				while(!client.isClosed()) {
					
					String input = reader.readLine();
					
					if(input != null) {
						handleClientInput(this, input);
					}
					
				}
				
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; client " + client.getInetAddress()
						.getHostName() + " disconnected.");
				ex.printStackTrace();
				
				if(loggedIn) {
					handleClientDisconnect(this);
				} else {
					disconnectClient(this);
				}
				
			}
			
		}
		
		public Socket getClient() {
			return client;
		}
		
		public void setClientId(int clientId) {
			this.clientId = clientId;
			loggedIn = true;
		}
		
		public int getClientId() {
			return clientId;
		}
		
	}
	
}
