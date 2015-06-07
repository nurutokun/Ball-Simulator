package com.rawad.ballsimulator.gamestates;

import java.awt.Graphics2D;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.gui.Button;
import com.rawad.ballsimulator.gui.TextLabel;
import com.rawad.ballsimulator.log.Logger;

public class MenuState extends State {
	
	int i = 0;
	
	public MenuState() {
		super(StateEnum.MENUSTATE);
		
		int screenSections = DisplayManager.getScreenHeight()/8;
		int centerScreen = DisplayManager.getScreenWidth()/2;
		
		addGuiComponent(new TextLabel("Ball Simulator v0.1", centerScreen, screenSections, 256, 64));
		
		addGuiComponent(new Button("Click", centerScreen, screenSections * 3 + 10));
		addGuiComponent(new Button("Click 2", centerScreen, screenSections * 4 + 20));
		addGuiComponent(new Button("Option Menu", centerScreen, screenSections * 5 + 30));
		addGuiComponent(new Button("Exit", centerScreen, screenSections * 6 + 40));
		
	}
	
	@Override
	protected void handleButtonClick(Button comp) {
		
		switch(comp.getId()) {
		
		case "Click":
			i++;
			Logger.log(Logger.DEBUG, "Cliiiccckkkk 1 #: " + i);
			break;
			
		case "Click 2":
			i++;
			Logger.log(Logger.DEBUG, "Cliiiccckkkk 2 #: " + i);
			break;
			
		case "Option Menu":
			sm.setState(StateEnum.OPTIONSTATE);
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
