package com.rawad.ballsimulator.game;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;

public class RenderingSystem extends GameSystem {
	
	private ArrayList<Entity> compatibleEntitiesWrapper;
	
	public RenderingSystem() {
		super();
		
		compatibleEntitiesWrapper = new ArrayList<Entity>();
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(RenderingComponent.class);
		
	}
	
	@Override
	public void tick() {
		
		compatibleEntitiesWrapper = new ArrayList<Entity>();
		compatibleEntitiesWrapper.addAll(compatibleEntities);
		
		super.tick();
	}
	
	@Override
	public void tick(Entity e) {}
	
	@Override
	public ArrayList<Entity> getCompatibleEntities() {
		return compatibleEntitiesWrapper;
		
	}
	
}
