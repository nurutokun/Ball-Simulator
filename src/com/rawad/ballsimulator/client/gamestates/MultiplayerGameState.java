package com.rawad.ballsimulator.client.gamestates;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

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
import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.client.tcp.CPacket03Message;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.IController;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.TextLabel;
import com.rawad.gamehelpers.gui.overlay.Overlay;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;
import com.rawad.gamehelpers.input.EventHandler;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.log.Logger;

public class MultiplayerGameState extends State implements IController {
	
	private static final String CONNECTING_CARD = "Connecting";
	
	private JPanel mainCard;
	
	private Messenger mess;
	
	private Overlay connectingCard;
	private PauseOverlay pauseScreen;
	
	private CustomLoader loader;
	private TerrainFileParser terrainParser;
	private SettingsFileParser settingsParser;
	
	private TextLabel lblConnectingMessage;
	
	private ClientNetworkManager networkManager;
	
	private Viewport viewport;
	
	private World world;
	private Camera camera;
	
	private EntityPlayer player;
	private PlayerInventory inventory;
	
	public MultiplayerGameState(ClientNetworkManager networkManager) {
		super(EState.MULTIPLAYER_GAME);
		
		this.networkManager = networkManager;
		networkManager.setController(this);
		
		viewport = new Viewport();
		
		world = new World();
		
		camera = new Camera();
		camera.setScale(1d/2d, 1d/2d);
		
		player = new EntityPlayer(world);
		player.setName("Player" + (int) (new Random().nextDouble()*999));
		
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected void initialize() {
		super.initialize();
		
		pauseScreen = new PauseOverlay();
		
		addOverlay(pauseScreen);
		
		inventory = new PlayerInventory();
		
		addOverlay(inventory);
		
		mainCard = new JPanel() {

			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = 6136162253341289592L;
			
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				if(networkManager.isLoggedIn()) {
					viewport.drawScene(g, getWidth(), getHeight());
					
				}
				
			}
			
			@Override
			public void transferFocus() {
				container.transferFocus();
			}
			
		};
		
		EventHandler l = EventHandler.instance();
		
		mainCard.addKeyListener(l);
		mainCard.addMouseListener(l);
		mainCard.addMouseMotionListener(l);
		mainCard.addMouseWheelListener(l);
		mainCard.addComponentListener(l);
		
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
		
		connectingCard = new Overlay(CONNECTING_CARD);
		
		connectingCard.setLayout(new BorderLayout(0, 0));
		
		lblConnectingMessage = new TextLabel("Connecting to Server...");
		lblConnectingMessage.setDrawBackground(false);
		
		connectingCard.add(lblConnectingMessage, BorderLayout.CENTER);
		
		container.add(connectingCard, CONNECTING_CARD);
		
		initializeKeyBindings();
		
	}
	
	private void initializeKeyBindings() {
		
		InputMap input = mainCard.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap action = mainCard.getActionMap();
		
		input.put(KeyStroke.getKeyStroke("ENTER"), "enterMess");
		input.put(KeyStroke.getKeyStroke("released T"), "enterMess");// Released so that "T" isn't typed.
		
		action.put("enterMess", new AbstractAction() {
			
			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = 5131682721309115959L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mess.requestFocusInWindow();
			}
			
		});
		
		input.put(KeyStroke.getKeyStroke("ESCAPE"), pauseScreen.getId());
		action.put(pauseScreen.getId(), pauseScreen.getActiveChanger());
		
