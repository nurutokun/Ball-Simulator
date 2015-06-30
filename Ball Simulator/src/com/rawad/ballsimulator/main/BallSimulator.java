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
import com.rawad.ballsimulator.gamestates.WorldEditorState;
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
		sm.addState(new WorldEditorState());
		
		sm.setState(StateEnum.MENU_STATE);
		
	}
	
	private void update(long timePassed) {
		
		background.update(timePassed);
		
		handleKeyboardInput();
		
		sm.update();
		
	}
	
	private void handleKeyboardInput() {
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_F3)) {
			debug = !debug;
			
		} else if(KeyboardInput.isKeyDown(KeyEvent.VK_F4)) {
			showSquares = !showSquares;
			
		} else if(KeyboardInput.isKeyDown(KeyEvent.VK_C)) {
			
			MouseInput.setClamped(!MouseInput.isClamped(), DisplayManager.getScreenWidth()/2, DisplayManager.getScreenHeight()/2);
			
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
		
	}
	
	private void renderDebugOverlay(Graphics2D g) {
		
		int screenWidth = DisplayManager.getScreenWidth();
		int screenHeight = DisplayManager.getScreenHeight();
		
		g.setColor(Color.GREEN);
		g.fillOval(screenWidth - 50, screenHeight - 50, 50, 50);
		
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
