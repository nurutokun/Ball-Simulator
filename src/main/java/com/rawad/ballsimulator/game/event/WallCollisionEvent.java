package com.rawad.ballsimulator.game.event;

import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;

public class WallCollisionEvent extends Event {
	
	private final Entity entity;
	
	private final boolean collideX;
	private final boolean collideY;
	
	public WallCollisionEvent(Entity entity, boolean collideX, boolean collideY) {
		super(EventType.COLLISION_WALL);
		
		this.entity = entity;
		
		this.collideX = collideX;
		this.collideY = collideY;
		
	}
	
	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * @return the collideX
	 */
	public boolean isCollideX() {
		return collideX;
	}
	
	/**
	 * @return the collideY
	 */
	public boolean isCollideY() {
		return collideY;
	}
	
}
