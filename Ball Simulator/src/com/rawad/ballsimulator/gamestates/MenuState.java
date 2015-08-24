package com.rawad.ballsimulator.gamestates;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gamestates.StateEnum;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.TextLabel;

public class MenuState extends State {
	
	public MenuState() {
		super(StateEnum.MENU_STATE);
		
		int screenSections = DisplayManager.getScreenHeight()/8;
		int centerScreen = DisplayManager.getScreenWidth()/2;
		
		addGuiComponent(new TextLabel(GameManager.getGame().toString(), centerScreen, screenSections, 256, 64));
		
		addGuiComponent(new Button("Singleplayer", centerScreen, screenSections * 3 + 10));
		addGuiComponent(new Button("Multiplayer", centerScreen, screenSections * 4 + 20));
		addGuiComponent(new Button("Option Menu", centerScreen, screenSections * 5 + 30));
		addGuiComponent(new Button("Exit", centerScreen, screenSections * 6 + 40));
		
	}
	
	@Override
	protected void handleButtonClick(Button comp) {
		
		switch(comp.getId()) {
		
		case "Singleplayer":
			sm.setState(StateEnum.GAME_STATE);
			break;
			
		case "Multiplayer":
			sm.setState(StateEnum.MULTIPLAYERGAME_STATE);
			break;
			
		case "Option Menu":
			sm.setState(StateEnum.OPTION_STATE);
			break;
		
		case "Exit":
			DisplayManager.requestClose();
			break;
			
		}
		
	}
	
}
