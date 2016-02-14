package com.rawad.ballsimulator.networking.server;

import java.util.ArrayList;

import com.rawad.ballsimulator.networking.server.entity.EntityPlayerMP;
import com.rawad.ballsimulator.networking.server.main.WindowManager;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.input.Mouse;
import com.rawad.gamehelpers.log.Logger;

public class Server extends Proxy {
	
	public static final int PORT = 8008;
	
	public static final String TERRAIN_NAME = "terrain";
	
	private Game game;
	
	private ServerNetworkManager networkManager;
	
	private ServerController viewportController;
	
	public Server() { 
		
	}
	
	@Override
	public void init(Game game) {
		
		this.game = game;
		
		networkManager = new ServerNetworkManager(this);
		
		networkManager.init();
		
		viewportController = new ServerController(this);
		
		viewportController.init(game);
		
	}
	
	@Override
	public void initGUI() {
		
		viewportController.initGUI();
		
		WindowManager.instance().initialize(this);
		
	}
	
	@Override
	public void tick() {
		
		viewportController.tick();
		
		WindowManager wm = WindowManager.instance();
		
		String logBuffer = Logger.getBuffer();
		
		if(!logBuffer.isEmpty()) {
			wm.addDebugText(logBuffer);
		}
		
		Mouse.update(viewportController.getPanel());
		
	}
	
	public void stop() {
		
		WindowManager.instance().close();
		
	}
	
	public void updatePlayerNamesList() {
		
		ArrayList<EntityPlayerMP> players = viewportController.getWorld().getPlayers();
		
		String[] names = new String[players.size()];
		
		for(int i = 0; i < players.size(); i++) {
			
			names[i] = players.get(i).getName();
			
		}
		
		WindowManager.instance().setPlayerNames(names);
		
	}
	
	public Game getGame() {
		return game;
	}
	
	public ServerNetworkManager getNetworkManager() {
		return networkManager;
	}
	
	/**
	 * Overriden to return a proper <code>IController</code>, more relevant for the <code>Server</code>'s context.
	 */
	@Override
	public ServerController getController() {
		return viewportController;
	}
	
}
