package com.rawad.ballsimulator.game;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.entity.IListener;
import com.rawad.gamehelpers.geometry.Point;
import com.rawad.gamehelpers.geometry.Rectangle;
import com.rawad.gamehelpers.utils.Util;

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
		
		MovementComponent movementComp = e.getComponent(MovementComponent.class);
		
		if(movementComp == null) return;
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		Point newPos = new Point(transformComp.getX(), transformComp.getY());
		Point oldPos = new Point(newPos.getX() - movementComp.getVx(), newPos.getY() - movementComp.getVy());
		
		transformComp.setY(oldPos.getY());// Undo y to get moved x.
		
		Entity collidingWithX = checkEntityCollision(e, hitbox);
		
		boolean collideX = isOutOfBounds(transformComp, hitbox, bounds) || collidingWithX != null;
		
		transformComp.setY(newPos.getY());// Redo y.
		transformComp.setX(oldPos.getX());// Undo x to get moved y.
		
		Entity collidingWithY = checkEntityCollision(e, hitbox);
		
		boolean collideY = isOutOfBounds(transformComp, hitbox, bounds) || collidingWithY != null;
		
		transformComp.setX(newPos.getX());// Redo x.
		
		collisionComp.getCollidingWithEntity().set(collidingWithX == collidingWithY? collidingWithX:collidingWithY);
		// TODO: Which entity gets collided with? Horizontal or vertical? Maybe both?
		
		collisionComp.setCollideX(collideX);
		collisionComp.setCollideY(collideY);
		
		for(IListener<CollisionComponent> listener: collisionComp.getListeners()) {
			listener.onEvent(e, collisionComp);
		}
		
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
			
			Rectangle curHitbox = CollisionSystem.getHitboxInTransform(currentEntity
					.getComponent(TransformComponent.class), hitbox);
			
			Rectangle otherHitbox = CollisionSystem.getHitboxInTransform(e.getComponent(TransformComponent.class),
					e.getComponent(CollisionComponent.class).getHitbox());
			
			boolean collision = true;
			
			if(		curHitbox.getX() + curHitbox.getWidth() < otherHitbox.getX() ||
					curHitbox.getY() + curHitbox.getHeight() < otherHitbox.getY() ||
					otherHitbox.getX() + otherHitbox.getWidth() < curHitbox.getX() ||
					otherHitbox.getY() + otherHitbox.getHeight() < curHitbox.getY())
				collision = false;
			
			if(collision) return e;
			
		}
		
		return null;
		
	}
	
	public static Rectangle getHitboxInTransform(TransformComponent transformComp, Rectangle hitbox) {
		return new Rectangle(hitbox.getX() * transformComp.getScaleX() + transformComp.getX(), hitbox.getY() *
				transformComp.getScaleY() + transformComp.getY(), hitbox.getWidth() * transformComp.getScaleX(),
				hitbox.getHeight() * transformComp.getScaleY());
	}
	
	public static void keepInBounds(Rectangle inside, Rectangle bounds) {
		
		inside.setX(Util.clamp(inside.getX(), bounds.getX(), bounds.getWidth()));
		inside.setY(Util.clamp(inside.getY(), bounds.getY(), bounds.getHeight()));
		
	}
	
	private static boolean isOutOfBounds(TransformComponent hitboxTransform, Rectangle hitbox, Rectangle bounds) {
		
		hitbox = CollisionSystem.getHitboxInTransform(hitboxTransform, hitbox);
		
		if(hitbox.getX() + hitbox.getWidth() > bounds.getX() + bounds.getWidth() || hitbox.getX() < bounds.getX() ||
				hitbox.getY() < bounds.getY() || hitbox.getY() + hitbox.getHeight() > bounds.getY() + bounds.getHeight()) 
			return true;
		
		return false;
		
	}
	
}
