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
		
		CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
		SelectionComponent selectionComp = e.getComponent(SelectionComponent.class);
		
		double mouseX = Mouse.isClamped()? Mouse.getClampX():Mouse.getX();
		double mouseY = Mouse.isClamped()? Mouse.getClampY():Mouse.getY();
		
		Point mouseInWorld = cameraTransform.transformFromScreen(mouseX, mouseY);
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		if(hitbox.contains(mouseInWorld)) {
			
			selectionComp.setHighlighted(true);
			
			if(selectRequested) {
				selectionComp.setSelected(true);
				selectRequested = false;
			} else {
				selectionComp.setSelected(false);
			}
			
		} else {
			selectionComp.setHighlighted(false);
		}
		
	}
	
	/**
	 * Requests to this {@code GameSystem} to select the currently-highlighted {@code Entity}. If there is no highlighted 
	 * {@code Entity}, nothing happens.
	 */
	public void requestSelect() {
		this.selectRequested = true;
	}
	
}
