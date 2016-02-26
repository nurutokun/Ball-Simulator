package com.rawad.ballsimulator.client.gamestates;

import java.awt.AWTKeyStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.TextLabel;
import com.rawad.gamehelpers.renderengine.BackgroundRender;

public class MenuState extends State {
	
	private JPanel mainCard;
	
	private TextLabel lblTitle;
	
	private Button btnSingleplayer;
	private Button btnMultiplayer;
	private Button btnOptions;
	private Button btnExit;
	
	public MenuState() {
		super(EState.MENU);
		
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
			private static final long serialVersionUID = -1849858916865809369L;
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				BackgroundRender.instance().render((Graphics2D) g);
				
			}
			
		};
		
		mainCard.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("center:default:grow"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("pref:grow"),
				FormSpecs.PREF_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				ColumnSpec.decode("center:pref:grow"),
				ColumnSpec.decode("center:default:grow"),},
			new RowSpec[] {
				RowSpec.decode("pref:grow"),
				RowSpec.decode("fill:pref"),
				RowSpec.decode("pref:grow"),
				RowSpec.decode("fill:pref"),
				RowSpec.decode("5dlu"),
				RowSpec.decode("fill:pref"),
				RowSpec.decode("5dlu"),
				RowSpec.decode("fill:pref"),
				RowSpec.decode("5dlu"),
				RowSpec.decode("fill:pref"),
				RowSpec.decode("5dlu:grow"),}));
		
		lblTitle = new TextLabel(GameManager.instance().getCurrentGame().toString());
		mainCard.add(lblTitle, "3, 2, 3, 1, fill, fill");
		
		btnSingleplayer = new Button("Singleplayer");
		mainCard.add(btnSingleplayer, "4, 4, fill, fill");
		
		btnMultiplayer = new Button("Multiplayer");
		mainCard.add(btnMultiplayer, "4, 6, fill, fill");
		
		btnOptions = new Button("Option Menu");
		mainCard.add(btnOptions, "4, 8, fill, fill");
		
		btnExit = new Button("Exit");
		mainCard.add(btnExit, "4, 10, fill, fill");
		
		Set<AWTKeyStroke> forwardTraversalKeys = new HashSet<AWTKeyStroke>();
		Set<AWTKeyStroke> backwardTraversalKeys = new HashSet<AWTKeyStroke>();
		
		forwardTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false));
		backwardTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false));
		
		mainCard.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardTraversalKeys);
		mainCard.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardTraversalKeys);
		
		container.add(mainCard, "Main Card");
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		show("Main Card");
		
	}
	
	@Override
	protected void handleButtonClick(Button comp) {
		
		switch (comp.getId()) {
		
		case "Singleplayer":
			sm.requestStateChange(EState.GAME);
			break;
		case "Multiplayer":
			sm.requestStateChange(EState.MULTIPLAYER_GAME);
			break;
		case "Option Menu":
			sm.requestStateChange(EState.OPTION);
			break;
		case "Exit":
			sm.getGame().requestStop();
			break;
		}
		
	}
	
}
