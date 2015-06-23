package com.rawad.ballsimulator.client;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.rawad.ballsimulator.entity.Player;
import com.rawad.ballsimulator.input.KeyboardInput;
import com.rawad.ballsimulator.world.World;

public class Client {
	
	private World world;
	
	private Player player;
	
	private boolean paused;
	
	public Client() {
		
		this.world = new World();
		
		player = new Player(world);
		
		paused = false;
		
	}
	
	public void update(long timePassed) {
		
		// Check for server connectivity otherwise, perform game logic and update World
		if(!paused) {
			
			handleKeyboardInput();
			 
			player.update(timePassed);
		}
		
	}
	
	private void handleKeyboardInput() {
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_UP, false)) {
			player.moveUp();
		} else if(KeyboardInput.isKeyDown(KeyEvent.VK_DOWN, false)) {
			player.moveDown();
		}
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_RIGHT, false)) {
			player.moveRight();
		} else if(KeyboardInput.isKeyDown(KeyEvent.VK_LEFT, false)) {
			player.moveLeft();
		}
		
	}
	
	public void render(Graphics2D g) {
		
		world.render(g);
		
	}
	
	public void pauseGame(boolean paused) {
		// Work something out here.
		this.paused = paused;
	}
	
}
