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
		compatibleComponentTypes.add(UserViewComponent.class);
		compatibleComponentTypes.add(UserControlComponent.class);
		
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
		
		double minScaleX = viewport.getWidth() / bounds.getWidth();
		
		if(transformComp.getScaleX() < minScaleX) {
			transformComp.setScaleX(minScaleX);
		} else if(transformComp.getScaleX() > transformComp.getMaxScaleX()) {
			transformComp.setScaleX(transformComp.getMaxScaleX());
		}
		
		double minScaleY = viewport.getHeight() / bounds.getHeight();
		
		if(transformComp.getScaleY() < minScaleY) {
			transformComp.setScaleY(minScaleY);
		} else if(transformComp.getScaleY() > transformComp.getMaxScaleY()) {
			transformComp.setScaleY(transformComp.getMaxScaleY());
		}
		
		double maxWidth = bounds.getWidth() - (viewport.getWidth() / transformComp.getScaleX());
		
		if(x < bounds.getX()) {
			x = bounds.getX();
		} else if(x > maxWidth) {
			x = maxWidth;
		}
		
		double maxHeight = bounds.getHeight() - (viewport.getHeight() / transformComp.getScaleY());
		
		if(y < bounds.getY()) {
			y = bounds.getY();
		} else if(y > maxHeight) {
			y = maxHeight;
		}
		
		transformComp.setX(x);
		transformComp.setY(y);
		
	}
	
}
