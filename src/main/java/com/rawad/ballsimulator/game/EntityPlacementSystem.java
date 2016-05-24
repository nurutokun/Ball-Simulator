package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.PlaceableComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Point;

public class EntityPlacementSystem extends GameSystem {
	
	private TransformComponent cameraTransform;
	
	public EntityPlacementSystem(TransformComponent cameraTransform) {
		super();
		
		this.cameraTransform = cameraTransform;
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(SelectionComponent.class);
		compatibleComponentTypes.add(PlaceableComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		SelectionComponent selectionComp = e.getComponent(SelectionComponent.class);
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		
		if(selectionComp.isSelected()) {
			
			Point mouseInWorld = cameraTransform.transformFromScreen(Mouse.getX(), Mouse.getY());
			
			double x = mouseInWorld.getX();
			double y = mouseInWorld.getY();
			
			RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
			
			if(renderingComp != null) {
				x -= renderingComp.getTexture().getWidth() * transformComp.getScaleX() / 2d;
				y -= renderingComp.getTexture().getHeight() * transformComp.getScaleY() / 2d;
			}
			
			transformComp.setX(x);
			transformComp.setY(y);
			
		}
		
		PlaceableComponent placeableComp = e.getComponent(PlaceableComponent.class);
		
		if(placeableComp.isPlaceRequested()) {
			Entity newEntity = Entity.createEntity(e, EEntity.STATIC);
			/*/
			TransformComponent newEntityTransform = newEntity.getComponent(TransformComponent.class);
			
			if(newEntityTransform != null) {
				transformComp.copyData(newEntityTransform);
			}
			
			RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
			RenderingComponent newEntityRendering = newEntity.getComponent(RenderingComponent.class);
			
			if(renderingComp != null && newEntityRendering != null) {
				renderingComp.copyData(newEntityRendering);
			}/**/
			
			GameManager.instance().getCurrentGame().getWorld().addEntity(newEntity);
			
			placeableComp.setPlaceRequested(false);
			
		}
		
	}
	
}
