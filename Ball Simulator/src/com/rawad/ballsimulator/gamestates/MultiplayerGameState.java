package com.rawad.ballsimulator.gamestates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.gui.Messenger;
import com.rawad.ballsimulator.networking.ConnectionState;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.client.tcp.CPacket03Message;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gamestates.StateEnum;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.TextEdit;
import com.rawad.gamehelpers.gui.TextLabel;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.util.strings.DrawableString;

public class MultiplayerGameState extends State {
	
	private Client client;
	
	private Messenger mess;
	
	private PauseOverlay pauseScreen;
	
	public MultiplayerGameState(Client client) {
		super(StateEnum.MULTIPLAYERGAME_STATE);
		
		this.client = client;
		
		int screenWidth = DisplayManager.getScreenWidth();
		int screenHeight = DisplayManager.getScreenHeight();
		
		pauseScreen = new PauseOverlay(screenWidth, screenHeight);
		
		addOverlay(pauseScreen);
		
		int width = 128;
		int height = 128;
		
		int padding = 10;
		
		int x = padding + (width/2);
		int y = screenHeight - (height/2) - padding;
		
		mess = new Messenger("Mess", x, y, width, height);
//		mess.setUpdate(false);// Going to manually update
		
		addGuiComponent(mess);
		
	}
	
	@Override
	protected void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Resume":
			pauseScreen.setPaused(false);
			break;
		
		case "Main Menu":
			
			client.getNetworkManager().requestDisconnect();// TODO: If you do it quick enough, you can exit while the server thinks you 
			// are still logged on
			
			pauseScreen.setPaused(false);
			
			sm.setState(StateEnum.MENU_STATE);
			
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
					textObj.setCaretPosition(0, 0);// Because, reasons. (DrawableString.newLine() changing position automatically)
				}
					
			}
			
		}
		
		super.update(me, ke);
		super.updateOverlays(me, ke);
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_ESCAPE, true)) {
			pauseScreen.setPaused(!pauseScreen.isPaused());
		}
		
		client.pauseGame(pauseScreen.isPaused());
		
		client.update(ke, DisplayManager.getDeltaTime());
		
		if(DisplayManager.isCloseRequested()) {
			client.getNetworkManager().requestDisconnect();
		}
		
		ClientNetworkManager networkManager = client.getNetworkManager();
		
		if(networkManager.isDisconnectedFromServer()) {
			textOutput.setText("");
			client.getNetworkManager().setConnectionState(ConnectionState.NOT_CONNECTED);
			sm.setState(StateEnum.MENU_STATE);//TODO: or could show a sreen telling the client some debug info.
			
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
