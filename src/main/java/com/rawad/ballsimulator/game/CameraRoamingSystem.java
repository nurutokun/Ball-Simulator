package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;
import com.rawad.gamehelpers.utils.Util;

public class CameraRoamingSystem extends GameSystem {
	
	/** Whether or not this {@code ameraRoamingSystem} should use the {@code Mouse}, when clamped, to move. */
	private final boolean useMouse;
	
	private Rectangle bounds;
	
	public CameraRoamingSystem(boolean useMouse, double width, double height) {
		super();
		
		this.useMouse = useMouse;
		
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
		
		if(useMouse && Mouse.isClamped()) {
			
			x += Mouse.getDx();
			y += Mouse.getDy();
			
		} else {
			
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
			
		}
		
		UserViewComponent userViewComp = e.getComponent(UserViewComponent.class);
		
		Rectangle viewport = userViewComp.getViewport();
		Rectangle requestedViewport = userViewComp.getRequestedViewport();
		
		double boundsWidth = bounds.getWidth();
		double boundsHeight = bounds.getHeight();
		
		if(requestedViewport.getWidth() > 0 && requestedViewport.getWidth() != viewport.getWidth()) 
			viewport.setWidth(requestedViewport.getWidth());
		
		if(requestedViewport.getHeight() > 0 && requestedViewport.getHeight() != viewport.getHeight()) 
			viewport.setHeight(requestedViewport.getHeight());
		
		// TODO: Fix centering when zooming in. Currently centers viewport around mouseInWorld but should center relative 
		// to mouse position on screen.
		double oldScaleX = transformComp.getScaleX();
		double oldScaleY = transformComp.getScaleY();
		
		double minScaleX = viewport.getWidth() / boundsWidth;
		double minScaleY = viewport.getHeight() / boundsHeight;
		
		double requestedScaleX = userViewComp.getPreferredScaleX();
		double requestedScaleY = userViewComp.getPreferredScaleY();
		
		if(requestedScaleX != 0)
			transformComp.setScaleX(Util.clamp(requestedScaleX, minScaleX, transformComp.getMaxScaleX()));
		if(requestedScaleY != 0)
			transformComp.setScaleY(Util.clamp(requestedScaleY, minScaleY, transformComp.getMaxScaleY()));
		
		requestedScaleX = transformComp.getScaleX();
		requestedScaleY = transformComp.getScaleY();
		
		if(requestedScaleX != oldScaleX)
			x += Mouse.getX() / oldScaleX - (viewport.getWidth() / transformComp.getScaleX() / 2d);
		if(requestedScaleY != oldScaleY)
			y += Mouse.getY() / oldScaleY - (viewport.getHeight() / transformComp.getScaleY() / 2d);
		
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
