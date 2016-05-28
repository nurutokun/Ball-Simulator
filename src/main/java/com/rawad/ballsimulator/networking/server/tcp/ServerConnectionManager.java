package com.rawad.ballsimulator.networking.server.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.rawad.ballsimulator.networking.APacket;
import com.rawad.ballsimulator.networking.TCPPacketType;
import com.rawad.ballsimulator.networking.client.tcp.CPacket01Login;
import com.rawad.ballsimulator.networking.client.tcp.CPacket02Logout;
import com.rawad.ballsimulator.networking.client.tcp.CPacket04Message;
import com.rawad.ballsimulator.networking.server.ServerNetworkManager;
import com.rawad.ballsimulator.server.Server;
import com.rawad.ballsimulator.server.ServerController;
import com.rawad.ballsimulator.server.entity.EntityPlayerMP;
import com.rawad.ballsimulator.server.world.WorldMP;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.ArrayObservableList;
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
		
		synchronized(clientInputManagers) {
			for(ClientInputManager cim: clientInputManagers) {
				
				SPacket02Logout logoutPlayer = new SPacket02Logout(cim.getName());
				
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
	
	private synchronized void handleClientInput(Socket client, String input) {
		
		byte[] data = input.getBytes();
		
		TCPPacketType type = APacket.getTCPPacketTypeFromData(data);
		
		WorldMP world = networkManager.getServer().<ServerController>getController().getWorld();
		
		String username;
		
		switch(type) {
		
		case LOGIN:
			
			CPacket01Login clientLoginPacket = new CPacket01Login(data);
			
			username = clientLoginPacket.getUsername();
			
			boolean canLogin = true;
			
			if(world.getEntityByName(username) != null) {// Entity already exists
				canLogin = false;
				Logger.log(Logger.DEBUG, "Player with name \"" + username + "\" is already logged in, "
						+ "disconnecting new player.");
			}
			
			EntityPlayerMP player = null;
			
			int playerWidth = 0;
			int playerHeight = 0;
			
			double x = 0;
			double y = 0;
			
			double theta = 0;
			
			if(canLogin) {
				
				player = new EntityPlayerMP(world, clientLoginPacket.getUsername(), client.getInetAddress()
						.getHostAddress());
				// Player automatically added to server's world.
				
				world.generateCoordinates(player);
				
				x = player.getX();
				y = player.getY();
				
				playerWidth = player.getWidth();
				playerHeight = player.getHeight();
				
				theta = player.getTheta();
				
				player.setName(username);
				
				String loginMessage = username + " has joined the game...";
				
				Logger.log(Logger.DEBUG, loginMessage);
				
				sendPacketToAllClients(null, new SPacket04Message(Server.SIMPLE_NAME, loginMessage));
				
			}
			
			SPacket01Login serverLoginResponsePacket = new SPacket01Login(username, x, y, playerWidth, playerHeight, 
					theta, Server.TERRAIN_NAME, canLogin);
			
			if(canLogin) {
				
				ClientInputManager cim = getClientInputManager(client);
				
				cim.setName(username);
				cim.setLoggedIn(true);
				
				// Inform all current players of this new player's login.
				sendPacketToAllClients(null, serverLoginResponsePacket);
				
				ArrayObservableList<EntityPlayerMP> players = world.getPlayers();
				
				// Informs player that just logged in of previously logged-in players.
				for(EntityPlayerMP playerInWorld: players) {
					
					String name = playerInWorld.getName();
					
					if(!player.getName().equals(name)) {
						
						serverLoginResponsePacket = new SPacket01Login(name, playerInWorld.getX(), 
								playerInWorld.getY(), playerInWorld.getWidth(), playerInWorld.getHeight(), 
								playerInWorld.getTheta(), Server.TERRAIN_NAME, true);
						
						sendPacketToClient(client, serverLoginResponsePacket);
						
					}
					
				}
				
				clients.add(client);// Client is now officially added, mainly so datagram isn't sending data to
				// clients that haven't logged in yet and so that all other players are registered on the client so the client
				// isn't trying to update non-logged in players
				
			} else {
				sendPacketToClient(client, serverLoginResponsePacket);
			}
			
			break;
			
		case LOGOUT:
			
			CPacket02Logout logoutPacket = new CPacket02Logout(data);
			
			getClientInputManager(client).setLoggedIn(false);
			
			username = logoutPacket.getUsername();
			
			disconnectClient(client, username, world);
			
			SPacket02Logout clientInformerPacket = new SPacket02Logout(logoutPacket.getUsername());
			
			sendPacketToAllClients(null, clientInformerPacket);
			
			String logoutMessage = username + " has left the game...";
			
			Logger.log(Logger.DEBUG, logoutMessage);
			
			sendPacketToAllClients(client, new SPacket04Message(username, logoutMessage));
			
			break;
			
		case MESSAGE:
			
			CPacket04Message messagePacket = new CPacket04Message(data);
			
			username = messagePacket.getUsername();
			
			String message = messagePacket.getMessage();
			
			Logger.log(Logger.DEBUG, username + " sent a message: " + message);
			
			SPacket04Message replyPacket = new SPacket04Message(username, message);
			
			sendPacketToAllClients(client, replyPacket);// Don't need to send it back to the client that sent it.
			
			break;
			
		case INVALID:
		default:
			Logger.log(Logger.WARNING, "Invalid packet: \"" + input + "\".");
			break;
		}
		
	}
	
	private void disconnectClient(Socket client, String username, WorldMP world) {
		
			world.disconnectPlayer(username);
			
			clients.remove(client);
			
			removeClientInputManagerThread(client);
			
			Util.silentClose(client);
			
	}
	
	private void removeClientInputManagerThread(Socket clientToRemove) {
		
		clientInputManagers.remove(getClientInputManager(clientToRemove));
		
	}
	
	private synchronized void startNewClientInputManager(Socket client) {
		
		ClientInputManager clientInputManager = new ClientInputManager(client);
		
		clientInputManagers.add(clientInputManager);
		
		new Thread(clientInputManager, client.getInetAddress().getHostName() + " Client Manager").start();
		
	}
	
	private synchronized ClientInputManager getClientInputManager(Socket client) {
		
		for(ClientInputManager manager: clientInputManagers) {
			
			if(manager.getClient().equals(client)) {
				return manager;
			}
			
		}
		
		return null;
		
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
		
		private Socket client;
		
		private String clientName;
		
		private boolean loggedIn;
		
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
			
			if(loggedIn) {// Mainly for when client closes game while still connecting (TCP is connected but not logged 
				// in yet)
				
				CPacket02Logout ensureLogout = new CPacket02Logout(clientName, client.getInetAddress().getHostAddress());
				
				handleClientInput(client, ensureLogout.getDataAsString());
				
			}
			
		}
		
		public Socket getClient() {
			return client;
		}
		
		public void setName(String clientName) {
			this.clientName = clientName;
		}
		
		public String getName() {
			return clientName;
		}
		
		public void setLoggedIn(boolean loggedIn) {
			this.loggedIn = loggedIn;
		}
		
	}
	
}
