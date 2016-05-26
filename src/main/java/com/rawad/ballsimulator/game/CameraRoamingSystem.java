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
	
	/** Whether or not this {@code GameSystem} should use the {@code Mouse}, when clamped, to move. */
	private final boolean useMouse;
	
	private Rectangle bounds;
	
	private double requestedViewportWidth;
	private double requestedViewportHeight;
	
	private double requestedScaleX;
	private double requestedScaleY;
	
	public CameraRoamingSystem(boolean useMouse, double width, double height) {
		super();
		
		this.useMouse = useMouse;
		
		bounds = new Rectangle(0, 0, width, height);
		
		requestedScaleX = 0;
		requestedScaleY = 0;
		
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
		
		Rectangle viewport = e.getComponent(UserViewComponent.class).getViewport();
		
		double boundsWidth = bounds.getWidth();
		double boundsHeight = bounds.getHeight();
		
		if(requestedViewportWidth > 0 && requestedViewportWidth != viewport.getWidth()) {
			
			viewport.setWidth(requestedViewportWidth);
			
		}
		
		if(requestedViewportWidth > 0 && requestedViewportHeight != viewport.getHeight()) {
			
			viewport.setHeight(requestedViewportHeight);
			
		}
		
		// TODO: Fix centering when zooming in.
		double oldScaleX = transformComp.getScaleX();
		double oldScaleY = transformComp.getScaleY();
		
		double minScaleX = viewport.getWidth() / boundsWidth;
		double minScaleY = viewport.getHeight() / boundsHeight;
				
		transformComp.setScaleX(Util.clamp(requestedScaleX, minScaleX, transformComp.getMaxScaleX()));
		transformComp.setScaleY(Util.clamp(requestedScaleY, minScaleY, transformComp.getMaxScaleY()));
		
		requestedScaleX = transformComp.getScaleX();
		requestedScaleY = transformComp.getScaleY();
		
		if(requestedScaleX != oldScaleX)
			x += (Mouse.getX() / oldScaleX - (viewport.getWidth() / transformComp.getScaleX() / 2d));
		if(requestedScaleY != oldScaleY)
			y += (Mouse.getY() / oldScaleY - (viewport.getHeight() / transformComp.getScaleY() / 2d));
		
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
	
	public void requestNewViewportWidth(double newWidth) {
		requestedViewportWidth = newWidth;
	}
	
	public void requestNewViewportHeight(double newHeight) {
		requestedViewportHeight = newHeight;
	}
	
	public void requestScaleX(double scaleFactor) {
		requestedScaleX = scaleFactor;
	}
	
	public void requestScaleY(double scaleFactor) {
		requestedScaleY = scaleFactor;
	}
	
}
