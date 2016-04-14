package com.rawad.ballsimulator.client;

import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.world.WorldRender;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.renderengine.MasterRender;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Viewport {
	
	private MasterRender masterRender;
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	public Viewport() {
		
		masterRender = new MasterRender();
		
		worldRender = new WorldRender();
		debugRender = new DebugRender();
		
		masterRender.registerRender(worldRender);
		masterRender.registerRender(debugRender);
		
	}
	
	/**
	 * Simply resets the <code>World</code> and <code>Camera</code> objects of this <code>Viewport</code>, including
	 * those in the renders.
	 * 
	 * @param world
	 * @param camera
	 */
	public void update(World world, Camera camera) {
		
		setWorld(world);
		setCamera(camera);
		
	}
	
	public void render(Canvas canvas, double width, double height) {
		
		GraphicsContext g = canvas.getGraphicsContext2D();
		
		double scaleX = canvas.getWidth() / width;
		double scaleY = canvas.getHeight() / height;
		
		masterRender.render(g, scaleX, scaleY);
		
	}
	
	public void render(Canvas canvas) {
		render(canvas, canvas.getWidth(), canvas.getHeight());
	}
	
	public MasterRender getMasterRender() {
		return masterRender;
	}
	
	public void setCamera(Camera camera) {
		
		worldRender.setCamera(camera);
		debugRender.setCamera(camera);
		
	}
	
	public void setWorld(World world) {
		
		worldRender.setWorld(world);
		
	}
	
}
