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
		
		collisionComp.setOutOfBoundsUp(isOutOfBoundsUp(collisionComp, bounds));
		collisionComp.setOutOfBoundsDown(isOutOfBoundsDown(collisionComp, bounds));
		collisionComp.setOutOfBoundsRight(isOutOfBoundsRight(collisionComp, bounds));
		collisionComp.setOutOfBoundsLeft(isOutOfBoundsLeft(collisionComp, bounds));
		
		if(collisionComp.isOutOfBoundsUp() || collisionComp.isOutOfBoundsDown() || collisionComp.isOutOfBoundsRight() ||
				collisionComp.isOutOfBoundsLeft()) {
			transformComp.setX(collisionComp.getHitbox().getX());
			transformComp.setY(collisionComp.getHitbox().getY());
		}
		
		checkEntityCollision(e, transformComp, collisionComp);
		
	}
	
	/**
	 * Directly updates {@code collisionComp} with the necessary data about collision with another {@code Entity}.
	 * 
	 * @param compatibleEntities
	 * @param currentEntity
	 * @param collisionComp
	 * @return Whether the {@code currentEntity} is intersecting with another {@code Entity} from {@code compatibleEntities}.
	 */
	public boolean checkEntityCollision(Entity currentEntity, TransformComponent transformComp, 
			CollisionComponent collisionComp) {
		
		Rectangle hitbox = collisionComp.getHitbox();
		Rectangle newHitbox = new Rectangle(transformComp.getX(), transformComp.getY(), hitbox.getWidth(), 
				hitbox.getHeight());
		
		for(Entity e: compatibleEntities) {
			
			if(currentEntity.equals(e)) continue;
			
			CollisionComponent otherCollisionComp = e.getComponent(CollisionComponent.class);
			Rectangle otherHitbox = otherCollisionComp.getHitbox();
			
			boolean collidingUp = true;
			boolean collidingDown = true;
			boolean collidingRight = true;
			boolean collidingLeft = true;
			
			if(otherHitbox.getY() + otherHitbox.getHeight() < newHitbox.getY()) collidingUp = false;
			if(newHitbox.getY() + newHitbox.getHeight() < otherHitbox.getY()) collidingDown = false;
			
			if(newHitbox.getX() + newHitbox.getWidth() < otherHitbox.getX()) collidingRight = false;
			if(otherHitbox.getX() + otherHitbox.getWidth() < newHitbox.getX()) collidingLeft = false;
			
			boolean colliding = true;
			
			if(		newHitbox.getX() + newHitbox.getWidth() < otherHitbox.getX() || 
					newHitbox.getY() + newHitbox.getHeight() < otherHitbox.getY() || 
					otherHitbox.getX() + otherHitbox.getWidth() < newHitbox.getX() ||
					otherHitbox.getY() + otherHitbox.getHeight() < newHitbox.getY()) {
				colliding = false;
			}
			
			
			if(colliding) {
				collisionComp.getCollidingWithEntity().set(e);
				
				collisionComp.setCollidingUp(collidingUp);
				collisionComp.setCollidingDown(collidingDown);
				collisionComp.setCollidingRight(collidingRight);
				collisionComp.setCollidingLeft(collidingLeft);
				
				transformComp.setX(hitbox.getX());
				transformComp.setY(hitbox.getY());
				
				return true;
			}
			
		}
		
		collisionComp.getCollidingWithEntity().set(null);
		
		collisionComp.setCollidingUp(false);
		collisionComp.setCollidingDown(false);
		collisionComp.setCollidingRight(false);
		collisionComp.setCollidingLeft(false);
		
		hitbox.setX(transformComp.getX());
		hitbox.setY(transformComp.getY());
		
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
