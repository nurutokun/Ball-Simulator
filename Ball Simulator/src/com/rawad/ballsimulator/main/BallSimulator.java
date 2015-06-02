package com.rawad.ballsimulator.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.gamestates.MenuState;
import com.rawad.ballsimulator.gamestates.StateManager;
import com.rawad.ballsimulator.input.KeyboardInput;
import com.rawad.ballsimulator.input.MouseInput;

public class BallSimulator {
	
	public static final String NAME = "Ball Simulator";
	
	private static final BallSimulator game = new BallSimulator();
	
	private StateManager sm;
	
	private boolean debug;
	private boolean showSquares;
	
	int i = 0;
	
	public BallSimulator() {
		
		sm = new StateManager();
		
		debug = false;
		showSquares = false;
		
	}
	
	public static BallSimulator instance() {
		return game;
	}
	
	public void init() {
		
		sm.addState(new MenuState());
		
	}
	
	private void update(long timePassed) {
		
		sm.update();
		
		handleKeyboardInput();
		
	}
	
	private void handleKeyboardInput() {
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_F3)) {
			debug = !debug;
			
			KeyboardInput.setKeyDown(KeyEvent.VK_F3, false);
			
		} else if(KeyboardInput.isKeyDown(KeyEvent.VK_F4)) {
			showSquares = !showSquares;
			
			KeyboardInput.setKeyDown(KeyEvent.VK_F4, false);
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
		update(DisplayManager.getDeltaTime());
		
		sm.render(g);
		
		if(debug) {
			renderDebugOverlay(g);
		}
		
		if(showSquares) {
			renderSquares(g);
		}
		
	}
	
	private void renderDebugOverlay(Graphics2D g) {
		
		g.setColor(Color.WHITE);
		
		g.fillOval(DisplayManager.getWidth() - 50, DisplayManager.getHeight() - 50, 50, 50);
		g.drawString(DisplayManager.getWidth() + ", " + DisplayManager.getHeight() + " | " +
			DisplayManager.getFPS() + " | " + DisplayManager.getDeltaTime(), 10, 10);
		
		g.drawString(MouseInput.getX() + ", " + MouseInput.getY(), 10, 20);
		
		g.setColor(Color.RED);
		g.fillRect(MouseInput.getX(), MouseInput.getY(), 1, 1);
		
	}
	
	private void renderSquares(Graphics2D g) {
		
		g.setColor(Color.WHITE);
		
		for(int i = 0; i < DisplayManager.getWidth(); i++) {
			for(int j = 0; j < DisplayManager.getHeight(); j++) {
				
				if(i%2 == 0 && j%2 == 0) {
					g.fillRect(i, j, 1, 1);
				}
				
			}	
		}
		
	}
	
}
