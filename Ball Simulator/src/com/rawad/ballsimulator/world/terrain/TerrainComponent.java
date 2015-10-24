package com.rawad.ballsimulator.world.terrain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.rawad.ballsimulator.loader.Loader;

public class TerrainComponent {
	
	
	private double x;
	private double y;
	
	private int width;
	private int height;
	
	public boolean selected;
	
	public TerrainComponent(double x, double y, int width, int height) {
		
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
	}
	
	public void render(Graphics2D g) {
		if(selected) {
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.RED);
		}
		
		g.drawRect((int) x, (int) y, width, height);
	}
	
	public boolean intersects(Rectangle hitbox) {
		Rectangle curHit = new Rectangle((int) x, (int) y, width, height);
		
		return curHit.intersects(hitbox);
		
	}
	
	public boolean intersects(int x, int y) {
		return new Rectangle((int) this.x, (int) this.y, width, height).contains(x, y);
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
	
	@Override
	public String toString() {
		
		String regex = Loader.TERRAIN_COMPONENT_ATTRIB_SPLIT;
		
		return x + regex + y + regex + width + regex + height;
	}
	
}
