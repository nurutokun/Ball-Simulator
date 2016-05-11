package com.rawad.ballsimulator.game;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;

public class RenderingSystem extends GameSystem {
	
	/** Remaining entities with no additional componentss that effect rendering. */
	private ArrayList<Entity> staticEntities;
	
	public RenderingSystem() {
		super();
		
		staticEntities = new ArrayList<Entity>();
		
		compatibleComponentTypes.add(RenderingComponent.class);
		compatibleComponentTypes.add(TransformComponent.class);
		
	}
	
	@Override
	public void tick() {
		
		staticEntities = new ArrayList<Entity>();
		
		super.tick();
		
	}
	
	@Override
	public void tick(Entity e) {
		
		staticEntities.add(e);
		
	}
	
	public ArrayList<Entity> getStaticEntities() {
		return new ArrayList<Entity>(staticEntities);
	}
	
}
