package com.rawad.ballsimulator.networking.server.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.networking.server.Server;
import com.rawad.ballsimulator.networking.server.entity.EntityPlayerMP;
import com.rawad.ballsimulator.networking.server.udp.SPacket02Move;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.input.MouseEvent;

public class WorldMP extends World {
	
	private Server server;
	
	private ArrayList<EntityPlayerMP> players;
	
	public WorldMP(Server server) {
		super();
		
		this.server = server;
		
		players = new ArrayList<EntityPlayerMP>();
		
	}
	
	@Override
	public void update(long timePassed) {
		super.update(timePassed);
		
		for(EntityPlayerMP player: players) {
			player.update(timePassed, new MouseEvent(0, 0, 0));
			
			// TODO: do an "isMoved()" check right here for efficiency.
			SPacket02Move move = new SPacket02Move(player.getName(), player.getX(), player.getY(), player.getVx(), player.getVy(), 
					player.getAx(), player.getAy());
			
			server.getNetworkManager().getDatagramManager().sendPacketToAllClients(move);
			
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
//		super.render(g);/* < NullPointerException */
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, width, height);
		
		terrain.render(g);
		
		drawPlayers(g, players);
		
	}
	
	public void drawPlayers(Graphics2D g, ArrayList<EntityPlayerMP> players) {
		
		g.setColor(Color.GREEN);
		
		for(EntityPlayerMP player: players) {
			Rectangle hitbox = player.getHitbox();
			g.fillOval((int) hitbox.getX(), (int) hitbox.getY(), hitbox.width, hitbox.height);
		}
		
	}
	
	@Override
	public void addEntity(Entity e) {
		super.addEntity(e);
		
		if(e instanceof EntityPlayerMP) {
			players.add((EntityPlayerMP) e);
		}
		
	}
	
	public void disconnectPlayer(String username) {
		
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
