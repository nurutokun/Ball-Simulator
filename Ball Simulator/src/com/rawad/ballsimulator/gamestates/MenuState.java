package com.rawad.ballsimulator.gamestates;

import java.awt.Graphics2D;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.gui.Button;
import com.rawad.ballsimulator.gui.GuiComponent;
import com.rawad.ballsimulator.log.Logger;

public class MenuState extends State {
	
	int i = 0;
	
	public MenuState() {
		super(StateEnum.MENUSTATE);
		
		addGuiComponent(new Button("Click", DisplayManager.getWidth()/2 - 50, DisplayManager.getHeight()/2 - 40, 40, 80));
		addGuiComponent(new Button("Click 2", DisplayManager.getWidth()/2, DisplayManager.getHeight()/2, 80, 40));
		
	}
	
	public void update() {
		super.update();
		
	}
	
	protected void handleMouseClick(GuiComponent comp) {
		
		if("Click".equals(comp.getId())) {
			i++;
			
			Logger.log(Logger.DEBUG, "Cliiiccckkkk #: " + i);
		} else if("Click 2".equals(comp.getId())) {
			i++;
			
			Logger.log(Logger.DEBUG, "Cliiiccckkkk 2 #: " + i);
			
		}
		
	}
	
	public void render(Graphics2D g) {
		super.render(g);
		
		
		
	}
	
}
