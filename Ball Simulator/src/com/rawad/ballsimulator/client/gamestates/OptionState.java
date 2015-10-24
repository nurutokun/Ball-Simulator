package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.files.SettingsLoader;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.gui.TextEdit;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;

public class OptionState extends State {
	
	private TextEdit ipHolder;
	
	private SettingsLoader settings;
	
	public OptionState() {
		super(EState.OPTION);
		
		int verticalSections = DisplayManager.getScreenHeight()/7;
		int horizontalCenter = DisplayManager.getScreenWidth()/2;
		
		ipHolder = new TextEdit("", horizontalCenter, verticalSections * 4, 128, 16);
		
		addGuiComponent(new Button("Main Menu", horizontalCenter, verticalSections * 6));
		addGuiComponent(new Button("World Editor", horizontalCenter, verticalSections * 2));
		addGuiComponent(ipHolder);
		addGuiComponent(new DropDown("Fullscreen Resolution", horizontalCenter, verticalSections, 256,
				64, DisplayManager.getFullScreenResolution(), DisplayManager.getCompatibleDisplayModeResolutions()));
		
		ipHolder.setNewLineOnEnter(false);
		
	}
	
	@Override
	public void update(MouseEvent me, KeyboardEvent ke) {
		super.update(me, ke);
		
		if(ipHolder.wasPrevEdited()) {
			settings.setIp(ipHolder.getText());
		}
		
		if(DisplayManager.isCloseRequested()) {
			Loader.saveFile(settings, "settings");
		}
		
	}
	
	@Override
	protected void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Main Menu":
			sm.setState(EState.MENU);
			break;
			
		case "World Editor":
			sm.setState(EState.WORLD_EDITOR);
			break;
		
		}
		
		Loader.saveFile(settings, "settings");
		
	}
	
	@Override
	protected void handleDropDownMenuSelect(DropDown drop) {
		
		switch(drop.getId()) {
		
		case "Fullscreen Resolution":
			DisplayManager.changeFullScreenResolution(drop.getCurrentSelectedItem());
			break;
		
		}
		
	}
	
	@Override
	protected void onActivate() {
		
		settings = (SettingsLoader) sm.getGame().getFiles().get(SettingsLoader.class);
		
		Loader.loadSettings(sm.getGame(), "settings");
		
		ipHolder.setText(settings.getIp());
		
	}
	
}
