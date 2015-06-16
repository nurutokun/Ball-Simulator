package com.rawad.ballsimulator.client;

import com.rawad.ballsimulator.entity.Player;
import com.rawad.ballsimulator.world.World;

public class Client {
	
	private Player player;
	
	public Client(World world) {
		
		player = new Player(world);
		
	}
	
	public void update(long timePassed) {
		
		// Check for server connectivity otherwise, perform game logic and update World
		player.update(timePassed);
		
	}

}
