package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.RollingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;

public class RollingSystem extends GameSystem {
	
	public RollingSystem() {
		super();
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(MovementComponent.class);
		compatibleComponentTypes.add(RollingComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		MovementComponent movementComp = e.getComponent(MovementComponent.class);
		
		transformComp.setTheta(transformComp.getTheta() + (movementComp.getVx() / 2));
		
	}
	
}
