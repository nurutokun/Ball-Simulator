package com.rawad.ballsimulator.client.renderengine.world;

import java.util.ArrayList;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.renderengine.entity.PlayerRender;
import com.rawad.ballsimulator.client.renderengine.world.terrain.TerrainComponentRender;
import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.world.World;
import com.rawad.ballsimulator.world.terrain.Terrain;
import com.rawad.ballsimulator.world.terrain.TerrainComponent;
import com.rawad.gamehelpers.renderengine.LayeredRender;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class WorldRender extends LayeredRender {
	
	private PlayerRender playerRender;
	
	private TerrainComponentRender tcRender;
	
	private World world;
	private Camera camera;
	
	public WorldRender() {
		
		playerRender = new PlayerRender();
		
		tcRender = new TerrainComponentRender();
		
	}
	
	@Override
	public void render(GraphicsContext g) {
		
		if(world == null || camera == null) return;
		
		Affine af = g.getTransform();
		
		g.scale(camera.getScaleX(), camera.getScaleY());
		
		g.rotate(camera.getRotation());
		
		g.translate(-camera.getX(), -camera.getY());
		
		Terrain terrain = world.getTerrain();
		
		g.setFill(Color.GRAY);
		g.fillRect(0, 0, world.getWidth(), world.getHeight());
		
		ArrayList<TerrainComponent> components = terrain.getTerrainComponents();
		
		for(TerrainComponent comp: components) {
			
			tcRender.addComponent(comp);
			
		}
		
		ArrayList<Entity> entities = world.getEntities();
		
		for(Entity e: entities) {
			
			if(e instanceof EntityPlayer) {
				playerRender.addPlayer((EntityPlayer) e);
			}
			
		}
		
		tcRender.render(g);
		playerRender.render(g);
		
		g.setTransform(af);
		
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public TerrainComponentRender getTerrainComponentRender() {
		return tcRender;
	}
	
}