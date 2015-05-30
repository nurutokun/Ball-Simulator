package com.rawad.ballsimulator.main;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.input.MouseInput;

public class BallSimulator {
	
	public static final String NAME = "Ball Simulator";
	
	private static final BallSimulator game = new BallSimulator();
	
	public static BallSimulator instance() {
		return game;
	}
	
	public static void init() {
		
		
		
	}
	
	private void update(long timePassed) {
		
	}
	
	public void render(Graphics2D g) {
		
		update(DisplayManager.getDeltaTime());
		
//		g.setColor(Color.BLUE);
		
//		g.fillRect(0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());
		
		g.setColor(Color.WHITE);
		
		g.fillOval(DisplayManager.getWidth() - 50, DisplayManager.getHeight() - 50, 50, 50);
		g.drawString(DisplayManager.getWidth() + ", " + DisplayManager.getHeight() + " | " +
			DisplayManager.getFPS() + " | " + DisplayManager.getDeltaTime(), 10, 10);
		
		g.drawString(MouseInput.getX() + ", " + MouseInput.getY(), 10, 20);
		
		g.setColor(Color.RED);
		g.fillRect(MouseInput.getX(), MouseInput.getY(), 1, 1);
		
		/*
		for(int i = 0; i < DisplayManager.getWidth(); i++) {
			for(int j = 0; j < DisplayManager.getHeight(); j++) {
				
				if(i%2 == 0 && j%2 == 0) {
					g.fillRect(i, j, 1, 1);
				}
				
			}
			
		}
		/**/
		
	}
	
}
