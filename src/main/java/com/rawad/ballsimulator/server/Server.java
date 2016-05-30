package com.rawad.ballsimulator.server;

import java.util.ArrayList;

import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.server.ServerNetworkManager;
import com.rawad.ballsimulator.server.entity.NetworkComponent;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.world.World;
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
		
		game.setWorld(new WorldMP());
		
		ArrayList<GameSystem> gameSystems = new ArrayList<GameSystem>();
		
		// TODO: Add game systems.
		// RandomGenerationSystem
		
		game.getGameEngine().setGameSystems(gameSystems);
		
		networkManager = new ServerNetworkManager(this);
		
		CustomLoader loader = game.getLoader(CustomLoader.class);
		
		TerrainFileParser parser = game.getFileParser(TerrainFileParser.class);
		
		World world = game.getWorld();
		
		addTask(new Task<Integer>() {
			
			@Override
			protected Integer call() throws Exception {
				
				Logger.log(Logger.DEBUG, "Initializing network manager...");
				networkManager.init();// Allows for world to be initialized before clients can connect.
				Logger.log(Logger.DEBUG, "Network manager initialized.");
				
				Logger.log(Logger.DEBUG, "Loading terrain...");				
				loader.loadTerrain(parser, world, Server.TERRAIN_NAME);
				
				Logger.log(Logger.DEBUG, "Terrain loaded successfully.");
				
				return 0;
				
			}
		});
		
	}
	
	@Override
	public void tick() {
		
	}
	
	@Override
	public void stop() {
		networkManager.stop();
	}
	
	public ServerNetworkManager getNetworkManager() {
		return networkManager;
	}
	
	public Entity getEntityById(int id) {
		
		for(Entity e: game.getWorld().getEntitiesAsList()) {
			
			NetworkComponent networkComp = e.getComponent(NetworkComponent.class);
			
			if(networkComp != null && networkComp.getId() == id) return e;
			
		}
		
		return null;
		
	}
	
	private static class WorldMP extends World {
		
		private int entityIdCounter = 0;
		
		@Override
		public boolean addEntity(Entity e) {
			
			NetworkComponent networkComp = e.getComponent(NetworkComponent.class);
			
			if(networkComp == null) {
				networkComp = new NetworkComponent();
				e.addComponent(networkComp);
			}
			
			networkComp.setId(entityIdCounter++);
			
			if(entityIdCounter >= Integer.MAX_VALUE) entityIdCounter = Integer.MIN_VALUE;
			
			if(entityIdCounter == -1) Logger.log(Logger.SEVERE, "The absolute entity cap for this world has been reached.");
			
			return super.addEntity(e);
		}
		
	}
	
}
