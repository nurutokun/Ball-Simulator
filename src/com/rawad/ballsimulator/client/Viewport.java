package com.rawad.ballsimulator.client;

import java.awt.Graphics;

import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.world.WorldRender;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.input.Mouse;
import com.rawad.gamehelpers.renderengine.MasterRender;

public class Viewport {
	
	private MasterRender masterRender;
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	private World world;
	
	private Camera camera;
	
	public Viewport(MasterRender masterRender, World world) {
		
		this.world = world;
		
		this.masterRender = masterRender;
		
		worldRender = masterRender.getRender(WorldRender.class);
		debugRender = masterRender.getRender(DebugRender.class);
		
		camera = new Camera();
		
	}
	
	public void update(long timePassed, double x, double y) {
		
//		camera.setScale(1/2d, 1/2d);
		camera.setScale((double) Game.SCREEN_WIDTH / (double) world.getWidth(), 
				(double) Game.SCREEN_HEIGHT / (double) world.getHeight());
		
		camera.update(x, y, world.getWidth(), world.getHeight(), 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		
		worldRender.setWorld(world);
		worldRender.setCamera(camera);
		debugRender.setCamera(camera);
		
	}
	
	public void update(long timePassed, int dx, int dy) {
		
		camera.update(camera.getX() + dx, camera.getY() + dy, world.getHeight(), world.getWidth(), 0, 0, 
				Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		
		double mouseDelta = (double) Mouse.getMouseWheelPosition()/2d;
		
		if(mouseDelta > 0) {
			camera.setScale(camera.getXScale() / mouseDelta, camera.getYScale() / mouseDelta);
			
		} else if(mouseDelta < 0) {
			mouseDelta *= -1;
			camera.setScale(camera.getXScale() * mouseDelta, camera.getYScale() * mouseDelta);
			
		}
		
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
	
	public void render(Graphics g, int width, int height) {
		
		masterRender.render();
		
		g.drawImage(masterRender.getBuffer(), 0, 0, width, height, null);
		
	}
	
	public MasterRender getMasterRender() {
		return masterRender;
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
