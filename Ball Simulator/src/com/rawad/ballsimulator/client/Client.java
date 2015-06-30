package com.rawad.ballsimulator.client;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.input.KeyboardInput;
import com.rawad.ballsimulator.input.MouseInput;
import com.rawad.ballsimulator.world.World;

public class Client {
	
	private World world;
	
	private EntityPlayer player;
	
	private Camera camera;
	
	private boolean paused;
	
	public Client() {
		
		world = new World();
		
		player = new EntityPlayer(world);
		
		camera = new Camera(player);
		
		paused = false;
		
	}
	
	public void update(long timePassed) {
		
		// Check for server connectivity otherwise, perform game logic and update World
		if(!paused) {
			
			handleKeyboardInput();
			
			player.update(timePassed, MouseInput.getX() + camera.getX(), MouseInput.getY() + camera.getY());
			
			if(MouseInput.isClamped()) {
				camera.update(camera.getX() + MouseInput.getX(), camera.getY() + MouseInput.getY(), 0, 0);
				
				double mouseDelta = MouseInput.getMouseWheelPosition()/1d;
				
				if(mouseDelta > 0) {
					camera.setScale(camera.getXScale() / mouseDelta, camera.getYScale() / mouseDelta);
					
				} else if(mouseDelta < 0) {
					mouseDelta *= -1;
					camera.setScale(camera.getXScale() * mouseDelta, camera.getYScale() * mouseDelta);
					
				}
				
			} else {
				camera.update();
				
				camera.setScale(1, 1);
				
				final int maxWidth = world.getWidth() - DisplayManager.getScreenWidth();
				final int maxHeight = world.getHeight() - DisplayManager.getScreenHeight();
				
				if(camera.getX() < 0) {
					camera.setX(0);
				} else if(camera.getX() > maxWidth) {
					camera.setX(maxWidth);
				}
				
				if(camera.getY() < 0) {
					camera.setY(0);
				} else if(camera.getY() > maxHeight) {
					camera.setY(maxHeight);
				}
				
			}
			
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
		
		AffineTransform af = g.getTransform();
		
		int translateX = camera.getX();
		int translateY = camera.getY();
		
		g.translate(-translateX, -translateY);
		
		double xScale = camera.getXScale();
		double yScale = camera.getYScale();
		
		g.scale(xScale, yScale);
		
		world.render(g);
		
		g.setTransform(af);
		
	}
	
	public void pauseGame(boolean paused) {
		// Work something out here.
		this.paused = paused;
	}
	
}
