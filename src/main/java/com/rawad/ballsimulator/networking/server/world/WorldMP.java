package com.rawad.ballsimulator.networking.server.world;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.networking.server.Server;
import com.rawad.ballsimulator.networking.server.entity.EntityPlayerMP;
import com.rawad.ballsimulator.world.World;

public class WorldMP extends World {
	
	private Server server;
	
	private ArrayList<EntityPlayerMP> players;
	
	private int ticksPerUpdate;
	private int tickCount;
	
	public WorldMP(Server server) {
		super();
		
		this.server = server;
		
		players = new ArrayList<EntityPlayerMP>();
		
		ticksPerUpdate = 20;// Update client view every 20 ticks
		tickCount = 0;
		
	}
	
	@Override
	public synchronized void update() {
		super.update();
		
		tickCount++;
		
		if(tickCount >= ticksPerUpdate) {
			
			/**/
			for(EntityPlayerMP player: players) {
				
				server.getNetworkManager().getDatagramManager().sendMoveUpdate(player);
				
			}/**/
			
			tickCount = 0;
			
		}
		
	}
		
	@Override
	public synchronized void addEntity(Entity e) {
		super.addEntity(e);
		
		if(e instanceof EntityPlayerMP) {
			players.add((EntityPlayerMP) e);
		}
		
	}
	
	public synchronized void disconnectPlayer(String username) {
		
		EntityPlayerMP playerToRemove = null;
		
		for(EntityPlayerMP player: players) {
			
			if(player.getName().equals(username)) {
				playerToRemove = player;
				break;
			}
			
		}
		
		if(playerToRemove != null) {
			entities.remove(playerToRemove);
			players.remove(playerToRemove);
		}
		
	}
	
	public ArrayList<EntityPlayerMP> getPlayers() {
		return players;
	}
	
}
