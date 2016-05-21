package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class EntitySelectingSystem extends GameSystem implements EventHandler<MouseEvent> {
	
	private boolean leftMouse;
	
	public EntitySelectingSystem() {
		super();
		
		compatibleComponentTypes.add(CollisionComponent.class);
		compatibleComponentTypes.add(SelectionComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
	}
	
	@Override
	public void handle(MouseEvent event) {
		
	}
	
}
