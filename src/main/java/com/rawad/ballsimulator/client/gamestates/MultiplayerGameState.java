package com.rawad.ballsimulator.client.gamestates;

import java.util.Random;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.gui.Transitions;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerList;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.AttachmentComponent;
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
import com.rawad.ballsimulator.networking.client.listeners.MovementControlListener;
import com.rawad.ballsimulator.networking.client.tcp.CPacket03Message;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;
import com.rawad.gamehelpers.log.Logger;

import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MultiplayerGameState extends State {
	
	private static final double PREFERRED_SCALE = 1d / 2d;
	
	private Client client;
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	private CustomLoader loader;
	private SettingsFileParser settingsParser;
	
	@FXML private Messenger mess;
	@FXML private GridPane playerListContainer;
	@FXML private PlayerList playerList;
	@FXML private PlayerInventory inventory;
	@FXML private PauseScreen pauseScreen;
	@FXML private VBox connectContainer;
	@FXML private Button cancelConnect;
	@FXML private Label lblConnectingMessage;
	
	private ClientNetworkManager networkManager;
	
	private MovementControlSystem movementControlSystem;
	
	private Entity camera;
	private UserViewComponent cameraView;
	
	private Entity player;
	private UserComponent playerUser;
	
	public MultiplayerGameState(StateManager sm) {
		super(sm);
		
		this.client = game.getProxies().get(Client.class);
		
		networkManager = new ClientNetworkManager(this);
		
		player = Entity.createEntity(EEntity.PLAYER);
		player.addComponent(new UserControlComponent());
		player.addComponent(new GuiComponent());
		player.addComponent(new NetworkComponent());
		
		playerUser = new UserComponent();
		playerUser.setUsername("Player" + (int) (new Random().nextDouble() * 999d));
		player.addComponent(playerUser);
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		AttachmentComponent attachmentComp = new AttachmentComponent();
		attachmentComp.setAttachedTo(player);
		camera.addComponent(attachmentComp);
		
		TransformComponent cameraTransform = camera.getComponent(TransformComponent.class);
		cameraTransform.setScaleX(PREFERRED_SCALE);
		cameraTransform.setScaleY(PREFERRED_SCALE);
		
		cameraView = camera.getComponent(UserViewComponent.class);
		
		worldRender = new WorldRender(world, camera);
		debugRender = new DebugRender(client, camera);
		
		masterRender.getRenders().put(worldRender);
		masterRender.getRenders().put(debugRender);
		
		movementControlSystem = new MovementControlSystem(client.getInputBindings());
		movementControlSystem.getListeners().add(new MovementControlListener(networkManager));
		
		gameSystems.put(movementControlSystem);
		gameSystems.put(new MovementSystem());
		gameSystems.put(new CollisionSystem(world.getWidth(), world.getHeight()));
		gameSystems.put(new RollingSystem());
		gameSystems.put(new CameraFollowSystem(world.getWidth(), world.getHeight()));
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			if(!networkManager.isLoggedIn()) return;
			
			InputAction action = (InputAction) client.getInputBindings().get(keyEvent.getCode());
			
			if(pauseScreen.isVisible() || inventory.isVisible() || mess.isShowing()) {
				
				switch(action) {
				
				case PAUSE:
					mess.hide();
					pauseScreen.hide();
					
				case INVENTORY:
					inventory.setVisible(false);
					break;
					
				default:
					break;
				}
				
			} else if(!playerList.isVisible()) {
				
				switch(action) {
					
				case PAUSE:
					pauseScreen.show();
					break;
					
				case INVENTORY:
					if(inventory.isShowing())
						inventory.hide();
					else
						inventory.show();
					break;
					
				case SEND:
					mess.show();
					break;
					
				case PLAYER_LIST:
					playerListContainer.setVisible(true);
					
					keyEvent.consume();
					
					break;
					
				default:
					break;
				}
			}
			
		});
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			
			if(!networkManager.isLoggedIn()) return;
			
			InputAction action = (InputAction) client.getInputBindings().get(keyEvent.getCode());
			
			switch(action) {
			
			case CHAT:
				if(!pauseScreen.isShowing() && !inventory.isShowing()) mess.show();
				break;
				
			case PLAYER_LIST:
				playerListContainer.setVisible(false);
				break;
			
			default:
				break;
			
			}
			
		});
		
		root.widthProperty().addListener(e -> cameraView.getRequestedViewport().setWidth(root.getWidth()));
		root.heightProperty().addListener(e -> cameraView.getRequestedViewport().setHeight(root.getHeight()));
		
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(MenuState.class));
		
		cancelConnect.setOnAction(e -> networkManager.requestDisconnect());
		
		mess.getInputArea().addEventHandler(ActionEvent.ACTION, e -> {
			
			String text = mess.getInputArea().getText();
			
			if(text.isEmpty()) return;
			
			CPacket03Message message = new CPacket03Message(player.getComponent(UserComponent.class).getUsername(), text);
			
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
		
		game.addTask(new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
				
				Game game = sm.getGame();
				
				loader = game.getLoaders().get(CustomLoader.class);
				
				settingsParser = game.getFileParsers().get(SettingsFileParser.class);
				
				loader.loadSettings(settingsParser, client.getSettingsFileName());
				
				setMessage("Connecting To " + settingsParser.getIp() + " ...");
				
				networkManager.connectToServer(settingsParser.getIp());
				
				return null;
				
			}
		});
		
		mess.hide();
		connectContainer.setVisible(true);
		playerListContainer.setVisible(false);
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		networkManager.requestDisconnect();
		
	}
	
	public void onConnect() {
		
		world.addEntity(player);
		world.addEntity(camera);
		
		mess.setVisible(true);
		connectContainer.setVisible(false);
		
	}
	
	public void onDisconnect() {
		
		world.clearEntities();
		playerList.getItems().clear();
		
		sm.requestStateChange(MenuState.class);
		
	}
	
	public Entity getEntityById(int id) {
		
		for(Entity e: world.getEntities()) {
			
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
		
		synchronized(world.getEntities()) {
			for(Entity entity: world.getEntities()) {
				
				NetworkComponent networkComp = entity.getComponent(NetworkComponent.class);
				
				if(networkComp != null && networkComp.getId() == id) {
					entityToRemove = entity;
					break;
				}
			}
			
			if(entityToRemove != null) {
				playerList.getItems().remove(entityToRemove);
				world.removeEntity(entityToRemove);
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
	
	@Override
	public Transition getOnActivateTransition() {
		return Transitions.stateOnActivate(guiContainer, Transitions.DEFAULT_DURATION);
	}
	
	@Override
	public Transition getOnDeactivateTransition() {
		return Transitions.stateOnDeactivate(guiContainer, Transitions.DEFAULT_DURATION);
	}
	
}
