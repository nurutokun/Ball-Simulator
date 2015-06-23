package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.rawad.ballsimulator.world.World;

public abstract class Entity {
	
	protected World world;
	
	protected double x;
	protected double y;
	
	protected int width;
	protected int height;
	
	protected Rectangle hitbox;
	
	public Entity(World world) {
		
		this.world = world;
		
		this.world.addEntity(this);
		
	}
	
	public abstract void update(long timePassed);
	
	public abstract void render(Graphics2D g);
	
	public boolean intersects(Entity e) {
		return hitbox.intersects(e.getHitbox());
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
}
