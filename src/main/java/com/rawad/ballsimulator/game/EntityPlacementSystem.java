package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.PlaceableComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.entity.IListener;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.geometry.Point;
import com.rawad.gamehelpers.geometry.Rectangle;

public class EntityPlacementSystem extends GameSystem {
	
	private TransformComponent cameraTransform;
	
	public EntityPlacementSystem(TransformComponent cameraTransform) {
		super();
		
		this.cameraTransform = cameraTransform;
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(PlaceableComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		PlaceableComponent placeableComp = e.getComponent(PlaceableComponent.class);
		
		World world = GameManager.instance().getCurrentGame().getWorld();
		
		if(placeableComp.isExtractRequested()) {
			
			Entity entityToExtract = EntityPlacementSystem.getMousedOverEntity(world, e, cameraTransform);
			
			placeableComp.setExtractRequested(false);
			
			if(entityToExtract != null) {
				
				TransformComponent transformToExtract = entityToExtract.getComponent(TransformComponent.class);
				
				if(transformToExtract != null) {
					for(IListener<TransformComponent> listener: placeableComp.getExtractionListeners()) {
						listener.onEvent(e, transformToExtract);
					}
				}
				
			}
			
		}
		
		if(placeableComp.isRemoveRequested()) {
			
			Entity toRemove = EntityPlacementSystem.getMousedOverEntity(world, e, cameraTransform);
			
			world.getEntitiesAsList().remove(toRemove);
			
			placeableComp.setRemoveRequested(false);
			
		}
		
		if(placeableComp.isPlaceRequested()) {
			
			CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
			
			if(!(collisionComp == null)) {
				if(gameEngine.getGameSystem(CollisionSystem.class).checkEntityCollision(e, 
						collisionComp.getHitbox()) == null) {
					
					Entity newEntity = Entity.createEntity(e, placeableComp.getToPlace());
					
					world.addEntity(newEntity);
					
					
				}
			}
			
			placeableComp.setPlaceRequested(false);
			
		}
		
		double mouseX = Mouse.isClamped()? Mouse.getClampX():Mouse.getX();
		double mouseY = Mouse.isClamped()? Mouse.getClampY():Mouse.getY();
		
		Point mouseInWorld = EntitySelectionSystem.transformFromScreen(cameraTransform, mouseX, mouseY);
		
		transformComp.setX(mouseInWorld.getX());
		transformComp.setY(mouseInWorld.getY());
		
		SelectionComponent selectionComp = e.getComponent(SelectionComponent.class);
		
		if(selectionComp != null) selectionComp.setHighlighted(false);// Don't highlight the placeable Entity.
		
	}
	
	public static Entity getMousedOverEntity(World world, Entity currentPlaceable, TransformComponent cameraTransform) {
		
		Point mouseInWorld = EntitySelectionSystem.transformFromScreen(cameraTransform, Mouse.getX(), Mouse.getY());
		
		for(Entity e: world.getEntitiesAsList()) {
			
			if(e.equals(currentPlaceable)) continue;
			
			TransformComponent transformComp = e.getComponent(TransformComponent.class);
			CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
			
			if(transformComp == null || collisionComp == null) continue;
			
			Rectangle hitboxInWorld = CollisionSystem.getHitboxInTransform(transformComp, collisionComp.getHitbox());
			
			if(hitboxInWorld.contains(mouseInWorld)) return e;
			
		}
		
		return null;
		
	}
	
}
