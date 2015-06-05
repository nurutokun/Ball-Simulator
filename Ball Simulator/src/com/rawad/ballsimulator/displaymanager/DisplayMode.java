package com.rawad.ballsimulator.displaymanager;

import java.awt.Component;

public abstract class DisplayMode {
	
	public abstract void create();
	
	public abstract void destroy();
	
	public abstract void repaint();
	
	/**
	 * Just so that the {@code Robot} in the {@code MouseInut} class has a position to set relative to.
	 * 
	 * @return
	 */
	public abstract Component getCurrentWindow();
	
}
