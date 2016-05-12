package com.rawad.ballsimulator.game;

import java.util.ArrayList;

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
		
		CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
		
		collisionComp.setCollidingUp(isCollidingUp(collisionComp, bounds));
		collisionComp.setCollidingDown(isCollidingDown(collisionComp, bounds));
		collisionComp.setCollidingRight(isCollidingRight(collisionComp, bounds));
		collisionComp.setCollidingLeft(isCollidingLeft(collisionComp, bounds));
		
		checkEntityCollision(compatibleEntities, e, collisionComp);
		
	}
	
	/**
	 * Directly updates {@code collisionComp} with the necessary data about collision with another {@code Entity}.
	 * 
	 * @param compatibleEntities
	 * @param currentEntity
	 * @param collisionComp
	 * @return Whether the {@code currentEntity} is intersecting with another {@code Entity} from {@code compatibleEntities}.
	 */
	public static boolean checkEntityCollision(ArrayList<Entity> compatibleEntities, Entity currentEntity, 
			CollisionComponent collisionComp) {
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		for(Entity e: compatibleEntities) {
			
			if(currentEntity.equals(e)) continue;
			
			CollisionComponent otherCollisionComp = e.getComponent(CollisionComponent.class);
			Rectangle otherHitbox = otherCollisionComp.getHitbox();
			
			boolean collidingUp = true;
			boolean collidingDown = true;
			boolean collidingRight = true;
			boolean collidingLeft = true;
			
			if(otherHitbox.getY() + otherHitbox.getHeight() < hitbox.getY()) collidingUp = false;
			if(hitbox.getY() + hitbox.getHeight() < otherHitbox.getY()) collidingDown = false;
			
			if(hitbox.getX() + hitbox.getWidth() < otherHitbox.getX()) collidingRight = false;
			if(otherHitbox.getX() + otherHitbox.getWidth() < hitbox.getX()) collidingLeft = false;
			
			collisionComp.setCollidingUp(collidingUp);
			collisionComp.setCollidingDown(collidingDown);
			collisionComp.setCollidingRight(collidingRight);
			collisionComp.setCollidingLeft(collidingLeft);
			
			if(collidingUp || collidingDown || collidingRight || collidingLeft) {
				collisionComp.getCollidingWithEntity().set(e);
				return true;
			}
			
		}
		
		return false;
		
	}
	
	public static boolean isCollidingUp(CollisionComponent collisionComp, Rectangle bounds) {

		Rectangle hitbox = collisionComp.getHitbox();
		
		if(hitbox.getY() < bounds.getY()) return true;
		
		return false;
		
	}
	
	public static boolean isCollidingDown(CollisionComponent collisionComp, Rectangle bounds) {

		Rectangle hitbox = collisionComp.getHitbox();
		
		if(hitbox.getY() + hitbox.getHeight() > bounds.getY() + bounds.getHeight()) return true;
		
		return false;
		
	}
	
	public static boolean isCollidingRight(CollisionComponent collisionComp, Rectangle bounds) {
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		if(hitbox.getX() + hitbox.getWidth() > bounds.getX() + bounds.getWidth()) return true;
		
		return false;
		
	}
	
	public static boolean isCollidingLeft(CollisionComponent collisionComp, Rectangle bounds) {
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		if(hitbox.getY() < bounds.getY()) return true;
		
		return false;
		
	}
	
}
