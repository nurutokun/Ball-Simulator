package com.rawad.ballsimulator.gamestates;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.gui.Button;
import com.rawad.ballsimulator.gui.DropDown;

public class OptionState extends State {
	
	public OptionState() {
		super(StateEnum.OPTION_STATE);
		
		int verticalSections = DisplayManager.getScreenHeight()/7;
		int horizontalCenter = DisplayManager.getScreenWidth()/2;
		
		addGuiComponent(new Button("Main Menu", horizontalCenter, verticalSections * 6));
		addGuiComponent(new DropDown("Fullscreen Resolution", horizontalCenter, verticalSections, 256,
				64, DisplayManager.getFullScreenResolution(), DisplayManager.getCompatibleDisplayModeResolutions()));
		addGuiComponent(new Button("World Editor", horizontalCenter, verticalSections * 2));
		
	}
	
	@Override
	protected void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Main Menu":
			sm.setState(StateEnum.MENU_STATE);
			break;
			
		case "World Editor":
			sm.setState(StateEnum.WORLDEDITOR_STATE);
			break;
		
		}
		
	}
	
	@Override
	protected void handleDropDownMenuSelect(DropDown drop) {
		
		switch(drop.getId()) {
		
		case "Fullscreen Resolution":
			DisplayManager.changeFullScreenResolution(drop.getCurrentSelectedItem());
			break;
		
		}
		
	}
	
}
