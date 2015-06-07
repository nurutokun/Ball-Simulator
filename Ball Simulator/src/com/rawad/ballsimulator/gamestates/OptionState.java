package com.rawad.ballsimulator.gamestates;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.gui.Button;
import com.rawad.ballsimulator.gui.DropDown;

public class OptionState extends State {
	
	public OptionState() {
		super(StateEnum.OPTIONSTATE);
		
		addGuiComponent(new Button("Main Menu", DisplayManager.getScreenWidth()/2, DisplayManager.getScreenHeight() - 64));
		addGuiComponent(new DropDown("Fullscreen Resolution", DisplayManager.getScreenWidth()/2, 128));
		
	}
	
	@Override
	protected void handleButtonClick(Button comp) {
		
		switch(comp.getId()) {
		
		case "Main Menu":
			sm.setState(StateEnum.MENUSTATE);
			break;
		
		}
		
	}
}
