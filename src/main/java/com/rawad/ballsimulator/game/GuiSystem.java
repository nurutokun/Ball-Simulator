package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.GuiComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;

/**
 * Handles any gui that is {@code Entity}-related to be displayed.
 * 
 * @author Rawad
 *
 */
public class GuiSystem extends GameSystem {
	
	public GuiSystem() {
		super();
		
		compatibleComponentTypes.add(GuiComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
	}
	
}
