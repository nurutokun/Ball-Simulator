package com.rawad.ballsimulator.client;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.rawad.ballsimulator.client.renderengine.world.WorldRender;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
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
		//(WorldRender) GameManager.instance().getRender().getRender(WorldRender.class);
		
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
			
			camera.update();
			
			camera.setScale(1, 1);
			
			final int maxWidth = world.getWidth() - Game.SCREEN_WIDTH;
			final int maxHeight = world.getHeight() - Game.SCREEN_HEIGHT;
			
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
			
			double mouseDelta = MouseInput.getMouseWheelPosition()/2d;
			
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
		
		double translateX = camera.getX();
		double translateY = camera.getY();
		
		if(camera.getTrackedEntity() != null) {
			g.rotate(camera.getTheta(), camera.getTrackedEntity().getX() + translateX, camera.getTrackedEntity().getY() + 
					translateY);
		}
		
		g.translate(-translateX, -translateY);
		
		double xScale = camera.getXScale();
		double yScale = camera.getYScale();
		
		g.scale(xScale, yScale);
		
		world.render(g);
		
		g.setTransform(af);
		
	}
	
	public void loadTerrain(String terrainName) {
		world.setTerrain(Loader.loadTerrain(GameManager.instance().getCurrentGame(), terrainName));
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
	
	public World getWorld() {
		return world;
	}
	
}
