package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.AttachmentComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

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
		
		double x = attachedToTransform.getX() - (viewport.getWidth() / transformComp.getScaleX() / 2d);
		double y = attachedToTransform.getY() - (viewport.getHeight() / transformComp.getScaleY() / 2d);
		
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
