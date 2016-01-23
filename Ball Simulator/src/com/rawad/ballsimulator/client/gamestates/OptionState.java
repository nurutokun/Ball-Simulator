package com.rawad.ballsimulator.client.gamestates;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.renderengine.BackgroundRender;

public class OptionState extends State {
	
	private JPanel mainCard;
	
	private DropDown resolutions;
	
	private Button btnMainMenu;
	private Button btnWorldEditor;
	
	private JTextField ipHolder;
	
	private CustomLoader loader;
	
	private SettingsFileParser settings;
	
	public OptionState() {
		super(EState.OPTION);
		
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected void initialize() {
		super.initialize();
		
		mainCard = new JPanel() {
			
			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = 5591364184528704906L;
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				BackgroundRender.instance().render((Graphics2D) g);
				
			}
			
		};
		
		resolutions = new DropDown("Fullscreen Resolution", DisplayManager.getFullScreenResolution(), 
				DisplayManager.getCompatibleDisplayModeResolutions());
		
		mainCard.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("257px"),
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("30px:grow"),
				RowSpec.decode("max(50dlu;pref)"),
				RowSpec.decode("max(10dlu;pref):grow"),
				RowSpec.decode("max(25dlu;pref)"),
				RowSpec.decode("35px:grow"),
				RowSpec.decode("22px"),
				RowSpec.decode("max(10dlu;pref):grow"),
				RowSpec.decode("max(25dlu;pref)"),
				RowSpec.decode("default:grow"),}));
		mainCard.add(resolutions, "2, 2, fill, fill");
		
		btnWorldEditor = new Button("World Editor");
		mainCard.add(btnWorldEditor, "2, 4, fill, fill");
		
		ipHolder = new JTextField("");
		mainCard.add(ipHolder, "2, 6, fill, fill");
		
		btnMainMenu = new Button("Main Menu");
		mainCard.add(btnMainMenu, "2, 8, fill, fill");
		
		container.add(mainCard, "Main Card");
		
	}
	
	@Override
	public void update() {
		super.update();
		
		if(!settings.getIp().equals(ipHolder.getText())) {
			settings.setIp(ipHolder.getText());
		}
		
		if(DisplayManager.isCloseRequested()) {
			loader.saveSettings(settings, sm.getGame().getSettingsFileName());
		}
		
		mainCard.repaint();
		
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
		
		loader.saveSettings(settings, sm.getGame().getSettingsFileName());
		
	}
	
	@Override
	protected void handleDropDownMenuSelect(DropDown drop) {
		
		switch(drop.getId()) {
		
		case "Fullscreen Resolution":
			
			String newRes = (String) drop.getSelectedItem();
			
			// Should save this...
			settings.setFullScreenResolution(newRes);
			
			DisplayManager.changeFullScreenResolution(newRes);
			
			break;
		
		}
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		Game game = sm.getGame();
		
		settings = game.getFileParser(SettingsFileParser.class);
		
		loader = game.getLoader(sm.getGame().toString());// Or could do BallSimulator.NAME
		
		loader.loadSettings(settings, game.getSettingsFileName());
		
		ipHolder.setText(settings.getIp());
		
		show("Main Card");
		
	}
	
}
