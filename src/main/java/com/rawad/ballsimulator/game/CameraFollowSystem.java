package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.AttachmentComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;
import com.rawad.gamehelpers.utils.Util;

public class CameraFollowSystem extends GameSystem {
	
	private Rectangle bounds;
	
	public CameraFollowSystem(double width, double height) {
		super();
		
		bounds = new Rectangle(0, 0, width, height);
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(AttachmentComponent.class);
		compatibleComponentTypes.add(UserViewComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		AttachmentComponent attachmentComp = e.getComponent(AttachmentComponent.class);
		UserViewComponent userViewComp = e.getComponent(UserViewComponent.class);
		
		TransformComponent attachedToTransform = attachmentComp.getAttachedTo().getComponent(TransformComponent.class);
		
		Rectangle viewport = userViewComp.getViewport();
		
		double boundsWidth = bounds.getWidth();
		double boundsHeight = bounds.getHeight();
		
		double minScaleX = viewport.getWidth() / boundsWidth;
		transformComp.setScaleX(Util.clamp(transformComp.getScaleX(), minScaleX, transformComp.getMaxScaleX()));
		
		double minScaleY = viewport.getHeight() / boundsHeight;
		transformComp.setScaleY(Util.clamp(transformComp.getScaleY(), minScaleY, transformComp.getMaxScaleY()));
		
		viewport.setX(attachedToTransform.getX() - (viewport.getWidth() / transformComp.getScaleX() / 2d));
		viewport.setY(attachedToTransform.getY() - (viewport.getHeight() / transformComp.getScaleY() / 2d));
		
		bounds.setWidth(bounds.getWidth() - (viewport.getWidth() / transformComp.getScaleX()));
		bounds.setHeight(bounds.getHeight() - (viewport.getHeight() / transformComp.getScaleY()));
		
		CollisionSystem.keepInBounds(viewport, bounds);
		
		bounds.setWidth(boundsWidth);
		bounds.setHeight(boundsHeight);
		
		transformComp.setX(viewport.getX());
		transformComp.setY(viewport.getY());
		
	}
	
}
