package com.rawad.ballsimulator.gui;

import java.awt.Graphics2D;

public abstract class GuiComponent {
	
	protected String id;
	
	protected int x;
	protected int y;
	
	protected int width;
	protected int height;
	
	public GuiComponent(String id, int x, int y, int width, int height) {
		
		this.id = id;
		
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
	}
	
	/**
	 * Update position and/or highlighted-status and/or typing cursor etc.
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
	
}
