package com.rawad.ballsimulator.client.renderengine.world;

import java.util.ArrayList;

import com.rawad.ballsimulator.client.renderengine.entity.PlayerRender;
import com.rawad.ballsimulator.client.renderengine.world.terrain.TerrainComponentRender;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.game.RenderingSystem;
import com.rawad.ballsimulator.terrain.Terrain;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.client.renderengine.Camera;
import com.rawad.gamehelpers.client.renderengine.LayeredRender;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class WorldRender extends LayeredRender {
	
	private RenderingSystem renderingSystem;
	
	private PlayerRender playerRender;
	
	private TerrainComponentRender tcRender;
	
	private World world;
	private Camera camera;
	
	public WorldRender(RenderingSystem renderingSystem) {
		
		this.renderingSystem = renderingSystem;
		
		playerRender = new PlayerRender();
		
		tcRender = new TerrainComponentRender();
		
	}
	
	@Override
	public void render(GraphicsContext g, Entity e) {
		
		if(world == null || camera == null) return;
		
		Affine af = g.getTransform();
		
		g.scale(camera.getScaleX(), camera.getScaleY());
		
		g.rotate(camera.getRotation());
		
		g.translate(-camera.getX(), -camera.getY());
		
		Terrain terrain = world.getTerrain();
		
		g.setFill(Color.GRAY);
		g.fillRect(0, 0, world.getWidth(), world.getHeight());
		
		ArrayList<Entity> staticEntities = renderingSystem.getStaticEntities();
		
		for(Entity staticEntity: staticEntities) {
			
			tcRender.addComponent(staticEntity);
			
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
