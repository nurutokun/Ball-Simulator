package com.rawad.ballsimulator.client;

import java.awt.Graphics;

import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.world.WorldRender;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.renderengine.MasterRender;

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
	
	public void render() {
		masterRender.render();
	}
	
	public void drawScene(Graphics g, int width, int height) {
		
		g.drawImage(masterRender.getBuffer(), 0, 0, width, height, null);
		
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
