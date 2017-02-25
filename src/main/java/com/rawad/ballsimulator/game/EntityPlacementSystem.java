package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.client.input.Mouse;
import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.PlaceableComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.game.event.EventType;
import com.rawad.ballsimulator.geometry.Rectangle;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.World;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;
import com.rawad.gamehelpers.geometry.Point2d;

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
		
		World world = GameManager.getCurrentGame().getWorld();
		
		if(placeableComp.isExtractRequested()) {
			
			Entity entityToExtract = EntityPlacementSystem.getMousedOverEntity(world, e, cameraTransform);
			
			placeableComp.setExtractRequested(false);
			
			if(entityToExtract != null && entityToExtract.getComponent(TransformComponent.class) != null) {
				
				placeableComp.setToExtract(entityToExtract);
				
				gameEngine.submitEvent(new Event(EventType.ENTITY_EXTRACT, e));
				
			}
			
		}
		
		if(placeableComp.isRemoveRequested()) {
			
			Entity toRemove = EntityPlacementSystem.getMousedOverEntity(world, e, cameraTransform);
			
			world.removeEntity(toRemove);
			
			placeableComp.setRemoveRequested(false);
			
		}
		
		if(placeableComp.isPlaceRequested()) {
			
			CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
			
			if(!(collisionComp == null)) {
				if(gameEngine.getGameSystems().get(CollisionSystem.class).checkEntityCollision(e, 
						collisionComp.getHitbox()) == null) {
					
					Entity newEntity = Entity.createEntity(e, placeableComp.getToPlace());
					
					world.addEntity(newEntity);
					
					
				}
			}
			
			placeableComp.setPlaceRequested(false);
			
		}
		
		double mouseX = Mouse.isClamped()? Mouse.getClampX():Mouse.getX();
		double mouseY = Mouse.isClamped()? Mouse.getClampY():Mouse.getY();
		
		Point2d mouseInWorld = EntitySelectionSystem.transformFromScreen(cameraTransform, mouseX, mouseY);
		
		transformComp.setX(mouseInWorld.getX());
		transformComp.setY(mouseInWorld.getY());
		
		SelectionComponent selectionComp = e.getComponent(SelectionComponent.class);
		
		if(selectionComp != null) selectionComp.setHighlighted(false);// Don't highlight the placeable Entity.
		
	}
	
	public static Entity getMousedOverEntity(World world, Entity currentPlaceable, TransformComponent cameraTransform) {
		
		Point2d mouseInWorld = EntitySelectionSystem.transformFromScreen(cameraTransform, Mouse.getX(), Mouse.getY());
		
		for(Entity e: world.getEntities()) {
			
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
