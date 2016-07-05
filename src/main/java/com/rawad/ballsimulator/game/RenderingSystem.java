package com.rawad.ballsimulator.game;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.GameTextures;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;


public class RenderingSystem extends GameSystem {
	
	private WorldRender worldRender;
	
	private LinkedHashMap<Integer, ArrayList<Entity>> entities;
	
	public RenderingSystem(WorldRender worldRender) {
		super();
		
		this.worldRender = worldRender;
		
		entities = new LinkedHashMap<Integer, ArrayList<Entity>>();
		
		entities.put(GameTextures.findTexture(EEntity.STATIC), new ArrayList<Entity>());
		entities.put(GameTextures.findTexture(EEntity.PLAYER), new ArrayList<Entity>());
		
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
		
		RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
		
		ArrayList<Entity> batch = entities.get(renderingComp.getTextureLocation());
		
		if(batch == null) {
			batch = new ArrayList<Entity>();
			entities.put(renderingComp.getTextureLocation(), batch);
		}
		
		batch.add(e);
		
	}
	
}
