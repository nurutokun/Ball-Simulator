package com.rawad.ballsimulator.gamestates;

import java.awt.Graphics2D;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.gui.Button;
import com.rawad.ballsimulator.log.Logger;

public class MenuState extends State {
	
	int i = 0;
	
	public MenuState() {
		super(StateEnum.MENUSTATE);
		
		addGuiComponent(new Button("Click", DisplayManager.getScreenWidth()/2, DisplayManager.getScreenHeight()/2 - 64));
		addGuiComponent(new Button("Click 2", DisplayManager.getScreenWidth()/2, DisplayManager.getScreenHeight()/2));
		addGuiComponent(new Button("Click 33333", 150, 200));
		
	}
	
	public void update() {
		super.update();
		
	}
	
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
			
		case "Click 33333":
			i++;
			Logger.log(Logger.DEBUG, "Cliiiccckkkk 3 #: " + i);
			break;
		
		}
		
	}
	
	public void render(Graphics2D g) {
		super.render(g);
		
		
		
	}
	
}
