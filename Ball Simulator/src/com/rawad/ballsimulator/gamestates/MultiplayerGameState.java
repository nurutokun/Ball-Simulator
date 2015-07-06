package com.rawad.ballsimulator.gamestates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.networking.ConnectionState;
import com.rawad.gamehelpers.displaymanager.DisplayManager;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gamestates.StateEnum;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;
import com.rawad.gamehelpers.input.KeyboardInput;

public class MultiplayerGameState extends State {
	
	private Client client;
	
	private PauseOverlay pauseScreen;
	
	public MultiplayerGameState(Client client) {
		super(StateEnum.MULTIPLAYERGAME_STATE);
		
		this.client = client;
		
		pauseScreen = new PauseOverlay(DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
		
		addOverlay(pauseScreen);
		
	}
	
	@Override
	protected void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Resume":
			pauseScreen.setPaused(false);
			break;
		
		case "Main Menu":
			
			client.getNetworkManager().requestDisconnect();
			
			pauseScreen.setPaused(false);
			
			sm.setState(StateEnum.MENU_STATE);
			
			break;
		
		}
		
	}
	
	@Override
	public void update() {
		super.update();
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_ESCAPE)) {
			pauseScreen.setPaused(!pauseScreen.isPaused());
		}
		
		client.pauseGame(pauseScreen.isPaused());
		
		client.update(DisplayManager.getDeltaTime());
		
		if(DisplayManager.isCloseRequested()) {
			client.getNetworkManager().requestDisconnect();
		}
		
		if(client.getNetworkManager().isDisconnectedFromServer()) {
			client.getNetworkManager().setConnectionState(ConnectionState.NOT_CONNECTED);
			sm.setState(StateEnum.MENU_STATE);//TODO: or could show a sreen telling the client some debug info.
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		if(client.getNetworkManager().isConnectedToServer()) {
			try {
			client.render(g);
			} catch(Exception ex) {}
		} else {
			g.setColor(Color.RED);
			g.drawString("Connecting...", DisplayManager.getScreenWidth()/2, DisplayManager.getScreenHeight()/2);
		}
		
		if(pauseScreen.isPaused()) {
			pauseScreen.render(g);
		}
		
	}
	
	@Override
	protected void onActivate() {
		client.connectToServer("localhost");
	}
	
}
