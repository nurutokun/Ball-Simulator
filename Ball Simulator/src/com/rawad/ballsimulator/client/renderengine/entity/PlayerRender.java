package com.rawad.ballsimulator.client.renderengine.entity;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.gamehelpers.renderengine.Render;

public class PlayerRender extends Render {
	
	private ArrayList<EntityPlayer> players;
	
	public PlayerRender() {
		super();
		
		players = new ArrayList<EntityPlayer>();
	}
	
	@Override
	public void render(Graphics2D g) {
		
		for(EntityPlayer player: players) {
			player.render(g);
		}
		
		players.clear();
		
	}
	
	public void addPlayer(EntityPlayer player) {
		players.add(player);
	}
	
}
