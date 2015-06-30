package com.rawad.ballsimulator.gamestates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.gui.Button;
import com.rawad.ballsimulator.gui.overlay.PauseOverlay;
import com.rawad.ballsimulator.input.KeyboardInput;
import com.rawad.ballsimulator.input.MouseEvent;

public class GameState extends State {
	
	private Client client;
	
	private PauseOverlay pauseScreen;
	
	public GameState() {
		super(StateEnum.GAME_STATE);
		
		client = new Client();
		
		pauseScreen = new PauseOverlay(Color.GRAY, 0, 0);
		
		addOverlay(pauseScreen);
		
	}
	
	@Override
	public void update(MouseEvent e) {
		super.update(e);
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_ESCAPE)) {
			
			pauseScreen.setPaused(!pauseScreen.isPaused());
			
		}
		
		client.update(DisplayManager.getDeltaTime());
		
		client.pauseGame(pauseScreen.isPaused());
		
	}
	
	@Override
	public void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Resume":
			
			pauseScreen.setPaused(false);
			
			break;
		
		case "Main Menu":
			
			// No need to check if paused; pauseOverlay does that for you.
			pauseScreen.setPaused(false);// For next time.
			sm.setState(StateEnum.MENU_STATE);
			
			break;
			
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		client.render(g);
		
		if(pauseScreen.isPaused()) {
			pauseScreen.render(g);
		}
		
	}
	
}