		pauseScreen.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), 
				pauseScreen.getId());
		pauseScreen.getActionMap().put(pauseScreen.getId(), pauseScreen.getActiveChanger());
		
		input.put(KeyStroke.getKeyStroke("E"), inventory.getId());
		action.put(inventory.getId(), inventory.getActiveChanger());
		
		inventory.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("E"), 
				inventory.getId());
		inventory.getActionMap().put(inventory.getId(), inventory.getActiveChanger());
		
	}
	
	@Override
	public void tick() {
		
		if(networkManager.isLoggedIn()) {// Start updating world, player and camera once login is successful
			
			if(!pauseScreen.isActive()) {
				
				handleKeyboardInput();// TODO: Doesn't move with keyboard...
				handleMouseInput();
				
			}
			
			camera.update(player.getX(), player.getY(), world.getWidth(), world.getHeight(), 0, 0, 
					Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
			
			player.update();
			world.update();
			
			viewport.update(world, camera);
			
		}
		
	}
	
	@Override
	public void handleMouseInput() {
		
	}
	
	@Override
	public void handleKeyboardInput() {
		
		KeyboardInput.setConsumeAfterRequest(false);
		
		boolean up = KeyboardInput.isKeyDown(KeyEvent.VK_UP) | KeyboardInput.isKeyDown(KeyEvent.VK_W);
		boolean down = KeyboardInput.isKeyDown(KeyEvent.VK_DOWN) | KeyboardInput.isKeyDown(KeyEvent.VK_S);
		boolean right = KeyboardInput.isKeyDown(KeyEvent.VK_RIGHT) | KeyboardInput.isKeyDown(KeyEvent.VK_D);
		boolean left = KeyboardInput.isKeyDown(KeyEvent.VK_LEFT) | KeyboardInput.isKeyDown(KeyEvent.VK_A);
		
		KeyboardInput.setConsumeAfterRequest(true);
		
		networkManager.updatePlayerMovement(up, down, right, left);
		
//		if(up) {
//			player.moveUp();
//		} else if(down) {
//			player.moveDown();
//		}
//		
//		if(right) {
//			player.moveRight();
//		} else if(left) {
//			player.moveLeft();
//		}
		
	}
	
	@Override
	public void update() {
		super.update();
		
		String text = mess.getText();
		
		if(!"".equals(text)) {
			
			networkManager.getConnectionManager().sendPacketToServer(new CPacket03Message(player.getName(), text));
			
			mess.addNewLine("You> " + text);
			
		}
		
		viewport.render();
		
		if(networkManager.isConnectedToServer()) {
			
			text = networkManager.getMessages();
			
			if(!text.isEmpty()) {
				mess.addNewLine(text);
			}
			
			if(pauseScreen.isActive()) {
				
				pauseScreen.setBackground(viewport.getMasterRender().getBuffer());
				show(pauseScreen.getId());
				
			} else if(inventory.isActive()) {
				
				inventory.setBackground(viewport.getMasterRender().getBuffer());
				show(inventory.getId());
				
			} else {
				
				show("Main Card");
				
			}
			
			
		}
		
	}
	
	@Override
	protected void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Resume":
			
			pauseScreen.setActive(false);
			
			break;
		
		case "Main Menu":
			
//			networkManager.requestDisconnect();
			
			sm.requestStateChange(EState.MENU);
			
			break;
		}
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		Game game = sm.getGame();
		
		game.getProxy().setController(this);
		
		loader = game.getLoader(CustomLoader.class);
		
		terrainParser = game.getFileParser(TerrainFileParser.class);
		settingsParser = game.getFileParser(SettingsFileParser.class);
		
		loader.loadSettings(settingsParser, game.getSettingsFileName());
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				networkManager.init(settingsParser.getIp());// TODO: Separate this.
				
				if(networkManager.isConnectedToServer()) {
					MultiplayerGameState.this.show("Main Card");
				}
				
			}
			
		}, "MP Connection Thread").start();
		
		String text = "Connecting To " + settingsParser.getIp() + " ...";
		
		Logger.log(Logger.DEBUG, text);
		lblConnectingMessage.setText(text);
		
		show(CONNECTING_CARD);
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		mess.clearText();
		networkManager.requestDisconnect();
		
		inventory.setActive(false);
		pauseScreen.setActive(false);
		
	}
	
	public void onDisconnect() {
		
		ArrayList<Entity> entities = world.getEntities();
		ArrayList<Entity> entitiesToRemove = new ArrayList<Entity>();
		
		for(Entity e: entities) {
			
			if(e instanceof EntityPlayer && !e.equals(player)) {
				entitiesToRemove.add(e);
			}
			
		}
		
		for(Entity e: entitiesToRemove) {
			
			world.removeEntity(e);
			
		}
		
		sm.requestStateChange(EState.MENU);//TODO: or could show a sreen telling the client some debug info.
		
	}
	
	public void loadTerrain(String terrainName) {
		world.setTerrain(loader.loadTerrain(terrainParser, terrainName));
	}
	
	public World getWorld() {
		return world;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
}
