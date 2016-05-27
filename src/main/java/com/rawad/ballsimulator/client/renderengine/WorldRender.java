package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.client.renderengine.LayerRender;
import com.rawad.gamehelpers.client.renderengine.MasterRender;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

public class WorldRender extends LayerRender {
	
	private World world;
	
	private TransformComponent cameraTransform;
	private UserViewComponent userView;
	
	private TerrainRender terrainRender;
	private EntityRender entityRender;
	
	public WorldRender(World world, Entity camera) {
		
		this.world = world;
		
		cameraTransform = camera.getComponent(TransformComponent.class);
		userView = camera.getComponent(UserViewComponent.class);
		
		terrainRender = new TerrainRender();
		entityRender = new EntityRender();
		
	}
	
	@Override
	public void render(GraphicsContext g) {
		
		Rectangle viewport = userView.getViewport();
		
		g.setFill(MasterRender.DEFAULT_BACKGROUND_COLOR);
		g.fillRect(0, 0, viewport.getWidth(), viewport.getHeight());
		
		g.scale(cameraTransform.getScaleX(), cameraTransform.getScaleY());
		
		g.rotate(cameraTransform.getTheta());
		
		g.translate(-cameraTransform.getX(), -cameraTransform.getY());
		
//		g.scale(viewport.getWidth() / world.getWidth(), viewport.getHeight() / world.getHeight());
		
		Affine affine = g.getTransform();
		
		terrainRender.render(g, world.getWidth(), world.getHeight());
		
		for(Entity e: world.getEntitiesAsList()) {
			if(e.getComponent(RenderingComponent.class) == null) continue;
			
			entityRender.render(g, e);
			g.setTransform(affine);
			
		}
			
//		g.setStroke(Color.BLACK);// Optional rendering, with the g.scale() earlier to show viewport in world.
//		g.strokeRect(cameraTransform.getX(), cameraTransform.getY(), viewport.getWidth() / cameraTransform.getScaleX(), 
//				viewport.getHeight() / cameraTransform.getScaleY());
		
	}
	
	/**
	 * For customizing component rendering.
	 * 
	 * @see EntityRender#getComponentRenders()
	 * 
	 * @return
	 */
	public EntityRender getEntityRender() {
		return entityRender;
	}
	
}
