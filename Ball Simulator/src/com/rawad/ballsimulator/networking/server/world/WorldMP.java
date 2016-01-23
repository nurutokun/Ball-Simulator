package com.rawad.ballsimulator.networking.server.world;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.networking.server.Server;
import com.rawad.ballsimulator.networking.server.entity.EntityPlayerMP;
import com.rawad.ballsimulator.networking.server.udp.SPacket02Move;
import com.rawad.ballsimulator.world.World;

public class WorldMP extends World {
	
	private Server server;
	
	private ArrayList<EntityPlayerMP> players;
	
	public WorldMP(Server server) {
		super();
		
		this.server = server;
		
		players = new ArrayList<EntityPlayerMP>();
		
	}
	
	@Override
	public synchronized void update(long timePassed) {
		super.update(timePassed);
		
		for(EntityPlayerMP player: players) {
			player.update(timePassed, -100, -100);
			
			// TODO: do an "isMoved()" check right here for efficiency.
			SPacket02Move move = new SPacket02Move(player.getName(), player.getX(), player.getY(), player.getVx(), player.getVy(), 
					player.getAx(), player.getAy());
			
			server.getNetworkManager().getDatagramManager().sendPacketToAllClients(move);
			
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
