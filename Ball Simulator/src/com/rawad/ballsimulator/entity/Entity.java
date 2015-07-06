package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.rawad.ballsimulator.world.World;

public abstract class Entity {
	
	// TODO: Implement UUID's with UUIDManager
//	private final String UUID;
	
	protected World world;
	
	protected Rectangle hitbox;
	
	protected String name = "";
	
	protected double x;
	protected double y;
	
	protected int width;
	protected int height;
	
	public Entity(World world) {
		
		this.world = world;
		
		hitbox = new Rectangle();
		
		this.world.addEntity(this);
		
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
	
	/**
	 * 
	 * @return the x-coordinate of the center point of this entity
	 */
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * 
	 * @return the y-coordinate of the center point of this entity
	 */
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * 
	 * @return Current world this entity is in.
	 */
	public World getWorld() {
		return world;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
