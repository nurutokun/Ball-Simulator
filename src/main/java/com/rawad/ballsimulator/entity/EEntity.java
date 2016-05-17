package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.utils.Util;

public enum EEntity {
	
	CAMERA(new TransformComponent(), new AttachmentComponent(), new UserViewComponent()),// Transform for scaling, collision for keeping in bounds
	USER_CONTROLLABLE_CAMERA(new TransformComponent(), new MovementComponent(), new UserControlComponent(), new UserViewComponent()),
	STATIC(new TransformComponent(), new CollisionComponent(), new RenderingComponent()),
	PLAYER(new TransformComponent(), new CollisionComponent(), new HealthComponent(), new MovementComponent(), new RollingComponent(), new RenderingComponent()),
	USER_CONTROLLABLE_PLAYER(PLAYER.getComponents(), new GuiComponent(), new UserControlComponent(), new RandomPositionComponent()),
	NETWORKING_PLAYER(PLAYER.getComponents(), new GuiComponent());// NetworkControlComponent? (or that + UserControlComponent)
	
	private final Component[] components;
	
	private EEntity(Component... components) {
		this.components = components;
	}
	
	private EEntity(Component[] components, Component... moreComponents) {
		this(Util.append(components, moreComponents));
	}
	
	public Component[] getComponents() {
		return components;
	}
	
}
