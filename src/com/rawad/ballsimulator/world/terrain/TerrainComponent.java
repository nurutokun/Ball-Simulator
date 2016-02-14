package com.rawad.ballsimulator.world.terrain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class TerrainComponent {
	
	/** Temporary */
	private Color highlightColor;
	
	private double x;
	private double y;
	
	private int width;
	private int height;
	
	private boolean selected;
	
	public TerrainComponent(double x, double y, int width, int height) {
		
		highlightColor = Color.GREEN;
		
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
	}
	
	public void render(Graphics2D g) {
		
		if(selected) {
			g.setColor(highlightColor);
		} else {
			g.setColor(Color.RED);
		}
		
		g.translate(x, y);
		
		g.drawRect(0, 0, width, height);
		
		g.translate(-x, -y);
		
	}
	
	public boolean intersects(Rectangle hitbox) {
		Rectangle curHit = new Rectangle((int) x, (int) y, width, height);
		
		return curHit.intersects(hitbox);
		
	}
	
	public boolean intersects(int x, int y) {
		return new Rectangle((int) this.x, (int) this.y, width, height).contains(x, y);
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
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
	
	public void setHighlightColor(Color color) {
		this.highlightColor = color;
	}
	
}
