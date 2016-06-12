package com.rawad.ballsimulator.client.gamestates;

import java.util.Random;

import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerList;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.AttachmentComponent;
import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.GuiComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.game.CameraFollowSystem;
import com.rawad.ballsimulator.game.CollisionSystem;
import com.rawad.ballsimulator.game.MovementControlSystem;
import com.rawad.ballsimulator.game.MovementSystem;
import com.rawad.ballsimulator.game.RollingSystem;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.client.tcp.CPacket04Message;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;
import com.rawad.gamehelpers.log.Logger;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

public class MultiplayerGameState extends State {
	
	private static final double PREFERRED_SCALE = 1d / 2d;
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	private CustomLoader loader;
	private SettingsFileParser settingsParser;
	
	@FXML private Messenger mess;
	@FXML private PlayerList playerList;
	@FXML private PlayerInventory inventory;
	@FXML private PauseScreen pauseScreen;
	@FXML private Button cancelConnect;
	@FXML private Label lblConnectingMessage;
	
	private ClientNetworkManager networkManager;
	
	private MovementControlSystem movementControlSystem;
	private CameraFollowSystem cameraFollowSystem;
	
	private Entity camera;
	
	private Entity player;
	private UserComponent playerUser;
	
	public MultiplayerGameState(AClient client) {
		super(client);
		
		networkManager = new ClientNetworkManager(this);
		
		player = Entity.createEntity(EEntity.PLAYER);
		player.addComponent(new UserControlComponent());
		player.addComponent(new GuiComponent());
		player.addComponent(new NetworkComponent());
		
		playerUser = new UserComponent();
		playerUser.setUsername("Player" + (int) (new Random().nextDouble() * 999d));
		player.addComponent(playerUser);
		
		world.addEntity(player);
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		AttachmentComponent attachmentComp = new AttachmentComponent();
		attachmentComp.setAttachedTo(player);
		camera.addComponent(attachmentComp);
		
		TransformComponent cameraTransform = camera.getComponent(TransformComponent.class);
		cameraTransform.setScaleX(PREFERRED_SCALE);
		cameraTransform.setScaleY(PREFERRED_SCALE);
		
		world.addEntity(camera);
		
		worldRender = new WorldRender(world, camera);
		debugRender = new DebugRender(client, camera);
		
		masterRender.registerRender(worldRender);
		masterRender.registerRender(debugRender);
		
		movementControlSystem = new MovementControlSystem();
		
		MovementSystem movementSystem = new MovementSystem();
		player.getComponent(CollisionComponent.class).getListeners().add(movementSystem);
		
		cameraFollowSystem = new CameraFollowSystem(world.getWidth(), world.getHeight(), PREFERRED_SCALE, 
				PREFERRED_SCALE);
		
		gameSystems.add(movementControlSystem);
		//gameSystems.add(new NetworkPlayerControlSystem(networkManager));
		gameSystems.add(movementSystem);
		gameSystems.add(new CollisionSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(new RollingSystem());
		gameSystems.add(cameraFollowSystem);
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			if(!networkManager.isLoggedIn()) return;
			
			if(pauseScreen.isVisible() || inventory.isVisible() || mess.isShowing()) {
				
				switch(keyEvent.getCode()) {
				
				case ESCAPE:
					mess.setShowing(false);
					pauseScreen.setVisible(false);
					
				case E:
					inventory.setVisible(false);
					break;
					
				default:
					break;
				}
				
			} else if(!playerList.isVisible()) {
				
				switch(keyEvent.getCode()) {
					
				case ESCAPE:
					pauseScreen.setVisible(true);
					break;
					
				case E:
					inventory.setVisible(!inventory.isVisible());
					break;
					
				case ENTER:
					mess.setShowing(true);
					break;
					
				case TAB:
					playerList.setVisible(true);
					
					keyEvent.consume();
					
					break;
					
				default:
					break;
				}
			}
			
		});
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			
			if(!networkManager.isLoggedIn()) return;
			
