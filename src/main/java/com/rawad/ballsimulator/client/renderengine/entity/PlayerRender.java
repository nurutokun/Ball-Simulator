package com.rawad.ballsimulator.client.renderengine.entity;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.EntityPlayer;

import javafx.scene.canvas.GraphicsContext;

public class PlayerRender extends LivingBaseRender {
	
	private ArrayList<EntityPlayer> players;
	
	public PlayerRender() {
		super();
		
		players = new ArrayList<EntityPlayer>();
		
	}
	
	@Override
	public synchronized void render(GraphicsContext g) {
		
		for(EntityPlayer player: players) {
			player.render(g);
		}
		
		players.clear();
		
	}
	
	public synchronized void addPlayer(EntityPlayer player) {
		players.add(player);
	}
	
}
