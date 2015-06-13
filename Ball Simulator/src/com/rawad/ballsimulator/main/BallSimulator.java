package com.rawad.ballsimulator.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.gamestates.GameState;
import com.rawad.ballsimulator.gamestates.MenuState;
import com.rawad.ballsimulator.gamestates.OptionState;
import com.rawad.ballsimulator.gamestates.StateEnum;
import com.rawad.ballsimulator.gamestates.StateManager;
import com.rawad.ballsimulator.gui.Background;
import com.rawad.ballsimulator.input.KeyboardInput;
import com.rawad.ballsimulator.input.MouseInput;

public class BallSimulator {
	
	public static final String NAME = "Ball Simulator";
	
	private static final BallSimulator game = new BallSimulator();
	
	private StateManager sm;
	
	private Background background;
	
	private boolean debug;
	private boolean showSquares;
	
	double x;
	double y;
	
	public BallSimulator() {
		
		sm = new StateManager();
		
		debug = false;
		showSquares = false;
		
	}
	
	public static BallSimulator instance() {
		return game;
	}
	
	public void init() {
		
		background = new Background();
		
		sm.addState(new MenuState());
		sm.addState(new GameState());
		sm.addState(new OptionState());
		
		sm.setState(StateEnum.MENUSTATE);
		
	}
	
	private void update(long timePassed) {
		
		background.update(timePassed);
		
		sm.update();
		
		handleKeyboardInput();
		
		if(MouseInput.isClamped()) {
			
			x += (double) MouseInput.getX()*timePassed/(double) 100.0;
			y += (double) MouseInput.getY()*timePassed/(double) 100.0;
			
		}
		
	}
	
	private void handleKeyboardInput() {
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_F3)) {
			debug = !debug;
			
			KeyboardInput.setKeyDown(KeyEvent.VK_F3, false);
			
		} else if(KeyboardInput.isKeyDown(KeyEvent.VK_F4)) {
			showSquares = !showSquares;
			
			KeyboardInput.setKeyDown(KeyEvent.VK_F4, false);
			
		} else if(KeyboardInput.isKeyDown(KeyEvent.VK_C)) {
			
			MouseInput.setClamped(!MouseInput.isClamped(), DisplayManager.getDisplayWidth()/2, DisplayManager.getDisplayHeight()/2);
			
			x = 0;
			y = DisplayManager.getScreenHeight()/2;
			
			KeyboardInput.setKeyDown(KeyEvent.VK_C, false);
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
		update(DisplayManager.getDeltaTime());
		
		background.render(g);
			
		sm.render(g);
		
		if(debug) {
			renderDebugOverlay(g);
		}
		
		if(showSquares) {
			renderSquares(g);
		}
		
		g.setColor(Color.BLACK);
		g.fillOval((int) x - 20, (int) y - 20, 40, 40);
		
	}
	
	private void renderDebugOverlay(Graphics2D g) {
		
		g.setColor(Color.GREEN);
		g.fillOval(DisplayManager.getScreenWidth() - 50, DisplayManager.getScreenHeight() - 50, 50, 50);
		
		g.setColor(Color.WHITE);
		g.drawString(DisplayManager.getDisplayWidth() + ", " + DisplayManager.getDisplayHeight() + " | " +
			DisplayManager.getFPS() + " | " + DisplayManager.getDeltaTime(), 10, 10);
		
		g.drawString(MouseInput.getX() + ", " + MouseInput.getY(), 10, 20);
		
		g.setColor(Color.RED);
		g.fillRect(MouseInput.getX(), MouseInput.getY(), 1, 1);
		
	}
	
	private void renderSquares(Graphics2D g) {
		
		g.setColor(Color.WHITE);
		
		for(int i = 0; i < DisplayManager.getScreenWidth(); i++) {
			for(int j = 0; j < DisplayManager.getScreenHeight(); j++) {
				
				if(i%2 == 0 && j%2 == 0) {
					g.fillRect(i, j, 1, 1);
				}
				
			}	
		}
		
	}
	
	public boolean isDebug() {
		return debug;
	}
	
}
