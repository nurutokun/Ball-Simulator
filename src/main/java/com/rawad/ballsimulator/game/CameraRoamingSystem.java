package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.game.event.EventType;
import com.rawad.ballsimulator.game.event.MovementEvent;
import com.rawad.ballsimulator.geometry.Rectangle;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;
import com.rawad.gamehelpers.game.event.EventManager;
import com.rawad.gamehelpers.game.event.Listener;
import com.rawad.gamehelpers.utils.Util;
import com.rawad.jfxengine.client.input.Mouse;

public class CameraRoamingSystem extends GameSystem implements Listener {
	
	private static final double ACCELERATION = 5d;
	
	/** Whether or not this {@code CameraRoamingSystem} should use the {@code Mouse}, when clamped, to move. */
	private final boolean useMouse;
	
	private Rectangle bounds;
	
	public CameraRoamingSystem(EventManager eventManager, boolean useMouse, double width, double height) {
		super();
		
		this.useMouse = useMouse;
		
		bounds = new Rectangle(0, 0, width, height);
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(MovementComponent.class);
		compatibleComponentTypes.add(UserControlComponent.class);
		compatibleComponentTypes.add(UserViewComponent.class);
		
		eventManager.registerListener(EventType.MOVEMENT, this);
		
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
			
			x += movementComp.getAx();
			y += movementComp.getAy();
			
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
		
		double prefScaleX = userViewComp.getPreferredScaleX();
		double prefScaleY = userViewComp.getPreferredScaleY();
		
		if(userViewComp.isShowEntireWorld()) {
			prefScaleX = minScaleX;
			prefScaleY = minScaleY;
		}
		
		transformComp.setScaleX(Util.clamp(prefScaleX, minScaleX, transformComp.getMaxScaleX()));
		transformComp.setScaleY(Util.clamp(prefScaleY, minScaleY, transformComp.getMaxScaleY()));
		
		prefScaleX = transformComp.getScaleX();
		prefScaleY = transformComp.getScaleY();
		
		if(prefScaleX != oldScaleX)
			x += Mouse.getX() / oldScaleX - (viewport.getWidth() / transformComp.getScaleX() / 2d);
		if(prefScaleY != oldScaleY)
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
	
	/**
	 * @see com.rawad.gamehelpers.game.event.Listener#onEvent(com.rawad.gamehelpers.game.event.Event)
	 */
	@Override
	public void onEvent(Event ev) {
		
		MovementEvent movementEvent = (MovementEvent) ev;
		
		Entity entityToMove = movementEvent.getEntityToMove();
		MovementRequest movementRequest = movementEvent.getMovementRequest();
		
		MovementComponent movementComp = entityToMove.getComponent(MovementComponent.class);
		
		
		movementComp.setAx((movementRequest.isRight()? 1d: movementRequest.isLeft()? -1d:0) * ACCELERATION);
		movementComp.setAy((movementRequest.isDown()? 1d: movementRequest.isUp()? -1d:0) * ACCELERATION);
		
	}
	
}
