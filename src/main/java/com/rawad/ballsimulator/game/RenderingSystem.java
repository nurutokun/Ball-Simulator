package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;

public class RenderingSystem extends GameSystem {
	
	public RenderingSystem() {
		super();
		
		compatibleComponentTypes.add(RenderingComponent.class);
		compatibleComponentTypes.add(TransformComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
	}
	
}
