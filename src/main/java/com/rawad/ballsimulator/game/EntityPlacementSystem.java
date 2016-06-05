package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.PlaceableComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.entity.IListener;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.geometry.Point;

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
			
			Entity entityToExtract = EntityPlacementSystem.getMousedOverEntity(world, cameraTransform);
			
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
			
			Entity toRemove = EntityPlacementSystem.getMousedOverEntity(world, cameraTransform);
			
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
		
		Point mouseInWorld = cameraTransform.transformFromScreen(mouseX, mouseY);
		
		transformComp.setX(mouseInWorld.getX());
		transformComp.setY(mouseInWorld.getY());
		
	}
	
	public static Entity getMousedOverEntity(World world, TransformComponent cameraTransform) {
		
		Point mouseInWorld = cameraTransform.transformFromScreen(Mouse.getX(), Mouse.getY());
		
		for(Entity e: world.getEntitiesAsList()) {
			
//			TransformComponent transformComp = e.getComponent(TransformComponent.class);
			CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
			
//			Rectangle hitboxInWorld = new Rectangle(collisionComp.getX() +)
			
			if(collisionComp != null)
				if(collisionComp.getHitbox().contains(mouseInWorld))
					return e;
			
		}
		
		return null;
		
	}
	
}
