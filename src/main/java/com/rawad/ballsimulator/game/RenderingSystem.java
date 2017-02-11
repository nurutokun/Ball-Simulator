package com.rawad.ballsimulator.game;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.geometry.Rectangle;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;


public class RenderingSystem extends GameSystem {
	
	private WorldRender worldRender;
	
	private UserViewComponent userView;
	
	private LinkedHashMap<Object, ArrayList<Entity>> entities;
	
	public RenderingSystem(WorldRender worldRender) {
		super();
		
		this.worldRender = worldRender;
		
		this.userView = worldRender.getUserView();
		
		entities = new LinkedHashMap<Object, ArrayList<Entity>>();
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(RenderingComponent.class);
		
	}
	
	@Override
	public void tick() {
		
		for(ArrayList<Entity> batch: entities.values()) {
			batch.clear();// Think of removing batches some way, when entities don't occupy it after some time.
		}
		
		super.tick();
		
		worldRender.setEntities(entities);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
		
		Rectangle boundingBox = new Rectangle(0, 0, renderingComp.getTexture().getWidth(), 
				renderingComp.getTexture().getHeight());
		boundingBox.setX(-boundingBox.getWidth() / 2d);
		boundingBox.setY(-boundingBox.getHeight() / 2d);
		
		Rectangle viewport = userView.getViewport();
		TransformComponent cameraTransform = worldRender.getCameraTransform();
		
		Rectangle scaledUserView = new Rectangle(viewport.getX(), viewport.getY(), viewport.getWidth() / 
				cameraTransform.getScaleX(), viewport.getHeight() / cameraTransform.getScaleY());
		
		if(!CollisionSystem.isInBounds(transformComp, boundingBox, scaledUserView)) return;
		
		ArrayList<Entity> batch = entities.get(renderingComp.getTexture());
		
		if(batch == null) {
			batch = new ArrayList<Entity>();
			entities.put(renderingComp.getTexture(), batch);
		}
		
		batch.add(e);
		
	}
	
}
