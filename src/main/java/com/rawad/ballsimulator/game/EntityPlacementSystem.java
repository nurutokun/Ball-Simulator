package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.PlaceableComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.entity.Listener;
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
		RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
		
		PlaceableComponent placeableComp = e.getComponent(PlaceableComponent.class);
		
		World world = GameManager.instance().getCurrentGame().getWorld();
		
		double scaleX = transformComp.getScaleX();// Used for centering, later. To prevent jumping when extracting, this is
		double scaleY = transformComp.getScaleY();// set immediately rather than waiting for the listeners.
		
		if(placeableComp.isExtractRequested()) {
			
			Entity entityToExtract = EntityPlacementSystem.getMousedOverEntity(world, cameraTransform);
			
			placeableComp.setExtractRequested(false);
			
			if(entityToExtract != null) {
				
				TransformComponent transformToExtract = entityToExtract.getComponent(TransformComponent.class);
				
				scaleX = transformToExtract.getScaleX();
				scaleY = transformToExtract.getScaleY();
				
				if(transformToExtract != null) {
					for(Listener<TransformComponent> listener: placeableComp.getExtractionListeners()) {
						listener.onEvent(e, transformToExtract);
					}
				}
				
			}
			
		}
		
		if(placeableComp.isRemoveRequested()) {
			
			Entity toRemove = getMousedOverEntity(world, cameraTransform);
			
			world.getEntitiesAsList().remove(toRemove);
			
			placeableComp.setRemoveRequested(false);
			
		}
		
		double width = 0;
		double height = 0;
		
		if(renderingComp != null) {
			width = renderingComp.getTexture().getWidth() * scaleX;
			height = renderingComp.getTexture().getHeight() * scaleY;
		}
		
		if(placeableComp.isPlaceRequested()) {
			
			if(gameEngine.getGameSystem(CollisionSystem.class).checkEntityCollision(e, new Rectangle(transformComp.getX(), 
						transformComp.getY(), width, height)) == null) {
				EntityPlacementSystem.placeEntity(GameManager.instance().getCurrentGame().getWorld(), e, 
						placeableComp.getToPlace());
			}
			
			placeableComp.setPlaceRequested(false);
			
		}
		
		Point mouseInWorld = cameraTransform.transformFromScreen(Mouse.getX(), Mouse.getY());
		
		transformComp.setX(mouseInWorld.getX() - (width / 2d));
		transformComp.setY(mouseInWorld.getY() - (height / 2d));
		
	}
	
	/**
	 * 
	 * @param world {@code World} to add the newly created {@code Entity} to.
	 * @param e {@code Entity} from which the data is to be taken from.
	 * @param blueprintId Used to get the blueprint for the new {@code Entity}.
	 */
	public static void placeEntity(World world, Entity e, Object blueprintId) {
		
		Entity newEntity = Entity.createEntity(e, blueprintId);
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
		
		CollisionComponent collisionComp = newEntity.getComponent(CollisionComponent.class);
		
		if(collisionComp != null && renderingComp != null) {
			
			Rectangle hitbox = collisionComp.getHitbox();
			
			hitbox.setX(transformComp.getX());
			hitbox.setY(transformComp.getY());
			hitbox.setWidth(renderingComp.getTexture().getWidth() * transformComp.getScaleX());
			hitbox.setHeight(renderingComp.getTexture().getHeight() * transformComp.getScaleY());
			
		}
		
		world.addEntity(newEntity);
		
	}
	
	public static Entity getMousedOverEntity(World world, TransformComponent cameraTransform) {
		
		Point mouseInWorld = cameraTransform.transformFromScreen(Mouse.getX(), Mouse.getY());
		
		for(Entity e: world.getEntitiesAsList()) {
			
			CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
			
			if(collisionComp != null)
				if(collisionComp.getHitbox().contains(mouseInWorld))
					return e;
			
		}
		
		return null;
		
	}
	
}
