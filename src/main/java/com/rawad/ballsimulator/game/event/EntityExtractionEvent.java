package com.rawad.ballsimulator.game.event;

import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;

public class EntityExtractionEvent extends Event {
	
	private final Entity entityToExtract;
	
	public EntityExtractionEvent(Entity entityToExtract) {
		super(EventType.ENTITY_EXTRACT);
		
		this.entityToExtract = entityToExtract;
		
	}
	
	/**
	 * @return the entityToExtract
	 */
	public Entity getEntityToExtract() {
		return entityToExtract;
	}
	
}
