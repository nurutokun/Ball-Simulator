package com.rawad.ballsimulator.client.game_states;

import com.rawad.ballsimulator.file_parser.FileParser;
import com.rawad.ballsimulator.file_parser.file_types.MultiplayerSettings;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.game_states.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.gui.TextEdit;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;

public class OptionState extends State {
	
	private TextEdit ipHolder;
	
	private MultiplayerSettings mpSettings;
	
	public OptionState() {
		super(EState.OPTION);
		
		int verticalSections = DisplayManager.getScreenHeight()/7;
		int horizontalCenter = DisplayManager.getScreenWidth()/2;
		
		ipHolder = new TextEdit("", horizontalCenter, verticalSections * 4, 128, 16);
		
		addGuiComponent(new Button("Main Menu", horizontalCenter, verticalSections * 6));
		addGuiComponent(new DropDown("Fullscreen Resolution", horizontalCenter, verticalSections, 256,
				64, DisplayManager.getFullScreenResolution(), DisplayManager.getCompatibleDisplayModeResolutions()));
		addGuiComponent(new Button("World Editor", horizontalCenter, verticalSections * 2));
		addGuiComponent(ipHolder);
		
		ipHolder.setNewLineOnEnter(false);
		
		mpSettings = (MultiplayerSettings) FileParser.parseFile(MultiplayerSettings.FILE_NAME);
		
		ipHolder.setText(mpSettings.getIp());
		
	}
	
	@Override
	public void update(MouseEvent me, KeyboardEvent ke) {
		super.update(me, ke);
		
		if(ipHolder.wasPrevEdited()) {
			mpSettings.setIp(ipHolder.getText());
		}
		
		if(DisplayManager.isCloseRequested()) {
			mpSettings.saveFile();
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
		
		mpSettings.saveFile();
		
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