			switch(keyEvent.getCode()) {
			
			case T:
				if(!pauseScreen.isVisible() && !inventory.isVisible()) mess.setShowing(true);
				break;
				
			case TAB:
				playerList.setVisible(false);
				break;
			
			default:
				break;
			
			}
			
		});
		
		root.widthProperty().addListener(e -> cameraFollowSystem.requestNewViewportWidth(root.getWidth()));
		root.heightProperty().addListener(e -> cameraFollowSystem.requestNewViewportHeight(root.getHeight()));
		
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(MenuState.class));
		
		cancelConnect.setOnAction(e -> networkManager.requestDisconnect());
		
		mess.getInputArea().addEventHandler(ActionEvent.ACTION, e -> {
			
			String text = mess.getInputArea().getText();
			
			if(text.isEmpty()) return;
			
			CPacket04Message message = new CPacket04Message(player.getComponent(UserComponent.class).getUsername(), text);
			
			networkManager.getConnectionManager().sendPacketToServer(message);
			
			addUserMessage("You", text);
			mess.getInputArea().setText("");// So that it doesn't get duplicated.
			
		});
		
		Rectangle viewport = camera.getComponent(UserViewComponent.class).getViewport();
		viewport.widthProperty().bind(root.widthProperty());
		viewport.heightProperty().bind(root.heightProperty());
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, movementControlSystem);
		root.addEventHandler(KeyEvent.KEY_RELEASED, movementControlSystem);
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		client.addTask(new Task<Integer>() {
			
			@Override
			protected Integer call() throws Exception {
				
				Game game = sm.getGame();
				
				loader = game.getLoader(CustomLoader.class);
				
				settingsParser = game.getFileParser(SettingsFileParser.class);
				
				loader.loadSettings(settingsParser, client.getSettingsFileName());
				
				setMessage("Connecting To " + settingsParser.getIp() + " ...");
				
				networkManager.connectToServer(settingsParser.getIp());
				
				return 0;
				
			}
		});
		
		pauseScreen.setVisible(false);
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		networkManager.requestDisconnect();
		
		mess.setVisible(false);
		lblConnectingMessage.setVisible(true);
		cancelConnect.setVisible(true);
		
	}
	
	public void onConnect() {
		mess.setVisible(true);
		lblConnectingMessage.setVisible(false);
		cancelConnect.setVisible(false);
	}
	
	public void onDisconnect() {
		
		world.getEntitiesAsList().clear();// We're reloading terrain each time anyways
		playerList.getItems().clear();
		
		sm.requestStateChange(MenuState.class);
		
	}
	
	public Entity getEntityById(int id) {
		
		for(Entity e: world.getEntitiesAsList()) {
			
			NetworkComponent networkComp = e.getComponent(NetworkComponent.class);
			
			if(networkComp != null && networkComp.getId() == id) return e;
			
		}
		
		return null;
		
	}
	
	public void addPlayer(Entity player) {
		playerList.getItems().add(player);
	}
	
	public void removeEntity(int id) {
		
		Entity entityToRemove = null;
		
		synchronized(world.getEntitiesAsList()) {
			for(Entity entity: world.getEntitiesAsList()) {
				
				NetworkComponent networkComp = entity.getComponent(NetworkComponent.class);
				
				if(networkComp != null && networkComp.getId() == id) {
					entityToRemove = entity;
					break;
				}
			}
			
			if(entityToRemove != null) {
				playerList.getItems().remove(entityToRemove);
				world.getEntitiesAsList().remove(entityToRemove);
			}
			
		}
		
	}
	
	public void addUserMessage(String usernameOfSender, String message) {
		
		if(!usernameOfSender.isEmpty()) {
			message = usernameOfSender + "> " + message;
		}
		
		mess.appendNewLine(message);
		Logger.log(Logger.DEBUG, message);
		
	}
	
	public void setMessage(String message) {
		Platform.runLater(() -> lblConnectingMessage.setText(message));
		Logger.log(Logger.DEBUG, message);
	}
	
	public Entity getPlayer() {
		return player;
	}
	
}
