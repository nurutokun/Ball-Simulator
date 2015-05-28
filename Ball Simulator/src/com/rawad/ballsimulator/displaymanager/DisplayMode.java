package com.rawad.ballsimulator.displaymanager;


public abstract class DisplayMode {
	
	private int width;
	private int height;
	
	public abstract void create();
	
	public abstract void destroy();
	
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
