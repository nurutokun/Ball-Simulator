package com.rawad.ballsimulator.client;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.rawad.ballsimulator.client.renderengine.world.WorldRender;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.renderengine.MasterRender;

public class Viewport {
	
	private WorldRender worldRender;
	
	private World world;
	
	private Camera camera;
	
	private boolean lockCameraToPlayer;
	
	public Viewport(MasterRender masterRender, World world, EntityPlayer player) {
		
		this.world = world;
		
		worldRender = (WorldRender) masterRender.getRender(WorldRender.class);
		
		camera = new Camera(player);
		
		lockCameraToPlayer = (player != null);
		
	}
	
	/**
	 * This constructor should be used when not wanting to lock to a specific player.
	 * 
	 * @param world
	 */
	public Viewport(MasterRender masterRender, World world) {
		this(masterRender, world, null);
	}
	
	public void update(long timePassed, int dx, int dy) {
		
		if(lockCameraToPlayer) {
			
			camera.setScale(1d/2d, 1d/2d);
			
			camera.update(world.getWidth(), world.getHeight());
			
		} else {
			
			camera.update(camera.getX() + dx, camera.getY() + dy, world.getHeight(), world.getWidth(), 
					Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
			
			double mouseDelta = (double) MouseInput.getMouseWheelPosition()/2d;
			
			if(mouseDelta > 0) {
				camera.setScale(camera.getXScale() / mouseDelta, camera.getYScale() / mouseDelta);
				
			} else if(mouseDelta < 0) {
				mouseDelta *= -1;
				camera.setScale(camera.getXScale() * mouseDelta, camera.getYScale() * mouseDelta);
				
			}
			
		}
		
		worldRender.setWorld(world);
		worldRender.setCamera(camera);
		
	}
	
	public void update(long timePassed) {
		
		int dx = 0;
		int dy = 0;
		
		if(MouseInput.isClamped()) {
			dx = MouseInput.getX(false);
			dy = MouseInput.getY(false);
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
		
		double translateX = camera.getX();
		double translateY = camera.getY();
		
		if(camera.getTrackedEntity() != null) {
			g.rotate(camera.getTheta(), camera.getTrackedEntity().getX() + translateX, 
					camera.getTrackedEntity().getY() + translateY);
		}
		
		g.translate(-translateX, -translateY);
		
		double xScale = camera.getXScale();
		double yScale = camera.getYScale();
		
		g.scale(xScale, yScale);
		
		world.render(g);
		
		g.setTransform(af);
		
	}
	
	public void setCameraLocked(boolean lockCameraToPlayer) {
		this.lockCameraToPlayer = lockCameraToPlayer;
	}
	
	public boolean isCameraLocked() {
		return lockCameraToPlayer;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
	
}
