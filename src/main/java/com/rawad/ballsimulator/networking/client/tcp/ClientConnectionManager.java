package com.rawad.ballsimulator.networking.client.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.networking.ConnectionState;
import com.rawad.ballsimulator.networking.Packet;
import com.rawad.ballsimulator.networking.TCPPacketType;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.server.tcp.SPacket01Login;
import com.rawad.ballsimulator.networking.server.tcp.SPacket02Logout;
import com.rawad.ballsimulator.networking.server.tcp.SPacket03Message;
import com.rawad.gamehelpers.log.Logger;

/**
 * Handles connecting to the server and telling the server when to disconnect/knowing when to get disconnected. Also just handles TCP 
 * packets in general.
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
					throw new Exception("Disconnect requested");
				}
				
				networkManager.setConnectionState(ConnectionState.CONNECTED);
				networkManager.onConnect();
				
				connectionHandler = new Thread(new ConnectionHandler(socket), "Connection Handler");
				
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
			
			CPacket02Logout logoutPacket = new CPacket02Logout(networkManager.getClient().getPlayer().getName(), 
					socket.getInetAddress().getHostAddress());
			
			sendPacketToServer(logoutPacket);
			
			socket.close();
			
		} catch (Exception ex) {
			Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; couldn't close client socket.");
		}
		
	}
	
	private void handleServerInput(String input) {
		
		byte[] data = input.getBytes();
		
		TCPPacketType type = Packet.getTCPPacketTypeFromData(data);
		
		switch(type) {
		
		case LOGIN:
			
			SPacket01Login loginReplyPacket = new SPacket01Login(data);
			
			EntityPlayer mainClientPlayer = networkManager.getClient().getPlayer();
			
			// Denied login
			if(!loginReplyPacket.canLogin() && mainClientPlayer.getName().equals(loginReplyPacket.getUsername())) {
				Logger.log(Logger.DEBUG, "Login denied by server.");
				networkManager.requestDisconnect();
				
				break;
				
			}
			
			EntityPlayer player = mainClientPlayer;
			
			String receivedUsername = loginReplyPacket.getUsername();
			
			// Player that is logging in isn't the client's player, so create a new player.
			if(!player.getName().equals(receivedUsername)) {
				player = new EntityPlayer(networkManager.getClient().getWorld());
				
				player.setName(loginReplyPacket.getUsername());
				
			}
			
			player.setX(loginReplyPacket.getX());
			player.setY(loginReplyPacket.getY());
			
			player.setWidth(loginReplyPacket.getWidth());
			player.setHeight(loginReplyPacket.getHeight());
			
			player.setTheta(loginReplyPacket.getTheta());
			
			player.updateHitbox();
			
			networkManager.getClient().loadTerrain(loginReplyPacket.getTerrainName());
			
			networkManager.setLoggedIn(true);
			
			break;
			
		case LOGOUT:
			
			SPacket02Logout logoutPacket = new SPacket02Logout(data);
			
			EntityPlayer mainPlayer = networkManager.getClient().getPlayer();
			
			if(mainPlayer.getName().equals(logoutPacket.getUsername())) {
				networkManager.setLoggedIn(false);
				
				networkManager.requestDisconnect();
				
			} else {
				
				networkManager.getClient().getWorld().removeEntityByName(logoutPacket.getUsername());
				
			}
			
			break;
			
		case MESSAGE:
			
			// When receiving a message from another client.
			SPacket03Message messagePacket = new SPacket03Message(data);
			
			networkManager.addUserMessage(messagePacket.getMessage());
			
			break;
			
		case INVALID:
		default:
			Logger.log(Logger.WARNING, "Invalid packet: \"" + input + "\".");
			break;
		
		}
		
	}
	
	public void sendPacketToServer(Packet packet) {
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
						handleServerInput(input);
					}
					
				}
				
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; client lost connection to server or"
						+ " socket was closed by other means.");
				
				networkManager.requestDisconnect();
				
			}
			
//			disconnect();// After breaking out of loop, request to disconnect from server. //(socket already closed)
			
		}
		
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String getServerAddress() {
		return serverAddress;
	}
	
}
