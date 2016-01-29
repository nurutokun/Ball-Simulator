package com.rawad.ballsimulator.client.gamestates;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.ConnectionState;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.client.tcp.CPacket03Message;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.TextLabel;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;

public class MultiplayerGameState extends State {
	
	private static final String CONNECTING_CARD = "Connecting";
	
	private Client client;
	
	private JPanel mainCard;
	private JPanel connectingCard;
	
	private Messenger mess;
	
	private PauseOverlay pauseScreen;
	
	private SettingsFileParser settings;
	private TextLabel lblConnectingMessage;
	
	public MultiplayerGameState(Client client) {
		super(EState.MULTIPLAYER_GAME);
		
		this.client = client;
		
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
			private static final long serialVersionUID = 6136162253341289592L;
			
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				if(client.getNetworkManager().isConnectedToServer()) {
					
					client.render(g, getWidth(), getHeight());
					
				}
				
			}
			
		};

		pauseScreen = new PauseOverlay();
		
		InputMap input = mainCard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap action = mainCard.getActionMap();
		
		AbstractAction pauseAction = new AbstractAction() {
			
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
		
		addOverlay(pauseScreen);
		
		addOverlay(client.getPlayerInventory());
		
		mainCard.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:pref:grow"),},
			new RowSpec[] {
				RowSpec.decode("pref:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:pref"),}));
		
		mess = new Messenger();
		mainCard.add(mess, "1, 3, fill, fill");
		
		container.add(mainCard, "Main Card");
		
		connectingCard = new JPanel();
		
		connectingCard.setLayout(new BorderLayout(0, 0));
		
		lblConnectingMessage = new TextLabel("Connecting to Server...");
		lblConnectingMessage.setDrawBackground(false);
		
		connectingCard.add(lblConnectingMessage, BorderLayout.CENTER);
		
		container.add(connectingCard, CONNECTING_CARD);
		
		show(CONNECTING_CARD);
		
	}
	
	@Override
	protected void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Resume":
			
			show("Main Card");// TODO: Ensure we can only pause when connected.
			
			break;
		
		case "Main Menu":
			
			client.getNetworkManager().requestDisconnect();
			
			sm.requestStateChange(EState.MENU);
			
			break;
		}
		
	}
	
	@Override
	public void update() {
		super.update();
		
		String text = mess.getText();
		
		if(!"".equals(text)) {
			
			client.getNetworkManager().getConnectionManager().sendPacketToServer(
					new CPacket03Message(client.getPlayer().getName(), text));
			
			mess.addNewLine("You> " + text);
			
		}
		
		client.setPaused(pauseScreen.isActive());
		client.setShowPauseScreen(pauseScreen.isActive());
		
		client.update(GameManager.instance().getDeltaTime());

		if(pauseScreen.isActive()) {
			pauseScreen.setBackground(client.getViewport().getMasterRender().getBuffer());
		}
		
		mainCard.repaint();
		
		if(DisplayManager.isCloseRequested()) {
			client.getNetworkManager().requestDisconnect();
		}
		
		ClientNetworkManager networkManager = client.getNetworkManager();
		
		if(networkManager.isDisconnectedFromServer()) {
			mess.clearText();
			client.getNetworkManager().setConnectionState(ConnectionState.NOT_CONNECTED);
			sm.requestStateChange(EState.MENU);//TODO: or could show a sreen telling the client some debug info.
			
		} else if(networkManager.isConnectedToServer()) {
			
			text = client.getNetworkManager().getMessages();
			
			if(!text.isEmpty()) {
				mess.addNewLine(text);
			}
			
		}
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		Game game = sm.getGame();
		
		CustomLoader loader = game.getLoader(game.toString());
		
		settings = game.getFileParser(SettingsFileParser.class);
		
		loader.loadSettings(settings, game.getSettingsFileName());
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				client.connectToServer(settings.getIp());// TODO: Separate this.
				
				if(client.getNetworkManager().getConnectionState() == ConnectionState.CONNECTED) {
					MultiplayerGameState.this.show("Main Card");
				}
				
			}
			
		}, "MP Connection Thread").start();
		
		lblConnectingMessage.setText("Connecting To " + settings.getIp() + " ...");
		
		show(CONNECTING_CARD);
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		client.onExit();
		
	}
	
}
