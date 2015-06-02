package com.rawad.ballsimulator.displaymanager;


public abstract class DisplayMode {
	
	public abstract void create();
	
	public abstract void destroy();
	
	public abstract void repaint();
	
	public abstract void update();
	
}
