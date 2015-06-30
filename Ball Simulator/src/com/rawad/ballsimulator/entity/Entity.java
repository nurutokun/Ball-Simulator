package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.rawad.ballsimulator.world.World;

public abstract class Entity {
	
	protected World world;
	
	protected Rectangle hitbox;
	
	protected double x;
	protected double y;
	
	protected int width;
	protected int height;
	
	public Entity(World world) {
		
		this.world = world;
		
		this.world.addEntity(this);
		
		hitbox = new Rectangle();
		
	}
	
	public abstract void update(long timePassed);
	
	public abstract void render(Graphics2D g);
	
	protected void updateHitbox(double x, double y, int width, int height) {
		hitbox.setBounds((int) x, (int) y, width, height);
	}
	
	public void updateHitbox() {
		updateHitbox(x - (width/2), y - (height/2), width, height);
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
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
}
