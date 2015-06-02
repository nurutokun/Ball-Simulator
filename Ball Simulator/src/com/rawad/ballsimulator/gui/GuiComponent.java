package com.rawad.ballsimulator.gui;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class GuiComponent {
	
	protected String id;
	
	protected Color background;
	protected Color foreground;
	
	protected int x;
	protected int y;
	
	protected int width;
	protected int height;
	
	private boolean hovered;
	
	public GuiComponent(String id, int x, int y, int width, int height) {
		
		this.id = id;
		
		background = Color.GREEN;
		foreground = Color.PINK;
		
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
		hovered = false;
		
	}
	
	/**
	 * Update position and/or highlighted-status and/or typing cursor etc.
	 * 
	 * @param x X-coordinate of mouse position
	 * @param y Y-coordinate of mouse position
	 */
	public abstract void update(int x, int y);
	
	public abstract void render(Graphics2D g);
	
	public boolean intersects(int x, int y) {
		
		if( x >= this.x && x <= this.x + width &&
			y >= this.y && y <= this.y + height) {
			
			return true;
		}
		
		return false;
	}
	
	public String getId() {
		return id;
	}
	
	public Color getBackground() {
		return background;
	}
	
	public void setBackground(Color background) {
		this.background = background;
	}
	
	public Color getForeground() {
		return foreground;
	}
	
	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}
	
	public boolean isHovered() {
		return hovered;
	}
	
	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}
	
}
