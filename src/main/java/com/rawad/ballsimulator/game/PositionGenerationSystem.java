package com.rawad.ballsimulator.game;

import java.util.Random;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.RandomPositionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

public class PositionGenerationSystem extends GameSystem {
	
	private double maxWidth;
	private double maxHeight;
	
	public PositionGenerationSystem(double maxWidth, double maxHeight) {
		super();
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(CollisionComponent.class);
		compatibleComponentTypes.add(RandomPositionComponent.class);
		
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		
	}
	
	@Override
	public void tick(Entity e) {
		
		RandomPositionComponent randomPositionComp = e.getComponent(RandomPositionComponent.class);
		
		if(randomPositionComp.isGenerateNewPosition()) {
			
			TransformComponent transformComp = e.getComponent(TransformComponent.class);
			CollisionComponent collisionComp = e.getComponent(CollisionComponent.class); 
			
			boolean entityBlocked = true;
			
			Rectangle hitbox = collisionComp.getHitbox();
			
			Random r = new Random();
			
			do {
				
				double x = r.nextDouble() * (maxWidth - hitbox.getWidth()) - hitbox.getX();
				double y = r.nextDouble() * (maxHeight - hitbox.getHeight()) - hitbox.getY();
				
				transformComp.setX(x);
				transformComp.setY(y);
				
				if(gameEngine.getGameSystem(CollisionSystem.class).checkEntityCollision(e, hitbox) == null) {
					entityBlocked = false;
				}
				
			} while(entityBlocked);
			
			randomPositionComp.setGenerateNewPosition(false);
			
		}
		
	}
	
}
