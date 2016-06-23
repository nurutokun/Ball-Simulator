package com.rawad.ballsimulator.game;

import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;


public class InputControlSystem extends GameSystem {
	
	public InputControlSystem() {
		super();
		
	}
	
	@Override
	public void tick(Entity e) {
		// How to communicate info from here to movement comp? Through IListener? InputComponent?
		// How to have this listen to events? i.e. most automatic way.
	}
	
}
