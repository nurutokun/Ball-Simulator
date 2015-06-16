package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;

import com.rawad.ballsimulator.world.World;

public abstract class Entity {
	
	protected World world;
	
	public Entity(World world) {
		
		this.world = world;
		
	}
	
	public abstract void update(long timePassed);
	
	public abstract void render(Graphics2D g);
	
}
