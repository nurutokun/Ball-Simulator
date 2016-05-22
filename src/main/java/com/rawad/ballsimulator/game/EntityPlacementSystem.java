package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.PlaceableComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;

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
			transformComp.setX(Mouse.getX() / cameraTransform.getScaleX() + (cameraTransform.getX()));
			transformComp.setY(Mouse.getY() / cameraTransform.getScaleY() + (cameraTransform.getY()));
		}
		
		PlaceableComponent placeableComp = e.getComponent(PlaceableComponent.class);
		
		if(placeableComp.isPlaceRequested()) {
			Entity newEntity = Entity.createEntity(placeableComp.getToPlace());
			
			TransformComponent newEntityTransform = newEntity.getComponent(TransformComponent.class);
			
			if(newEntityTransform != null) {
				
				newEntityTransform.setX(transformComp.getX());
				newEntityTransform.setY(transformComp.getY());
				
				newEntityTransform.setScaleX(transformComp.getScaleX());
				newEntityTransform.setScaleY(transformComp.getScaleY());
				
				newEntityTransform.setMaxScaleX(transformComp.getMaxScaleX());
				newEntityTransform.setMaxScaleY(transformComp.getMaxScaleY());
				
				newEntityTransform.setTheta(transformComp.getTheta());
				
			}
			
			RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
			RenderingComponent newEntityRendering = newEntity.getComponent(RenderingComponent.class);
			
			if(renderingComp != null && newEntityRendering != null) {
				
				newEntityRendering.setTexture(renderingComp.getTextureLocation());
				
			}
			
			GameManager.instance().getCurrentGame().getWorld().addEntity(newEntity);
			
			placeableComp.setPlaceRequested(false);
			
		}
		
	}
	
}
