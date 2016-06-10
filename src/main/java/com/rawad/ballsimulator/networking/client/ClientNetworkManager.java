package com.rawad.ballsimulator.networking.client;

import com.rawad.ballsimulator.client.gamestates.MultiplayerGameState;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.networking.ConnectionState;
import com.rawad.ballsimulator.networking.client.tcp.CPacket01Login;
import com.rawad.ballsimulator.networking.client.tcp.CPacket05Terrain;
import com.rawad.ballsimulator.networking.client.tcp.ClientConnectionManager;
import com.rawad.ballsimulator.networking.client.udp.CPacket02Move;
import com.rawad.ballsimulator.networking.client.udp.ClientDatagramManager;
import com.rawad.ballsimulator.server.Server;
import com.rawad.ballsimulator.server.entity.NetworkComponent;
import com.rawad.ballsimulator.server.entity.UserComponent;
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
		
		client.setMessage("Requesting terrain...");
		
		connectionManager.sendPacketToServer(new CPacket05Terrain());
		
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
	
	public void onTerrainLoadFinish() {
		client.setMessage("Done loading terrain.");
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
	
	private void updateEntityMovement(Entity entity) {
		
		NetworkComponent networkComp = entity.getComponent(NetworkComponent.class);
		MovementComponent movementComp = entity.getComponent(MovementComponent.class);
		
		CPacket02Move movePacket = new CPacket02Move(networkComp, movementComp);
		
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
	
}
