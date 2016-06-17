package com.rawad.ballsimulator.networking.client;

import com.rawad.ballsimulator.client.gamestates.MultiplayerGameState;
import com.rawad.ballsimulator.networking.ConnectionState;
import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.client.tcp.CPacket01Login;
import com.rawad.ballsimulator.networking.client.tcp.CPacket04Entity;
import com.rawad.ballsimulator.networking.client.tcp.ClientConnectionManager;
import com.rawad.ballsimulator.networking.client.udp.ClientDatagramManager;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.ballsimulator.server.Server;
import com.rawad.gamehelpers.game.entity.Entity;

public class ClientNetworkManager {
	
	private final MultiplayerGameState client;
	
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
	
	public ClientNetworkManager(MultiplayerGameState client) {
		
		this.client = client;
		
		connectionManager = new ClientConnectionManager(this);
		datagramManager = new ClientDatagramManager(this);
		
		connectionState = ConnectionState.NOT_CONNECTED;
		
	}
	
	public void connectToServer(String address) {
		
		connectionManager.connectToServer(address, Server.PORT);
		
	}
	
	/**
	 * Should be called after {@code connectionState} has been set to {@code ConnectionState.CONNECTED}
	 */
	public void onConnect() {
		
		client.setMessage("Requesting entities...");
		
		connectionManager.sendPacketToServer(new CPacket04Entity());
		
		Entity player = client.getPlayer();
		
		UserComponent userComp = player.getComponent(UserComponent.class);
		userComp.setIp(connectionManager.getSocket().getLocalAddress().getHostAddress());
		
		CPacket01Login loginPacket = new CPacket01Login(player.getComponent(NetworkComponent.class), userComp);
		
		connectionManager.sendPacketToServer(loginPacket);
		
		datagramManager.start();
		
	}
	
	/**
	 * Mainly does the resetting work for the next time a connection is needed to be made.
	 */
	public void onDisconnect() {
		
		connectionState = ConnectionState.NOT_CONNECTED;
		
		setLoggedIn(false);
		
	}
	
	public void onEntityLoadFinish() {
		client.onConnect();
	}
	
	public void requestDisconnect() {
		
		if(isConnectedToServer()) {
			
			datagramManager.stop();
			connectionManager.disconnect();
			
		}
		
		connectionState = ConnectionState.DISCONNECTED;
		
		client.onDisconnect();
		
	}
	
	/**
	 * Sends a {@code UDPPacket} packet with the {@code datagramManager}.
	 * 
	 * @param packet
	 */
	public void sendPacket(UDPPacket packet) {
		datagramManager.sendPacket(packet, connectionManager.getServerAddress(), Server.PORT);		
	}
	
	public void sendPacket(TCPPacket packet) {
		connectionManager.sendPacketToServer(packet);
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
	
	public synchronized boolean isLoggedIn() {// synchronized because it's used in datagram manager; doesn't work w/-out.
		return loggedIn;
	}
	
}
