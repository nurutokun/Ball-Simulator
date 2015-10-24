package com.rawad.ballsimulator.client.gamestates;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.TextLabel;

public class MenuState extends State {
	
	public MenuState() {
		super(EState.MENU);
		
		int screenSections = DisplayManager.getScreenHeight()/8;
		int centerScreen = DisplayManager.getScreenWidth()/2;
		
		addGuiComponent(new TextLabel(GameManager.instance().getCurrentGame().toString(), centerScreen, screenSections, 256, 64));
		
		addGuiComponent(new Button("Singleplayer", centerScreen, screenSections * 3 + 10));
		addGuiComponent(new Button("Multiplayer", centerScreen, screenSections * 4 + 20));
		addGuiComponent(new Button("Option Menu", centerScreen, screenSections * 5 + 30));
		addGuiComponent(new Button("Exit", centerScreen, screenSections * 6 + 40));
		
	}
	
	@Override
	protected void handleButtonClick(Button comp) {
		
		switch(comp.getId()) {
		
		case "Singleplayer":
			sm.setState(EState.GAME);
			break;
			
		case "Multiplayer":
			sm.setState(EState.MULTIPLAYER_GAME);
			break;
			
		case "Option Menu":
			sm.setState(EState.OPTION);
			break;
		
		case "Exit":
			DisplayManager.requestClose();
			break;
			
		}
		
	}
	
}
