package com.rawad.ballsimulator.client;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.input.KeyboardInput;
import com.rawad.ballsimulator.world.World;

public class Client {
	
	private World world;
	
	private EntityPlayer player;
	
	private boolean paused;
	
	public Client() {
		
		this.world = new World();
		
		player = new EntityPlayer(world);
		
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
		
		/**/
		boolean up = KeyboardInput.isKeyDown(KeyEvent.VK_UP, false) | KeyboardInput.isKeyDown(KeyEvent.VK_W, false);
		boolean down = KeyboardInput.isKeyDown(KeyEvent.VK_DOWN, false) | KeyboardInput.isKeyDown(KeyEvent.VK_S, false);
		boolean right = KeyboardInput.isKeyDown(KeyEvent.VK_RIGHT, false) | KeyboardInput.isKeyDown(KeyEvent.VK_D, false);
		boolean left = KeyboardInput.isKeyDown(KeyEvent.VK_LEFT, false) | KeyboardInput.isKeyDown(KeyEvent.VK_A, false);
		
		if(up) {
			player.moveUp();
		} else if(down) {
			player.moveDown();
		}
		
		if(right) {
			player.moveRight();
		} else if(left) {
			player.moveLeft();
		}
		/**/
		
		/*/
		int[] keysPressed = KeyboardInput.getPressedKeys();
		
		if(keysPressed.length <= 0) {
			player.stopMoving();
		}
		
		for(int i = 0; i < keysPressed.length; i++) {
			
			int keyCode = keysPressed[i];
			
			switch(keyCode) {
			
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				
				player.moveUp();
				
				break;
				
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				
				player.moveDown();
				
				break;
				
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				
				player.moveRight();
				
				break;
				
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				
				player.moveLeft();
				
				break;
				
			}
			
		}
		/**/
		
	}
	
	public void render(Graphics2D g) {
		
		world.render(g);
		
	}
	
	public void pauseGame(boolean paused) {
		// Work something out here.
		this.paused = paused;
	}
	
}
