package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.MovingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;


public class PlayerControlSystem extends GameSystem {
	
	public PlayerControlSystem() {// PlayerControlSystem? ClientControlSystem? <- Find way of implementing + components because
		// input is more than just player control.
		super();
		
		compatibleComponentTypes.add(UserControlComponent.class);
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(MovingComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		MovingComponent movingComp = e.getComponent(MovingComponent.class);
		
	}
	
}
