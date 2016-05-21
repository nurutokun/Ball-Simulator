package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.entity.Listener;
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
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		if(transformComp.getX() == hitbox.getX() && transformComp.getY() == hitbox.getY()) return;// Entity hasn't moved.
		
		Rectangle hitboxX = new Rectangle(transformComp.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		
		Entity collidingWithX = checkEntityCollision(e, hitboxX);
		
		boolean collideX = isOutOfBounds(hitboxX, bounds) || collidingWithX != null;
		
		Rectangle hitboxY = new Rectangle(hitbox.getX(), transformComp.getY(), hitbox.getWidth(), hitbox.getHeight());
		
		Entity collidingWithY = checkEntityCollision(e, hitboxY);
		
		boolean collideY = isOutOfBounds(hitboxY, bounds) || collidingWithY != null;
		
		collisionComp.getCollidingWithEntity().set(collidingWithX == collidingWithY? collidingWithX:collidingWithY);
		// TODO: Which entity gets collided with? Horizontal or vertical? Maybe both?
		
		collisionComp.setCollideX(collideX);
		collisionComp.setCollideY(collideY);
		
		for(Listener<CollisionComponent> listener: collisionComp.getListeners()) {
			listener.onEvent(e, collisionComp);
		}
		
		hitbox.setX(transformComp.getX());
		hitbox.setY(transformComp.getY());
		
	}
	
	/**
	 * Directly updates {@code collisionComp} with the necessary data about collision with another {@code Entity}.
	 * 
	 * @param currentEntity
	 * @param hitbox
	 * @return {@code Entity} colliding with the {@code currentEntity} from {@code compatibleEntities}.
	 */
	public Entity checkEntityCollision(Entity currentEntity, Rectangle hitbox) {
		
		for(Entity e: compatibleEntities) {
			
			if(currentEntity.equals(e)) continue;
			
			Rectangle otherHitbox = e.getComponent(CollisionComponent.class).getHitbox();
			
			boolean collision = true;
			
			if(		hitbox.getX() + hitbox.getWidth() < otherHitbox.getX() || 
					otherHitbox.getX() + otherHitbox.getWidth() < hitbox.getX() || 
					otherHitbox.getY() + otherHitbox.getHeight() < hitbox.getY() || 
					hitbox.getY() + hitbox.getHeight() < otherHitbox.getY())
				collision = false;
			
			if(collision) {
				return e;
			}
			
		}
		
		return null;
		
	}
	
	public static boolean isOutOfBounds(Rectangle hitbox, Rectangle bounds) {
		
		if(hitbox.getX() + hitbox.getWidth() > bounds.getX() + bounds.getWidth() || hitbox.getX() < bounds.getX() ||
				hitbox.getY() < bounds.getY() || hitbox.getY() + hitbox.getHeight() > bounds.getY() + bounds.getHeight()) 
			return true;
		
		return false;
		
	}
	
}
