package com.rawad.ballsimulator.networking.client;

import java.util.ArrayList;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.networking.ConnectionState;
import com.rawad.ballsimulator.networking.client.tcp.ClientConnectionManager;
import com.rawad.ballsimulator.networking.client.udp.CPacket02Move;
import com.rawad.ballsimulator.networking.client.udp.ClientDatagramManager;
import com.rawad.ballsimulator.networking.server.Server;

public class ClientNetworkManager {
	
	private Client client;
	
	private ClientConnectionManager connectionManager;
	private ClientDatagramManager datagramManager;
	
	/**
	 * Don't worry about touching this, should be handled by ClientConnectionManager
	 */
	private ConnectionState connectionState;
	
	public ClientNetworkManager(Client client) {
		
		this.client = client;
		
		connectionManager = new ClientConnectionManager(this);
		datagramManager = new ClientDatagramManager(this);
		
		connectionState = ConnectionState.NOT_CONNECTED;
		
	}
	
	public void init(String address) {
		
		connectionManager.connectToServer(address);
		
	}
	
	/**
	 * Should be called after {@code connectionState} has been set to {@code ConnectionState.CONNECTED}
	 */
	public void onConnect() {
		
		datagramManager.start();
		
	}
	
	public void onDisconnect() {
		
		datagramManager.stop();
		
		ArrayList<Entity> entities = client.getWorld().getEntities();
		ArrayList<Entity> entitiesToRemove = new ArrayList<Entity>();
		
		for(Entity e: entities) {
			
			if(e instanceof EntityPlayer && !e.equals(client.getPlayer())) {
				entitiesToRemove.add(e);
			}
			
		}
		
		for(Entity e: entitiesToRemove) {
			
			client.getWorld().removeEntity(e);
			
		}
		
	}
	
	public void requestDisconnect() {
		
		connectionManager.disconnect();
		
	}
	
	public void updatePlayerMovement(boolean up, boolean down, boolean right, boolean left) {
		
		CPacket02Move movePacket = new CPacket02Move(client.getPlayer().getName(), up, down, right, left);
		
		datagramManager.sendPacket(movePacket, connectionManager.getServerAddress(), Server.PORT);
		
	}
	
	public ClientConnectionManager getConnectionManager() {
		return connectionManager;
	}
	
	public Client getClient() {
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
	
	public boolean isDisconnectedFromServer() {
		return connectionState == ConnectionState.DISCONNECTED;
	}
	
	public boolean isConnectedToServer() {
		return connectionState == ConnectionState.CONNECTED;
	}
	
}
