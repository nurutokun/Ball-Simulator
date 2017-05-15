package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.geometry.Rectangle;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Point2d;
import com.rawad.jfxengine.client.input.Mouse;

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
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
		SelectionComponent selectionComp = e.getComponent(SelectionComponent.class);
		
		double mouseX = Mouse.isClamped()? Mouse.getClampX():Mouse.getX();
		double mouseY = Mouse.isClamped()? Mouse.getClampY():Mouse.getY();
		
		Point2d mouseInWorld = EntitySelectionSystem.transformFromScreen(cameraTransform, mouseX, mouseY);
		
		Rectangle hitbox = CollisionSystem.getHitboxInTransform(transformComp, collisionComp.getHitbox());
		
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
	 * 
	 * Converts the given {@code x} and {@code y} coordinates for a point on the screen and converts it to a point in the
	 * {@code transformComp}'s space.
	 * 
	 * @param transformComp
	 * @param x
	 * @param y
	 * @return
	 */
	public static Point2d transformFromScreen(TransformComponent transformComp, double x, double y) {
		
		Point2d pointInWorld = new Point2d(x, y);
		
		pointInWorld.setX(x / transformComp.getScaleX() + transformComp.getX());
		pointInWorld.setY(y /  transformComp.getScaleY() + transformComp.getY());
		
		return pointInWorld;
		
	}
	
	/**
	 * Requests to this {@code GameSystem} to select the currently-highlighted {@code Entity}. If there is no highlighted 
	 * {@code Entity}, nothing happens.
	 */
	public void requestSelect() {
		this.selectRequested = true;
	}
	
}
