package com.rawad.ballsimulator.gamestates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.gui.Button;
import com.rawad.ballsimulator.gui.Overlay;
import com.rawad.ballsimulator.input.KeyboardInput;

public class GameState extends State {
	
	private Client client;
	
	private Overlay gameOverScreen;
	
	private boolean gameOverScreenActive;
	
	public GameState() {
		super(StateEnum.GAMESTATE);
		
		client = new Client();
		
		initGameOverScreen();
		
		addOverlay(gameOverScreen);
		
		gameOverScreenActive = false;
		
	}
	
	private void initGameOverScreen() {
		
		int centerX = DisplayManager.getScreenWidth()/2;
		int verticalSections = DisplayManager.getScreenHeight()/7;
		
		gameOverScreen = new Overlay(Color.GRAY, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
		
		gameOverScreen.addComponent(new Button("Resume", centerX, verticalSections * 3));
		gameOverScreen.addComponent(new Button("Main Menu", centerX, verticalSections * 4));
		
	}
	
	@Override
	public void update(int x, int y, boolean mouseButtonDown) {
		super.update(x, y, mouseButtonDown);
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_ESCAPE)) {
			
			gameOverScreenActive = !gameOverScreenActive;
			
		}
		
//		if(!gameOverScreenActive) {
		client.update(DisplayManager.getDeltaTime());
//		}
		
		client.pauseGame(gameOverScreenActive);
			
	}
	
	@Override
	public void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Main Menu":
			
			if(gameOverScreenActive) {
				gameOverScreenActive = false;// For next time.
				sm.setState(StateEnum.MENUSTATE);
			}
			break;
			
		case "Resume" :
			
			gameOverScreenActive = false;
			
			break;
		
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		client.render(g);
		
		if(gameOverScreenActive) {
			gameOverScreen.render(g);
		}
		
	}
	
}
