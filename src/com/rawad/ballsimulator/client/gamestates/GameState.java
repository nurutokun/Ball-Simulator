package com.rawad.ballsimulator.client.gamestates;

import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.rawad.ballsimulator.client.Client;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;
import com.rawad.gamehelpers.input.EventHandler;

public class GameState extends State {
	
	// TODO: Shift entire BallSimulator game to here.
	
	private Client client;
	
	private PauseOverlay pauseScreen;
	
	private JPanel mainCard;
	
	private AbstractAction pauseAction;
	
	public GameState(Client client) {
		super(EState.GAME);
		
		this.client = client;
		
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected void initialize() {
		super.initialize();
		
		pauseScreen = new PauseOverlay();
		
		addOverlay(pauseScreen);
		
		addOverlay(client.getPlayerInventory());
		
		mainCard = new JPanel() {

			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = -4873917145932784820L;
			
			{
				setIgnoreRepaint(true);
			}
			
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				
				client.render(g, getWidth(), getHeight());
				
			}
			
		};
		
		mainCard.addKeyListener(EventHandler.instance());
		
		mainCard.setIgnoreRepaint(true);
		
		container.add(mainCard, "Main Card");
		
		initializeKeyBindings();
		
	}
	
	private void initializeKeyBindings() {
		
		InputMap input = mainCard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap action = mainCard.getActionMap();
		
		pauseAction = new AbstractAction() {
			
			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = 5131682721309115959L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pauseScreen.setActive(!pauseScreen.isActive());
			}
			
		};
		
		input.put(KeyStroke.getKeyStroke("ESCAPE"), "doPause");
		action.put("doPause", pauseAction);
		
		pauseScreen.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "doPause");
		pauseScreen.getActionMap().put("doPause", pauseAction);
		
	}
	
	@Override
	public void update() {
		super.update();
		
		if(pauseScreen.isActive()) {
			
			show(pauseScreen.getId());
			
			pauseScreen.setBackground(client.getViewport().getMasterRender().getBuffer());
			
			client.setPaused(true);
			client.setShowPauseScreen(true);
			
		} else if(client.getPlayerInventory().isActive()) {
			
			show(client.getPlayerInventory().getId());
			
			client.setPaused(true);
			client.setShowPauseScreen(false);
			
		} else {
			
			show("Main Card");
			
			client.setPaused(false);// TODO: Marker. Will probably need to be modified for inventory.
			client.setShowPauseScreen(false);
			
			mainCard.repaint();
			
		}
		
		client.update(GameManager.instance().getDeltaTime());
		
	}
	
	@Override
	public void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Resume":
			
			pauseScreen.setActive(false);
			
			break;
		
		case "Main Menu":
			
			sm.requestStateChange(EState.MENU);
			
			break;
			
		}
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		client.init("terrain");
		
		pauseScreen.setActive(false);
		
		show("Main Card");
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		client.onExit();
		
	}
	
}
