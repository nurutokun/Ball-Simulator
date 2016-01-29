package com.rawad.ballsimulator.networking.server;

import com.rawad.ballsimulator.networking.server.tcp.ServerConnectionManager;
import com.rawad.ballsimulator.networking.server.udp.ServerDatagramManager;


public class ServerNetworkManager {
	
	private Server server;
	
	private ServerConnectionManager connectionManager;
	private ServerDatagramManager datagramManager;
	
	public ServerNetworkManager(Server server) {
		
		this.server = server;
		
		connectionManager = new ServerConnectionManager(this);
		datagramManager = new ServerDatagramManager(this);
		
	}
	
	public void init() {
		
		connectionManager.start();
		datagramManager.start();
		
	}
	
	public Server getServer() {
		return server;
	}
	
	public ServerDatagramManager getDatagramManager() {
		return datagramManager;
	}
	
	public ServerConnectionManager getConnectionManager() {
		return connectionManager;
	}
	
}
