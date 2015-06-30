package com.rawad.ballsimulator.gamestates;

import java.awt.Graphics2D;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.gui.Button;
import com.rawad.ballsimulator.gui.TextLabel;
import com.rawad.ballsimulator.log.Logger;
import com.rawad.ballsimulator.main.BallSimulator;

public class MenuState extends State {
	
	public MenuState() {
		super(StateEnum.MENU_STATE);
		
		int screenSections = DisplayManager.getScreenHeight()/8;
		int centerScreen = DisplayManager.getScreenWidth()/2;
		
		addGuiComponent(new TextLabel(BallSimulator.NAME, centerScreen, screenSections, 256, 64));
		
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
			Logger.log(Logger.DEBUG, "Multiplayer");
			break;
			
		case "Option Menu":
			sm.setState(StateEnum.OPTION_STATE);
			break;
		
		case "Exit":
			DisplayManager.destroyWindow();
			break;
			
		}
		
	}
	
	public void render(Graphics2D g) {
		super.render(g);
		
		
		
	}
	
}
