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
import com.rawad.ballsimulator.networking.server.Server;
import com.rawad.ballsimulator.networking.server.tcp.SPacket01Login;
import com.rawad.ballsimulator.networking.server.tcp.SPacket02Logout;
import com.rawad.gamehelpers.log.Logger;

public class ClientConnectionManager {
	
	private ClientNetworkManager networkManager;
	
	private Socket socket;
	
	private Thread connectionHandler;
	
	private String serverAddress;
	
	public ClientConnectionManager(ClientNetworkManager networkManager) {
		this.networkManager = networkManager;
		
	}
	
	public void connectToServer(String address) {
		
		if(!networkManager.isConnectedToServer()) {
			
			this.serverAddress = address;
			
			connectionHandler = new Thread(new ConnectionHandler(address, Server.PORT), "Connection Handler");
			
			connectionHandler.start();
		}
		
	}
	
	public void disconnect() {
		
		if(socket != null && !socket.isClosed()) {
			try {
				
				CPacket02Logout logoutPacket = new CPacket02Logout(networkManager.getClient().getPlayer().getName(),
						socket.getInetAddress().getHostAddress());
				
				sendPacketToServer(logoutPacket);
				
				socket.close();
				
				networkManager.setConnectionState(ConnectionState.DISCONNECTED);
				networkManager.onDisconnect();
				
			} catch (Exception ex) {
				Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; couldn't close client socket.");
			}
		}
		
	}
	
	private void handleServerInput(String input) {
		
		TCPPacketType type = Packet.getTCPPacketTypeFromData(input.getBytes());
		
		switch(type) {
		
		case LOGIN:
			
			SPacket01Login loginReplyPacket = new SPacket01Login(input.getBytes());
			
			EntityPlayer mainClientPlayer = networkManager.getClient().getPlayer();
			
			if(!loginReplyPacket.canLogin() && mainClientPlayer.getName().equals(loginReplyPacket.getUsername())) {// Denied login
				Logger.log(Logger.DEBUG, "Login denied by server.");
				networkManager.setConnectionState(ConnectionState.DISCONNECTED);
				break;
			}
			
			EntityPlayer player = mainClientPlayer;
			
			String receivedUsername = loginReplyPacket.getUsername();
			
			if(!player.getName().equals(receivedUsername)) {// Player that is logging in isn't the client's player, so create a new player.
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
			
			networkManager.setConnectionState(ConnectionState.CONNECTED);
			
			break;
			
		case LOGOUT:
			
			SPacket02Logout logoutPacket = new SPacket02Logout(input.getBytes());
			
			EntityPlayer mainPlayer = networkManager.getClient().getPlayer();
			
			if(mainPlayer.getName().equals(logoutPacket.getUsername())) {
				networkManager.setConnectionState(ConnectionState.DISCONNECTED);
				networkManager.onDisconnect();
			} else {
				networkManager.getClient().getWorld().removeEntityByName(logoutPacket.getUsername());
			}
			
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
		
		private String address;
		
		private int port;
		
		public ConnectionHandler(String address, int port) {
			this.address = address;
			this.port = port;
			
		}
		
		@Override
		public void run() {
			
			try {
				
				networkManager.setConnectionState(ConnectionState.CONNECTING);
				
				socket = new Socket(address, port);
				
				networkManager.setConnectionState(ConnectionState.CONNECTED);
				networkManager.onConnect();
				
				CPacket01Login loginPacket = new CPacket01Login(networkManager.getClient().getPlayer().getName());
				
				sendPacketToServer(loginPacket);
				
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; couldn't connect to \"" + address + "\", on the port " + port);
				
				networkManager.setConnectionState(ConnectionState.DISCONNECTED);
				
				return;
			}
			
			try (	BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					) {
				
				while(!socket.isClosed()) {
					
					String input = reader.readLine();
					
					if(input != null) {
						handleServerInput(input);
					}
					
				}
				
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; client lost connection to server or"
						+ " socket was closed by other means.");
			} finally {
				networkManager.setConnectionState(ConnectionState.DISCONNECTED);
				networkManager.onDisconnect();
			}
			
			networkManager.requestDisconnect();// After breaking out of loop, request to disconnect from server.
			
		}
		
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String getServerAddress() {
		return serverAddress;
	}
	
}
