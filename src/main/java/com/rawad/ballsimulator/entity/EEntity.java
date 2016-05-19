package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.utils.Util;

public enum EEntity {
	
	CAMERA(TransformComponent.class, AttachmentComponent.class, UserViewComponent.class),// Transform for scaling, collision for keeping in bounds
	USER_CONTROLLABLE_CAMERA(TransformComponent.class, MovementComponent.class, UserControlComponent.class, UserViewComponent.class),
	STATIC(TransformComponent.class, CollisionComponent.class, RenderingComponent.class),
	PLAYER(TransformComponent.class, CollisionComponent.class, HealthComponent.class, MovementComponent.class, RollingComponent.class, RenderingComponent.class),
	USER_CONTROLLABLE_PLAYER(PLAYER.getComponents(), GuiComponent.class, UserControlComponent.class, RandomPositionComponent.class),
	NETWORKING_PLAYER(PLAYER.getComponents(), GuiComponent.class);// NetworkControlComponent? (or that + UserControlComponent)
	
	private final Class<? extends Component>[] components;
	
	@SafeVarargs
	private EEntity(Class<? extends Component>... components) {
		this.components = components;
	}
	
	@SafeVarargs
	private EEntity(Class<? extends Component>[] components, Class<? extends Component>... moreComponents) {
		this(Util.append(components, moreComponents));
	}
	
	public Class<? extends Component>[] getComponents() {
		return components;
	}
	
}
