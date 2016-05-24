package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Point;
import com.rawad.gamehelpers.geometry.Rectangle;

public class EntitySelectionSystem extends GameSystem {
	
	private TransformComponent cameraTransform;
	
	private boolean selectRequested;
	
	public EntitySelectionSystem(TransformComponent cameraTransform) {
		super();
		
		this.cameraTransform = cameraTransform;
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(CollisionComponent.class);
		compatibleComponentTypes.add(SelectionComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		SelectionComponent selectionComp = e.getComponent(SelectionComponent.class);
		
		if(!selectRequested) {
			selectionComp.setSelected(false);
			return;
		}
		
		CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
		
		Point mouseInWorld = cameraTransform.transformFromScreen(Mouse.getX(), Mouse.getY());
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		if(hitbox.contains(mouseInWorld)) {
			selectionComp.setSelected(true);
			selectRequested = false;
		}
		
	}
	
	/**
	 * Requests to this {@code GameSystem} to select the currently-hovered {@code Entity}. If there is no hovered 
	 * {@code Entity}, nothing happens.
	 */
	public void requestSelect() {
		this.selectRequested = true;
	}
	
}
