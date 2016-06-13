package com.rawad.ballsimulator.server;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.game.CollisionSystem;
import com.rawad.ballsimulator.game.MovementSystem;
import com.rawad.ballsimulator.game.PositionGenerationSystem;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.server.ServerNetworkManager;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.BlueprintManager;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.server.AServer;

public class Server extends AServer {
	
	/** Mainly used to identify the server for announcements/messages. */
	public static final String SIMPLE_NAME = "Server";
	
	public static final int PORT = 8008;
	
	private static final int TICKS_PER_UPDATE = 100;
	
	private ServerNetworkManager networkManager;
	
	private int tickCount;
	
	@Override
	public void init(Game game) {
		super.init(game);
		
		tickCount = 0;
		
		WorldMP world = new WorldMP();
		
		game.setWorld(world);
		
		ArrayList<GameSystem> gameSystems = new ArrayList<GameSystem>();
		
		// TODO: Add game systems.
		// RandomGenerationSystem
		gameSystems.add(new PositionGenerationSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(new CollisionSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(new MovementSystem());
		
		game.getGameEngine().setGameSystems(gameSystems);
		
		networkManager = new ServerNetworkManager(this);
		
	}
	
	@Override
	public void tick() {
		
		tickCount++;
		
		if(tickCount >= TICKS_PER_UPDATE) {
			// sync players with server.
			for(Entity e: getGame().getWorld().getEntitiesAsList()) {
				if(Entity.compare(e, BlueprintManager.getBlueprint(EEntity.PLAYER).getEntityBase())) {
					// Send movement, health, etc. to all players.
					networkManager.getDatagramManager().sendMoveUpdate(e);
				}
			}
			
			tickCount = 0;
			
		}
		
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
				return false;
//				networkComp = new NetworkComponent();
//				e.addComponent(networkComp);
			}
			
			networkComp.setId(entityIdCounter++);
			
			if(entityIdCounter >= Integer.MAX_VALUE) entityIdCounter = Integer.MIN_VALUE;
			
			if(entityIdCounter == -1) Logger.log(Logger.SEVERE, "The absolute entity cap for this world has "
					+ "been reached.");
			
			return super.addEntity(e);
		}
		
	}
	
}
