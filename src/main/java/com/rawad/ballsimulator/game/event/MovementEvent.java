package com.rawad.ballsimulator.game.event;

import com.rawad.ballsimulator.game.MovementRequest;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;

public class MovementEvent extends Event {
	
	private final Entity entityToMove;
	private final MovementRequest movementRequest;
	
	public MovementEvent(Entity entityToMove, MovementRequest movementRequest) {
		super(EventType.MOVEMENT);
		
		this.entityToMove = entityToMove;
		this.movementRequest = movementRequest;
		
	}
	
	/**
	 * @return the entityToMove
	 */
	public Entity getEntityToMove() {
		return entityToMove;
	}
	
	/**
	 * @return the movementRequest
	 */
	public MovementRequest getMovementRequest() {
		return movementRequest;
	}
	
}
