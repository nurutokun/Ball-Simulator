package com.rawad.ballsimulator.client.renderengine;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.geometry.Rectangle;
import com.rawad.gamehelpers.client.renderengine.LayerRender;
import com.rawad.gamehelpers.game.World;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class WorldRender extends LayerRender {
	
	// Has to be 0.0 - 1.0 .
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.DARKGRAY;//new Color(202, 212, 227, 25);
	
	private World world;
	
	private LinkedHashMap<Object, ArrayList<Entity>> entities;
	
	private TransformComponent cameraTransform;
	private UserViewComponent userView;
	
	private TerrainRender terrainRender;
	private EntityRender entityRender;
	
	public WorldRender(World world, Entity camera) {
		
		this.world = world;
		
		entities = new LinkedHashMap<Object, ArrayList<Entity>>();
		
		cameraTransform = camera.getComponent(TransformComponent.class);
		userView = camera.getComponent(UserViewComponent.class);
		
		terrainRender = new TerrainRender();
		entityRender = new EntityRender();
		
	}
	
	@Override
	public void render() {
		
		GraphicsContext g = GuiRegister.getRoot(masterRender.getState()).getCanvas().getGraphicsContext2D();
		
		Affine transform = g.getTransform();
		
		Rectangle viewport = userView.getViewport();
		
		g.setFill(DEFAULT_BACKGROUND_COLOR);
		g.fillRect(0, 0, viewport.getWidth(), viewport.getHeight());
		
		g.scale(cameraTransform.getScaleX(), cameraTransform.getScaleY());
		g.rotate(cameraTransform.getTheta());
		g.translate(-cameraTransform.getX(), -cameraTransform.getY());
		
//		g.scale(viewport.getWidth() / world.getWidth(), viewport.getHeight() / world.getHeight());
		
		Affine modTransform = g.getTransform();
		
		terrainRender.render(g, world.getWidth(), world.getHeight());
		
		for(Object batchKey: entities.keySet()) {
			
			ArrayList<Entity> batch = entities.get(batchKey);
			
			for(Entity e: batch) {
				
				entityRender.render(g, e);
				g.setTransform(modTransform);
				
			}
			
		}
		
//		g.setStroke(Color.BLACK);// Optional rendering, with the g.scale() earlier to show viewport in world.
//		g.strokeRect(cameraTransform.getX(), cameraTransform.getY(), viewport.getWidth() / cameraTransform.getScaleX(), 
//				viewport.getHeight() / cameraTransform.getScaleY());
		
		g.setTransform(transform);
		
	}
	
	public void setEntities(LinkedHashMap<Object, ArrayList<Entity>> entities) {
		this.entities = entities;
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
	
	/**
	 * Used by the {@link com.rawad.ballsimulator.game.RenderingSystem} to determine which entities are in view.
	 * 
	 * @return
	 */
	public UserViewComponent getUserView() {
		return userView;
	}
	
	/**
	 * Used by the {@link com.rawad.ballsimulator.game.RenderingSystem} to determine which entities are in view.
	 * 
	 * @return
	 */
	public TransformComponent getCameraTransform() {
		return cameraTransform;
	}
	
}
