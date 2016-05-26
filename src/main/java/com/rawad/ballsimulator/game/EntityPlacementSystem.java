package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.PlaceableComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.geometry.Point;
import com.rawad.gamehelpers.geometry.Rectangle;

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
		
		RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
		
		double width = 0;
		double height = 0;
		
		if(renderingComp != null) {
			width = renderingComp.getTexture().getWidth() * transformComp.getScaleX();
			height = renderingComp.getTexture().getHeight() * transformComp.getScaleY();
		}
		
		if(selectionComp.isSelected()) {
			
			Point mouseInWorld = cameraTransform.transformFromScreen(Mouse.getX(), Mouse.getY());
			
			double x = mouseInWorld.getX();
			double y = mouseInWorld.getY();
			
				x -= width / 2d;
				y -= height / 2d;
			
			transformComp.setX(x);
			transformComp.setY(y);
			
		}
		
		PlaceableComponent placeableComp = e.getComponent(PlaceableComponent.class);
		
		if(placeableComp.isPlaceRequested() && gameEngine.getGameSystem(CollisionSystem.class).checkEntityCollision(e, new Rectangle(transformComp.getX(), transformComp.getY(), width, height)) == null) {
			
			EntityPlacementSystem.placeEntity(GameManager.instance().getCurrentGame().getWorld(), e, 
					placeableComp.getToPlace());
			
			placeableComp.setPlaceRequested(false);
			
		}
		
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
		
		if(collisionComp != null) {
			
			Rectangle hitbox = collisionComp.getHitbox();
			
			hitbox.setX(transformComp.getX());
			hitbox.setY(transformComp.getY());
			hitbox.setWidth(renderingComp.getTexture().getWidth() * transformComp.getScaleX());
			hitbox.setHeight(renderingComp.getTexture().getHeight() * transformComp.getScaleY());
			
		}
		
		world.addEntity(newEntity);
		
	}
	
}
