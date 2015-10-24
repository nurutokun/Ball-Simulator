package com.rawad.ballsimulator.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gamestates.EState;
import com.rawad.ballsimulator.client.gamestates.GameState;
import com.rawad.ballsimulator.client.gamestates.MenuState;
import com.rawad.ballsimulator.client.gamestates.MultiplayerGameState;
import com.rawad.ballsimulator.client.gamestates.OptionState;
import com.rawad.ballsimulator.client.gamestates.WorldEditorState;
import com.rawad.ballsimulator.files.SettingsLoader;
import com.rawad.ballsimulator.files.TerrainLoader;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gui.Background;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.resources.ResourceManager;

public class BallSimulator extends Game {
	
	/**
	 * Temporary
	 */
	public static final String NAME = "Ball Simulator";
	
	private static final int ICON;
	
	private Client client;
	
	private Background background;
	
	private boolean showSquares;
	 
	public BallSimulator() {
		super();
		
		files.put(TerrainLoader.class, new TerrainLoader());// All of a sudden, I feel like changing the file-loading for
		// terrains at least...
		
	}
	
	static {
		
		ICON = Loader.loadTexture("", "game_icon");
		
	}
	
	public void init() {
		super.init();
		
		files.put(SettingsLoader.class, new SettingsLoader());
		
		client = new Client();
		
		background = new Background();
		
		sm.addState(new MenuState());
		sm.addState(new GameState(client));
		sm.addState(new OptionState());
		sm.addState(new WorldEditorState());
		sm.addState(new MultiplayerGameState(client));
		
		sm.setState(EState.MENU);
		
		showSquares = false;
		
	}
	
	@Override
	public void update(long timePassed) {
		
		background.update(timePassed);
		
		handleKeyboardInput();
		
		sm.update();
		
	}
	
	private void handleKeyboardInput() {
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_F3, true)) {
			debug = !debug;
			
		} else if(KeyboardInput.isKeyDown(KeyEvent.VK_F4, true)) {
			showSquares = !showSquares;
			
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
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
		
		g.setColor(Color.WHITE);
		g.drawString(Runtime.getRuntime().freeMemory() + "", 10, 30);
		
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
	
	@Override
	public BufferedImage getIcon() {
		return ResourceManager.getTexture(ICON);
	}
	
	@Override
	public String toString() {
		return NAME;
	}
	
}
