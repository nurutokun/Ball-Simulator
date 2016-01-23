package com.rawad.ballsimulator.client.gamestates;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

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
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
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
		
		pauseScreen = new PauseOverlay();
		addOverlay(pauseScreen);
		
		addOverlay(client.getPlayerInventory());
		
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
		
		mainCard.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("center:pref"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:450px:grow"),},
			new RowSpec[] {
				RowSpec.decode("200px:grow"),
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
			
			sm.setState(EState.MENU);
			
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
		
		client.update(GameManager.instance().getDeltaTime());
		
		mainCard.repaint();
		
//		if(client.showPauseScreen()) {
//			cl.show(this, pauseScreen.getId());
//		} else {
//			cl.show(this, "Main Card");
//		}
		
		if(DisplayManager.isCloseRequested()) {
			client.getNetworkManager().requestDisconnect();
		}
		
		ClientNetworkManager networkManager = client.getNetworkManager();
		
		if(networkManager.isDisconnectedFromServer()) {
			mess.clearText();
			client.getNetworkManager().setConnectionState(ConnectionState.NOT_CONNECTED);
			sm.setState(EState.MENU);//TODO: or could show a sreen telling the client some debug info.
			
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
				
			}
			
		}).start();
		
		lblConnectingMessage.setText("Connecting To " + settings.getIp() + " ...");
		
		show(CONNECTING_CARD);
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		client.onExit();
		
	}
	
}
