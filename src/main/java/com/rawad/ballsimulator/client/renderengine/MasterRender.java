package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.game.RenderingSystem;
import com.rawad.gamehelpers.client.renderengine.AMasterRender;
import com.rawad.gamehelpers.client.renderengine.Camera;

import javafx.scene.canvas.GraphicsContext;

/**
 * Represents the main render that handles all rendering of this game.
 * 
 * @author Rawad
 *
 */
public class MasterRender extends AMasterRender {
	
	private RenderingSystem renderingSystem;
	
	private BackgroundRender backgroundRender;
	private TerrainRender terrainRender;
	private EntityRender entityRender;
	private DebugRender debugRender;
	
	public MasterRender() {
		super();
		
		backgroundRender = new BackgroundRender();
		terrainRender = new TerrainRender();
		entityRender = new EntityRender();
		debugRender = new DebugRender();
		
		registerRender(terrainRender);
		registerRender(entityRender);
		
	}
	
	public void render(GraphicsContext g, Camera camera) {
		
		backgroundRender.render(g, camera.getCameraBounds().getWidth(), camera.getCameraBounds().getHeight());
		
		if(renderingSystem != null) {
			entityRender.setEntities(renderingSystem.getCompatibleEntities());
			
			super.render(g, camera);
		}
		
		debugRender.render(g, camera);
		
	}
	
	public void setRenderingSystem(RenderingSystem renderingSystem) {
		this.renderingSystem = renderingSystem;
	}
	
	public DebugRender getDebugRender() {
		return debugRender;
	}
	
}
