package com.rawad.ballsimulator.client.gamestates;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rawad.ballsimulator.client.Client;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;
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
	public void update(MouseEvent me, KeyboardEvent ke) {
		super.update(me, ke);
		super.updateOverlays(me, ke);
		
		client.update(me, ke, GameManager.instance().getDeltaTime());
		
		pauseScreen.setPaused(client.showPauseScreen());
		
	}
	
	@Override
	public void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Resume":
			
			pauseScreen.setPaused(false);
			client.setShowPauseScreen(false);
			
			break;
		
		case "Main Menu":
			
			// No need to check if paused; pauseOverlay does that for you.
//			pauseScreen.setPaused(false);// For next time.
			client.setPaused(false);
			client.setShowPauseScreen(false);
			sm.setState(EState.MENU);
			
			break;
			
		}
		
	}
	
	@Override
	protected void onActivate() {
		client.init();
	}
	
	@Override
	protected void onDeactivate() {
		
		client.onExit();
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		client.render(g);
		
		if(client.showPauseScreen()) {
			pauseScreen.render(g);
		}
		
	}
	
}
