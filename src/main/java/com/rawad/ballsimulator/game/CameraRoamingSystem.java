package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

public class CameraRoamingSystem extends GameSystem {
	
	private Rectangle bounds;
	
	public CameraRoamingSystem(double width, double height) {
		super();
		
		bounds = new Rectangle(0, 0, width, height);
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(MovementComponent.class);
		compatibleComponentTypes.add(UserControlComponent.class);
		compatibleComponentTypes.add(UserViewComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		MovementComponent movementComp = e.getComponent(MovementComponent.class);
		
		double x = transformComp.getX();
		double y = transformComp.getY();
		
		if(movementComp.isUp()) {
			y -= 5;
		} else if(movementComp.isDown()) {
			y += 5;
		}
		
		if(movementComp.isRight()) {
			x += 5;
		} else if(movementComp.isLeft()) {
			x -= 5;
		}
		
		Rectangle viewport = e.getComponent(UserViewComponent.class).getViewport();

		double boundsWidth = bounds.getWidth();
		double boundsHeight = bounds.getHeight();
		
		viewport.setX(x);
		viewport.setY(y);
		
		bounds.setWidth(bounds.getWidth() - (viewport.getWidth() / transformComp.getScaleX()));
		bounds.setHeight(bounds.getHeight() - (viewport.getHeight() / transformComp.getScaleY()));
		
		CollisionSystem.keepInBounds(viewport, bounds);
		
		bounds.setWidth(boundsWidth);
		bounds.setHeight(boundsHeight);
		
		transformComp.setX(viewport.getX());
		transformComp.setY(viewport.getY());
		
	}
	
}
