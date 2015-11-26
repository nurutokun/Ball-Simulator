package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.files.SettingsLoader;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.gui.TextEdit;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;

public class OptionState extends State {
	
	private DropDown resolutions;
	
	private TextEdit ipHolder;
	
	private SettingsLoader settings;
	
	public OptionState() {
		super(EState.OPTION);
		
		int verticalSections = Game.SCREEN_HEIGHT/7;
		int horizontalCenter = Game.SCREEN_WIDTH/2;
		
		ipHolder = new TextEdit("", horizontalCenter, verticalSections * 4, 128, 16);
		resolutions = new DropDown("Fullscreen Resolution", horizontalCenter, verticalSections, 256,
				64, DisplayManager.getFullScreenResolution(), DisplayManager.getCompatibleDisplayModeResolutions());
		
		addGuiComponent(new Button("Main Menu", horizontalCenter, verticalSections * 6));
		addGuiComponent(new Button("World Editor", horizontalCenter, verticalSections * 2));
		addGuiComponent(ipHolder);
		addGuiComponent(resolutions);
		
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
			
			String newRes = drop.getCurrentSelectedItem();
			
			DisplayManager.changeFullScreenResolution(newRes);
			
			// Should save this...
			settings.setFullScreenResolution(newRes);
			
			if(DisplayManager.getDisplayMode() == DisplayManager.Mode.FULLSCREEN) {
				// Refresh fullscreen resolution when in fullscreen mode
				
				// Temporary. Whole thing should be part of DisplayManager.changeFullScreenResolution();
				DisplayManager.setDisplayMode(DisplayManager.Mode.WINDOWED, sm.getGame().getMasterRender());
				DisplayManager.setDisplayMode(DisplayManager.Mode.FULLSCREEN, sm.getGame().getMasterRender());
				
			}
			
			break;
		
		}
		
	}
	
	@Override
	protected void onActivate() {
		
		settings = (SettingsLoader) sm.getGame().getFiles().get(SettingsLoader.class);
		
		Loader.loadSettings(sm.getGame(), sm.getGame().getSettingsFileName());
		
		ipHolder.setText(settings.getIp());
		
	}
	
}
