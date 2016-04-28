package com.rawad.ballsimulator.networking.client;

import com.rawad.ballsimulator.client.gamestates.MultiplayerGameState;
import com.rawad.ballsimulator.networking.ConnectionState;
import com.rawad.ballsimulator.networking.client.tcp.CPacket01Login;
import com.rawad.ballsimulator.networking.client.tcp.ClientConnectionManager;
import com.rawad.ballsimulator.networking.client.udp.CPacket02Move;
import com.rawad.ballsimulator.networking.client.udp.ClientDatagramManager;
import com.rawad.ballsimulator.server.Server;

public class ClientNetworkManager {
	
	private MultiplayerGameState client;
	
	private ClientConnectionManager connectionManager;
	private ClientDatagramManager datagramManager;
	
	/**
	 * Don't worry about touching this, should be handled by ClientConnectionManager
	 */
	private ConnectionState connectionState;
	
	/**
	 * Hopefully temporary.
	 */
	private boolean loggedIn;
	
	public ClientNetworkManager() {
		
		connectionManager = new ClientConnectionManager(this);
		datagramManager = new ClientDatagramManager(this);
		
		connectionState = ConnectionState.NOT_CONNECTED;
		
	}
	
	public void init(String address) {
		
		connectionManager.connectToServer(address, Server.PORT);
		
	}
	
	/**
	 * Should be called after {@code connectionState} has been set to {@code ConnectionState.CONNECTED}
	 */
	public void onConnect() {
		
		CPacket01Login loginPacket = new CPacket01Login(client.getPlayer().getName());
		
		connectionManager.sendPacketToServer(loginPacket);
		
		datagramManager.start();
		
		client.onConnect();
		
	}
	
	/**
	 * Mainly does the resetting work for the next time a connection is needed to be made.
	 */
	public void onDisconnect() {
		
		connectionState = ConnectionState.NOT_CONNECTED;
		
		setLoggedIn(false);
		
	}
	
	public void requestDisconnect() {
		
		if(isConnectedToServer()) {
			
			datagramManager.stop();
			connectionManager.disconnect();
			
		}
		
		connectionState = ConnectionState.DISCONNECTED;
		
		client.onDisconnect();
		
	}
	
	public void updatePlayerMovement(boolean up, boolean down, boolean right, boolean left) {
		
		CPacket02Move movePacket = new CPacket02Move(client.getPlayer().getName(), up, down, right, left);
		
		datagramManager.sendPacket(movePacket, connectionManager.getServerAddress(), Server.PORT);
		
	}
	
	public ClientConnectionManager getConnectionManager() {
		return connectionManager;
	}
	
	public MultiplayerGameState getClient() {
		return client;
	}
	
	public void setConnectionState(ConnectionState state) {
		this.connectionState = state;
		
	}
	
	public ConnectionState getConnectionState() {
		return connectionState;
	}
	
	public boolean isConnectingToServer() {
		return connectionState == ConnectionState.CONNECTING;
	}
	
	public boolean isConnectedToServer() {
		return connectionState == ConnectionState.CONNECTED;
	}
	
	public boolean isDisconnectedFromServer() {
		return connectionState == ConnectionState.DISCONNECTED;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void setClient(MultiplayerGameState client) {
		this.client = client;
	}
	
}
