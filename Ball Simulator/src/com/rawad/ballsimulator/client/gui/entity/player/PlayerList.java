package com.rawad.ballsimulator.client.gui.entity.player;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.gamehelpers.gui.TextContainer;

/**
 * Need to implement better {@code String} rendering to allow us to get a proper width/height of the newly rendered 
 * {@code String}.
 * 
 * @author Rawad
 *
 */
public class PlayerList extends TextContainer {
	
	private ArrayList<EntityPlayer> players;
	
	public PlayerList(String id, int x, int y, int width, int height) {
		super(id, x, y, width, height);
		
		players = new ArrayList<EntityPlayer>();
		
	}
	
	public void addPlayer(EntityPlayer player) {
		players.add(player);
	}
	
	public void removePlayer(EntityPlayer player) {
		players.remove(player);
	}
	
}
