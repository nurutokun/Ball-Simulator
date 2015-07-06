package com.rawad.ballsimulator.client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.displaymanager.DisplayManager;
import com.rawad.gamehelpers.input.MouseInput;

public class Viewport {
	
	private World world;
	
	private Camera camera;
	
	private boolean lockCameraToPlayer;
	
	public Viewport(World world, EntityPlayer player) {
		
		this.world = world;
		
		camera = new Camera(player);
		
		lockCameraToPlayer = (player != null);
		
	}
	
	/**
	 * This constructor should be used when not wanting to lock to a specific player.
	 * 
	 * @param world
	 */
	public Viewport(World world) {
		this(world, null);
	}
	
	public void update(long timePassed, int dx, int dy) {
		
		// Check for server connectivity otherwise, perform game logic and update World
		
		if(lockCameraToPlayer) {
			
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
			
		} else {
			
			camera.update(camera.getX() + dx, camera.getY() + dy, 0, 0);
			
			double mouseDelta = MouseInput.getMouseWheelPosition()/-10d;
			
			if(mouseDelta > 0) {
				camera.setScale(camera.getXScale() / mouseDelta, camera.getYScale() / mouseDelta);
				
			} else if(mouseDelta < 0) {
				mouseDelta *= -1;
				camera.setScale(camera.getXScale() * mouseDelta, camera.getYScale() * mouseDelta);
				
			}
			
		}
		
	}
	
	public void update(long timePassed) {
		
		int dx = 0;
		int dy = 0;
		
		if(MouseInput.isClamped()) {
			dx = MouseInput.getX();
			dy = MouseInput.getY();
		}
		
		this.update(timePassed, dx, dy);
	}
	
	public void handleKeyboardInput(boolean rotRight, boolean rotLeft, boolean rotReset) {
		
		if(rotRight) {
			camera.increaseRotation(0.01d);
		} else if(rotLeft) {
			camera.increaseRotation(-0.01d);
		} else if(rotReset) {
			camera.setTheta(0d);
		}
		
	}
	
	public void render(Graphics2D g) {
		
		AffineTransform af = g.getTransform();
		
		int translateX = (int) camera.getX();
		int translateY = (int) camera.getY();
		
		if(camera.getTrackedEntity() != null) {
			g.rotate(camera.getTheta(), camera.getTrackedEntity().getX() + translateX, camera.getTrackedEntity().getY() + translateY);
		}
		
		g.translate(-translateX, -translateY);
		
		double xScale = camera.getXScale();
		double yScale = camera.getYScale();
		
		g.scale(xScale, yScale);
		
		world.render(g);
		
		g.setTransform(af);
		
		g.setColor(Color.RED);
		Thread[] tarray = new Thread[Thread.activeCount()];
		Thread.enumerate(tarray);
		
		g.drawString("Active Threads: ", 10, 10);
		
		for(int i = 0; i < tarray.length; i++) {
			
			if(tarray[i] != null) {
				g.drawString(tarray[i].getName(), 10, (i*10) + 20);
			}
			
		}
		
	}
	
	public void loadTerrain() {
		world.setTerrain(Loader.loadTerrain("terrain"));
	}
	
	public void setCameraLocked(boolean lockCameraToPlayer) {
		this.lockCameraToPlayer = lockCameraToPlayer;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public World getWorld() {
		return world;
	}
	
}
