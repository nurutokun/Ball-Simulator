package com.rawad.ballsimulator.world.terrain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class TerrainComponent {
	
	private double x;
	private double y;
	
	private int width;
	private int height;
	
	public TerrainComponent(double x, double y, int width, int height) {
		
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.RED);
		g.drawRect((int) x, (int) y, width, height);
	}
	
	public boolean intersects(Rectangle hitbox) {
		Rectangle curHit = new Rectangle((int) x, (int) y, width, height);
		
		return curHit.intersects(hitbox);
		
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
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
	
}
