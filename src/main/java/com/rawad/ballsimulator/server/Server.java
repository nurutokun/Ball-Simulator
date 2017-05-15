package com.rawad.ballsimulator.server;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.CollisionSystem;
import com.rawad.ballsimulator.game.MovementSystem;
import com.rawad.ballsimulator.game.PositionGenerationSystem;
import com.rawad.ballsimulator.game.RollingSystem;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.server.ServerNetworkManager;
import com.rawad.ballsimulator.server.sync.IServerSync;
import com.rawad.ballsimulator.server.sync.component.IComponentSync;
import com.rawad.ballsimulator.server.sync.component.MovementSync;
import com.rawad.ballsimulator.server.sync.component.PingSync;
import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameEngine;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.game.entity.BlueprintManager;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;

import javafx.concurrent.Task;

public class Server extends Proxy {
	
	/** Mainly used to identify the server for announcements/messages. */
	public static final String SIMPLE_NAME = "Server";
	
	private static final String TERRAIN_NAME = "terrain";
	
	public static final int PORT = 8008;
	
	private static final int TICKS_PER_UPDATE = 50;
	
	private WorldMP world;
	
	private ServerNetworkManager networkManager;
	
	private ArrayList<IServerSync> serverSyncs = new ArrayList<IServerSync>();
	private ArrayList<IComponentSync> compSyncs = new ArrayList<IComponentSync>();
	
	private int tickCount;
	
	@Override
	public void preInit(Game game) {
		super.preInit(game);
		
		world = new WorldMP(game.getGameEngine().getEntities());
		
		Loader loader = new Loader();
		
		loaders.put(loader);
		
		EntityFileParser entityFileParser = new EntityFileParser();
		
		fileParsers.put(entityFileParser);
		fileParsers.put(new TerrainFileParser());
		
		Loader.addTask(Loader.getEntityBlueprintLoadingTask(loader, entityFileParser));
		
	}
	
	@Override
	public void init() {
		
		tickCount = 0;
		
		GameEngine gameEngine = game.getGameEngine();
		
		gameEngine.addGameSystem(new PositionGenerationSystem(gameEngine, world.getWidth(), world.getHeight()));
		gameEngine.addGameSystem(new MovementSystem(gameEngine.getEventManager()));
		gameEngine.addGameSystem(new CollisionSystem(gameEngine.getEventManager(), world.getWidth(), world.getHeight()));
		gameEngine.addGameSystem(new RollingSystem());
		
		compSyncs.add(new MovementSync());
		compSyncs.add(new PingSync());
		
		networkManager = new ServerNetworkManager(this);
		
		Loader.addTask(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				BlueprintManager.getBlueprint(EEntity.STATIC).getEntityBase().addComponent(new NetworkComponent());
				
				Loader loader = loaders.get(Loader.class);
				
				TerrainFileParser parser = fileParsers.get(TerrainFileParser.class);
				
				Logger.log(Logger.DEBUG, "Loading terrain...");
				loader.loadTerrain(parser, world, TERRAIN_NAME);
				Logger.log(Logger.DEBUG, "Terrain loaded successfully.");
				
				Logger.log(Logger.DEBUG, "Initializing network manager...");
				networkManager.init();// Allows for world to be initialized before clients can connect.
				Logger.log(Logger.DEBUG, "Network manager initialized.");
				
				update = true;
				
				return null;
				
			}
		});
		
	}
	
	@Override
	public void tick() {
		
		tickCount++;
		
		if(tickCount >= TICKS_PER_UPDATE) {
			// sync players with server.
			for(Entity e: world.getEntities()) {
				
				NetworkComponent networkComp = e.getComponent(NetworkComponent.class);
				
				if(networkComp != null) {
					// Send movement, health, etc. to all players.
					for(IComponentSync compSync: compSyncs) {
						compSync.sync(e, networkManager.getDatagramManager(), networkComp);
					}
				}
				
			}
			
			for(IServerSync serverSync: serverSyncs) {
				serverSync.sync(this);
			}
			
			tickCount = 0;
			
		}
		
	}
	
	@Override
	public void terminate() {
		
		update = false;
		
		networkManager.stop();
		
		serverSyncs.clear();
		compSyncs.clear();
		
	}
	
	public ServerNetworkManager getNetworkManager() {
		return networkManager;
	}
	
	public Entity getEntityById(int id) {
		
		for(Entity e: world.getEntities()) {
			
			NetworkComponent networkComp = e.getComponent(NetworkComponent.class);
			
			if(networkComp != null && networkComp.getId() == id) return e;
			
		}
		
		return null;
		
	}
	
	public ArrayList<IServerSync> getServerSyncs() {
		return serverSyncs;
	}
	
	public ArrayList<IComponentSync> getCompSyncs() {
		return compSyncs;
	}
	
	public WorldMP getWorld() {
		return world;
	}
	
}
