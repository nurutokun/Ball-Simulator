package com.rawad.ballsimulator.main;

import java.awt.Graphics2D;

import com.rawad.ballsimulator.displaymanager.DisplayManager;

public class BallSimulator {
	
	public static final String NAME = "Ball Simulator";
	
	private static final BallSimulator game = new BallSimulator();
	
	public static BallSimulator instance() {
		return game;
	}
	
	public static void init() {
		
	}
	
	public void render(Graphics2D g) {
		
		g.fillOval(DisplayManager.getWidth()/2, DisplayManager.getHeight()/2, 10, 10);
		g.drawString("check me out", 10, 10);
		
	}
	
}
