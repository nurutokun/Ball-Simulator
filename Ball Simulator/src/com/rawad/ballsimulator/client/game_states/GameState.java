package com.rawad.ballsimulator.client.game_states;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.rawad.ballsimulator.client.Client;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.game_states.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;

public class GameState extends State {
	
	private Client client;
	
	private PauseOverlay pauseScreen;
	
	public GameState(Client client) {
		super(EState.GAME);
		
		this.client = client;
		
		pauseScreen = new PauseOverlay(Color.GRAY, 0, 0);
		
		addOverlay(pauseScreen);
		
	}
	
	@Override
	public void update(MouseEvent e, KeyboardEvent ke) {
		super.update(e, ke);
		super.updateOverlays(e, ke);
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_ESCAPE, true)) {
			
			pauseScreen.setPaused(!pauseScreen.isPaused());
			
		}
		
		client.pauseGame(pauseScreen.isPaused());
		
		client.update(ke, DisplayManager.getDeltaTime());
		
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
			sm.setState(EState.MENU);
			
			break;
			
		}
		
	}
	
	@Override
	protected void onActivate() {
		client.init();
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
