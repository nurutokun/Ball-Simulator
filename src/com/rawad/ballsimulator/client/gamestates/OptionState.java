package com.rawad.ballsimulator.client.gamestates;

import java.awt.AWTKeyStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.game.Game;
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
		
		mainCard.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("pref:grow"),
				ColumnSpec.decode("min:grow"),
				FormSpecs.PREF_COLSPEC,
				ColumnSpec.decode("min:grow"),
				ColumnSpec.decode("pref:grow"),
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("30dlu:grow"),
				RowSpec.decode("max(50dlu;pref)"),
				RowSpec.decode("max(10dlu;pref):grow"),
				RowSpec.decode("max(25dlu;pref)"),
				RowSpec.decode("35dlu:grow"),
				RowSpec.decode("20dlu"),
				RowSpec.decode("max(10dlu;pref):grow"),
				RowSpec.decode("max(25dlu;pref)"),
				RowSpec.decode("default:grow"),}));
		
		resolutions = new DropDown("Fullscreen Resolution", DisplayManager.getFullScreenResolution(), 
				DisplayManager.getCompatibleDisplayModeResolutions());
		mainCard.add(resolutions, "3, 2, 3, 1, fill, fill");
		
		btnWorldEditor = new Button("World Editor");
		mainCard.add(btnWorldEditor, "4, 4, fill, fill");
		
		ipHolder = new JTextField("");
		mainCard.add(ipHolder, "3, 6, 3, 1, fill, fill");
		
		btnMainMenu = new Button("Main Menu");
		mainCard.add(btnMainMenu, "4, 8, fill, fill");
		
		Set<AWTKeyStroke> forwardTraversalKeys = new HashSet<AWTKeyStroke>();
		Set<AWTKeyStroke> backwardTraversalKeys = new HashSet<AWTKeyStroke>();
		
		forwardTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false));
		backwardTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false));
		
		mainCard.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardTraversalKeys);
		mainCard.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardTraversalKeys);
		
		container.add(mainCard, "Main Card");
		
	}
	
	@Override
	public void update() {
		super.update();
		
		if(!settings.getIp().equals(ipHolder.getText())) {
			settings.setIp(ipHolder.getText());
		}
		
	}
	
	@Override
	protected void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Main Menu":
			sm.requestStateChange(EState.MENU);
			break;
			
		case "World Editor":
			sm.requestStateChange(EState.WORLD_EDITOR);
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
		
		loader = game.getLoader(CustomLoader.class);
		
		loader.loadSettings(settings, game.getSettingsFileName());
		
		ipHolder.setText(settings.getIp());
		
		show("Main Card");
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		loader.saveSettings(settings, sm.getGame().getSettingsFileName());
		
	}
	
}
