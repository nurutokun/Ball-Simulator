package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

public class CollisionSystem extends GameSystem {
	
	private Rectangle bounds;
	
	public CollisionSystem(double maxWidth, double maxHeight) {
		super();
		
		bounds = new Rectangle(0, 0, maxWidth, maxHeight);
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(CollisionComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
		
		boolean oobUp = isOutOfBoundsUp(collisionComp, bounds);
		boolean oobDown = isOutOfBoundsDown(collisionComp, bounds);
		boolean oobRight = isOutOfBoundsRight(collisionComp, bounds);
		boolean oobLeft = isOutOfBoundsLeft(collisionComp, bounds);
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		double x = oobRight ? bounds.getX() + bounds.getWidth() - hitbox.getWidth():oobLeft ? bounds.getX():
			transformComp.getX();
		double y = oobUp ? bounds.getY():oobDown ? bounds.getY() + bounds.getHeight() - hitbox.getHeight():
			transformComp.getY();
		
		transformComp.setX(x);
		transformComp.setY(y);
		
		hitbox.setX(x);
		hitbox.setY(y);
		
		checkEntityCollision(e, collisionComp);
		
	}
	
	/**
	 * Directly updates {@code collisionComp} with the necessary data about collision with another {@code Entity}.
	 * 
	 * @param compatibleEntities
	 * @param currentEntity
	 * @param collisionComp
	 * @return Whether the {@code currentEntity} is intersecting with another {@code Entity} from {@code compatibleEntities}.
	 */
	public boolean checkEntityCollision(Entity currentEntity, CollisionComponent collisionComp) {
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		for(Entity e: compatibleEntities) {
			
			if(currentEntity.equals(e)) continue;
			
			CollisionComponent otherCollisionComp = e.getComponent(CollisionComponent.class);
			Rectangle otherHitbox = otherCollisionComp.getHitbox();
			
			boolean colliding = true;
			
			if(		hitbox.getX() + hitbox.getWidth() < otherHitbox.getX() || 
					hitbox.getY() + hitbox.getHeight() < otherHitbox.getY() || 
					otherHitbox.getX() + otherHitbox.getWidth() < hitbox.getX() ||
					otherHitbox.getY() + otherHitbox.getHeight() < hitbox.getY()) {
				colliding = false;
			}
			
			if(colliding) {
				collisionComp.getCollidingWithEntity().set(e);
				return true;
			}
			
		}
		
		collisionComp.getCollidingWithEntity().set(null);
		
		return false;
		
	}
	
	public static boolean isOutOfBoundsUp(CollisionComponent collisionComp, Rectangle bounds) {

		Rectangle hitbox = collisionComp.getHitbox();
		
		if(hitbox.getY() < bounds.getY()) return true;
		
		return false;
		
	}
	
	public static boolean isOutOfBoundsDown(CollisionComponent collisionComp, Rectangle bounds) {

		Rectangle hitbox = collisionComp.getHitbox();
		
		if(hitbox.getY() + hitbox.getHeight() > bounds.getY() + bounds.getHeight()) return true;
		
		return false;
		
	}
	
	public static boolean isOutOfBoundsRight(CollisionComponent collisionComp, Rectangle bounds) {
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		if(hitbox.getX() + hitbox.getWidth() > bounds.getX() + bounds.getWidth()) return true;
		
		return false;
		
	}
	
	public static boolean isOutOfBoundsLeft(CollisionComponent collisionComp, Rectangle bounds) {
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		if(hitbox.getX() < bounds.getX()) return true;
		
		return false;
		
	}
	
}
