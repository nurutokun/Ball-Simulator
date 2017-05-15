package com.rawad.ballsimulator.game.event;

import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;

public class EntityCollisionEvent extends Event {
	
	private final Entity entity;
	
	private final Entity collidingWithX;
	private final Entity collidingWithY;
	
	public EntityCollisionEvent(Entity entity, Entity collidingWithX, Entity collidingWithY) {
		super(EventType.COLLISION_ENTITY);
		
		this.entity = entity;
		
		this.collidingWithX = collidingWithX;
		this.collidingWithY = collidingWithY;
		
	}

	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * @return the collidingWithX
	 */
	public Entity getCollidingWithX() {
		return collidingWithX;
	}
	
	/**
	 * @return the collidingWithY
	 */
	public Entity getCollidingWithY() {
		return collidingWithY;
	}
	
}
