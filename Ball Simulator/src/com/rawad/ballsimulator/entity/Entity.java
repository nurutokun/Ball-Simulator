package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;

public abstract class Entity {
	
	public Entity() {
		
	}
	
	public abstract void update(long timePassed);
	
	public abstract void render(Graphics2D g);
	
}
