package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.renderengine.LayerRender;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.world.World;

import javafx.scene.canvas.GraphicsContext;

public class WorldRender extends LayerRender {
	
	private World world;
	
	private TransformComponent cameraTransform;
	
	private TerrainRender terrainRender;
	private EntityRender entityRender;
	
	public WorldRender(World world, Entity camera) {
		
		this.world = world;
		
		cameraTransform = camera.getComponent(TransformComponent.class);
		
		terrainRender = new TerrainRender();
		entityRender = new EntityRender();
		
	}
	
	@Override
	public void render(GraphicsContext g) {
		
//		g.scale(cameraTransform.getScaleX(), cameraTransform.getScaleY());
		
//		g.rotate(cameraTransform.getTheta());
		
//		g.translate(-cameraTransform.getX(), -cameraTransform.getY());
		
		terrainRender.render(g, world.getWidth(), world.getHeight());
		
		for(Entity e: world.getObservableEntities()) {
			if(e.getComponent(RenderingComponent.class) == null) continue;
			
			entityRender.render(g, e);
		}
		
	}
	
}
