package com.rawad.ballsimulator.client.gamestates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.files.SettingsLoader;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.ballsimulator.networking.ConnectionState;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.client.tcp.CPacket03Message;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.TextEdit;
import com.rawad.gamehelpers.gui.TextLabel;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.utils.Util;
import com.rawad.gamehelpers.utils.strings.DrawableString;

public class MultiplayerGameState extends State {
	
	private Client client;
	
	private Messenger mess;
	
	private PauseOverlay pauseScreen;
	
	private DrawableString debugMessage;
	
	private SettingsLoader settings;
	
	public MultiplayerGameState(Client client) {
		super(EState.MULTIPLAYER_GAME);
		
		this.client = client;
		
		int screenWidth = Game.SCREEN_WIDTH;
		int screenHeight = Game.SCREEN_HEIGHT;
		
		pauseScreen = new PauseOverlay(screenWidth, screenHeight);
		
		addOverlay(pauseScreen);
		
		int width = 128;
		int height = 128;
		
		int padding = 10;
		
		int x = padding + (width/2);
		int y = screenHeight - (height/2) - padding;
		
		mess = new Messenger("Mess", x, y, width, height);
		
		addGuiComponent(mess);
		
		debugMessage = new DrawableString();
		
	}
	
	@Override
	protected void handleButtonClick(Button butt) {
		
		mess.handleButtonClick(butt);
		
		switch(butt.getId()) {
		
		case "Resume":
			pauseScreen.setPaused(false);
			break;
		
		case "Main Menu":
			
			client.getNetworkManager().requestDisconnect();
			
			pauseScreen.setPaused(false);
			
			sm.setState(EState.MENU);
			
			break;
		}
		
	}
	
	@Override
	public void update(MouseEvent me, KeyboardEvent ke) {
		
		TextEdit textInput = mess.getInputField();
		TextLabel textOutput = mess.getOutputField();
		
		// If the textEditor is focused and shift isn't being held down while enter is, get the content and send the message
		if(textInput.isFocused()) {
			
			if(!KeyboardInput.isKeyDown(KeyEvent.VK_SHIFT, false) && KeyboardInput.isKeyDown(KeyEvent.VK_ENTER)) {
				
				if(!textInput.getText().isEmpty()) {
					
					String text = textInput.getText();// Should also use this to display on another GuiComponent
					
					client.getNetworkManager().getConnectionManager().sendPacketToServer(
							new CPacket03Message(client.getPlayer().getName(), text));
					
					textOutput.addNewLine("You> " + text);
					
					DrawableString textObj = textInput.getTextObject();
					
					textObj.setContent("");
					textObj.setCaretPosition(0, 0);// Because, reasons. (DrawableString.newLine() changing position
					// automatically)
				}
					
			}
			
		}
		
		super.update(me, ke);
		super.updateOverlays(me, ke);
		
		client.update(me, ke, GameManager.instance().getDeltaTime());
		
		pauseScreen.setPaused(client.showPauseScreen());
		
		if(DisplayManager.isCloseRequested()) {
			client.getNetworkManager().requestDisconnect();
		}
		
		ClientNetworkManager networkManager = client.getNetworkManager();
		
		if(networkManager.isDisconnectedFromServer()) {
			textOutput.setText("");
			client.getNetworkManager().setConnectionState(ConnectionState.NOT_CONNECTED);
			sm.setState(EState.MENU);//TODO: or could show a sreen telling the client some debug info.
			
		} else if(networkManager.isConnectedToServer()) {
			
			String text = client.getNetworkManager().getMessages();
			
			if(!text.isEmpty()) {
				textOutput.addNewLine(text);
			}
			
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		if(client.getNetworkManager().isConnectedToServer()) {
			
			client.render(g);
			
			super.render(g);// For GUI stuff.
			
		} else {
			
			String message = "Connecting to \"" + settings.getIp() + "\"...";
			debugMessage.setContent(message);
			
			debugMessage.render(g, Color.RED, Util.TRANSPARENT, Util.TRANSPARENT, 
					new Rectangle(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT), true, false);
			
		}
		
		if(pauseScreen.isPaused()) {
//			pauseScreen.render(g);
		}
		
	}
	
	@Override
	protected void onActivate() {
		
		Loader.loadSettings(sm.getGame(), "settings");
		
		settings = (SettingsLoader) sm.getGame().getFiles().get(SettingsLoader.class);
		
		client.connectToServer(settings.getIp());
	}
	
	@Override
	protected void onDeactivate() {
		
		client.onExit();
		
	}
	
}
