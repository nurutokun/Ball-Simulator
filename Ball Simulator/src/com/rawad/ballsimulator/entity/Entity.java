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
		
		hitbox = new Rectangle((int) x, (int) y, width, height);
		
	}
	
	public abstract void update(long timePassed);
	
	public abstract void render(Graphics2D g);
	
	protected void updateHitbox(double x, double y, int width, int height) {
		hitbox.setBounds((int) x, (int) y, width, height);
	}
	
	public void updateHitbox() {
		this.updateHitbox(this.x, this.y, this.width, this.height);
	}
	
	public boolean intersects(Entity e) {
		return hitbox.intersects(e.getHitbox());
	}
	
	public boolean intersects(int x, int y) {
		return hitbox.contains(x, y);
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
}
