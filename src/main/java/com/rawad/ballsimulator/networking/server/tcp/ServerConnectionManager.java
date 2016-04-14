package com.rawad.ballsimulator.networking.server.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.rawad.ballsimulator.networking.Packet;
import com.rawad.ballsimulator.networking.TCPPacketType;
import com.rawad.ballsimulator.networking.client.tcp.CPacket01Login;
import com.rawad.ballsimulator.networking.client.tcp.CPacket02Logout;
import com.rawad.ballsimulator.networking.client.tcp.CPacket03Message;
import com.rawad.ballsimulator.networking.server.Server;
import com.rawad.ballsimulator.networking.server.ServerNetworkManager;
import com.rawad.ballsimulator.networking.server.entity.EntityPlayerMP;
import com.rawad.ballsimulator.networking.server.world.WorldMP;
import com.rawad.gamehelpers.log.Logger;

/**
 * TCP server for accepting and terminating server's connections with clients. Also deals with TCP packets in 
 * general.
 * 
 * @author Rawad
 *
 */
public class ServerConnectionManager {
	
	private ServerNetworkManager networkManager;
	
	private ArrayList<Socket> clients;
	private ArrayList<ClientInputManager> clientInputManagers;
	
	private Thread connectionAcceptor;
	
	// TODO: Prevent clients from connecting before rest of server is initialized (e.g. world)
	public ServerConnectionManager(ServerNetworkManager networkManager) {
		this.networkManager = networkManager;
		
		clients = new ArrayList<Socket>();
		clientInputManagers = new ArrayList<ClientInputManager>();
		
		connectionAcceptor = new Thread(new ConnectionAcceptor(), "Connection Acceptor");
		
	}
	
	public void start() {
		
		connectionAcceptor.start();
		
	}
	
	private synchronized void handleClientInput(Socket client, String input) {
		
		byte[] data = input.getBytes();
		
		TCPPacketType type = Packet.getTCPPacketTypeFromData(data);
		
		WorldMP world = networkManager.getServer().getController().getWorld();
		
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
				
				networkManager.getServer().updatePlayerNamesList();
				
				String loginMessage = username + " has joined the game...";
				
				Logger.log(Logger.DEBUG, loginMessage);
				
				clients.add(client);// Client is now officially added, mainly so datagram isn't sending data to
				// clients that haven't logged in yet
				
				sendPacketToAllClients(null, new SPacket03Message(username, loginMessage));
				
			}
			
			SPacket01Login serverLoginResponsePacket = new SPacket01Login(username, x, y, playerWidth, playerHeight, 
					theta, Server.TERRAIN_NAME, canLogin);
			
			if(canLogin) {
				
				ClientInputManager cim = getClientInputManager(client);
				
				cim.setName(username);
				cim.setLoggedIn(true);
				
				// Inform all current players of this new player's login.
				sendPacketToAllClients(null, serverLoginResponsePacket);
				
				ArrayList<EntityPlayerMP> players = world.getPlayers();
				
				// Informs player that just logged in of previously logged-in players.
				for(EntityPlayerMP playerInWorld: players) {
					
					String name = playerInWorld.getName();
					
					if(!player.getName().equals(name)) {
						
						serverLoginResponsePacket = new SPacket01Login(name, playerInWorld.getX(), 
								playerInWorld.getY(), playerInWorld.getWidth(), playerInWorld.getHeight(), 
								playerInWorld.getTheta(), Server.TERRAIN_NAME, canLogin);
						
						sendPacketToClient(client, serverLoginResponsePacket);
						
					}
					
				}
				
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
			
			sendPacketToAllClients(client, new SPacket03Message(username, logoutMessage));
			
			break;
			
		case MESSAGE:
			
			CPacket03Message messagePacket = new CPacket03Message(data);
			
			username = messagePacket.getUsername();
			
			String message = username + "> " + messagePacket.getMessage();
			// Send message to other clients with the username indicated
			
			SPacket03Message replyPacket = new SPacket03Message(username, message);
			
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
			
			networkManager.getServer().updatePlayerNamesList();
			
			clients.remove(client);
			
			removeClientInputManagerThread(client);
			
			try {
				client.close();
			} catch(Exception ex) {
				Logger.log(Logger.SEVERE, "Couldn't disconnect client: " + client.getInetAddress().getAddress());
			}
			
	}
	
	private void removeClientInputManagerThread(Socket clientToRemove) {
		
		clientInputManagers.remove(getClientInputManager(clientToRemove));
		
	}
	
	private synchronized void startNewClientInputManager(Socket client) {
		
//		clients.add(client);// Moved to where the player logs in.
		
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
	
	public void sendPacketToAllClients(Socket clientToExclude, Packet packet) {
		
		for(Socket client: clients) {
			
			if(client.equals(clientToExclude)) {
				continue;
			}
			
			sendPacketToClient(client, packet);
			
		}
		
	}
	
	public void sendPacketToClient(Socket client, Packet packet) {
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
	
	public Socket[] getClients() {
		
		Socket[] re = new Socket[clients.size()];
		
		for(int i = 0; i < re.length; i++) {
			re[i] = clients.get(i);
		}
		
		return re;
	}
	
	private class ConnectionAcceptor implements Runnable {
		
		private ServerSocket serverSocket;
		
		@Override
		public void run() {
			
			try {
				// Rather initialize this here than in the constructor...
				serverSocket = new ServerSocket(Server.PORT);
			} catch(Exception ex) {
				Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; server socket for TCP connection couldn't "
						+ "be initialized.");
				System.exit(-1);
			}
			
			while(networkManager.getServer().getGame().isRunning()) {
				
				try {
					Socket client = serverSocket.accept();
					
					startNewClientInputManager(client);
					
				} catch(Exception ex) {
					Logger.log(Logger.WARNING, ex.getMessage());
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
			
			if(loggedIn) {// Mainly for when client closes game while still connecting (TCP is connected but not logged in yet)
				
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
		
		public void setLoggedIn(boolean loggedIn) {
			this.loggedIn = loggedIn;
		}
		
	}
	
}
