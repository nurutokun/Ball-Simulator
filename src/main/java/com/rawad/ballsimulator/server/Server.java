package com.rawad.ballsimulator.server;

import java.util.ArrayList;

import com.rawad.ballsimulator.networking.server.ServerNetworkManager;
import com.rawad.ballsimulator.server.entity.EntityPlayerMP;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.server.AServer;

import javafx.concurrent.Task;

public class Server extends AServer {
	
	/** Mainly used to identify the server for announcements/messages. */
	public static final String SIMPLE_NAME = "Server";
	
	public static final int PORT = 8008;
	
	public static final String TERRAIN_NAME = "terrain";
	
	private ServerNetworkManager networkManager;
	
	@Override
	public void init(Game game) {
		super.init(game);
		
		controller = new ServerController(this);
		
		this.<ServerController>getController().init(game);
		
		networkManager = new ServerNetworkManager(this);
		
		addTask(new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				
				Logger.log(Logger.DEBUG, "Initializing network manager...");
				networkManager.init();// Allows for world to be initialized before clients can connect.
				Logger.log(Logger.DEBUG, "Network manager initialized.");
				
				return 0;
			}
		});
		
	}
	
	public void updatePlayerNamesList() {
		
		ArrayList<EntityPlayerMP> players = this.<ServerController>getController().getWorld().getPlayers();
		
		String[] names = new String[players.size()];
		
		for(int i = 0; i < players.size(); i++) {
			
			names[i] = players.get(i).getName();
			
		}
		
	}
	
	public ServerNetworkManager getNetworkManager() {
		return networkManager;
	}
	
	@Override
	public void stop() {
		networkManager.stop();
	}
	
}
