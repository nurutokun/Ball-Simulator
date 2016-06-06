package com.rawad.ballsimulator.client.gamestates;

import java.util.Random;

import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerList;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.AttachmentComponent;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.GuiComponent;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.MovementControlSystem;
import com.rawad.ballsimulator.game.MovementSystem;
import com.rawad.ballsimulator.game.RollingSystem;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.client.tcp.CPacket04Message;
import com.rawad.ballsimulator.server.entity.NetworkComponent;
import com.rawad.ballsimulator.server.entity.UserComponent;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;
import com.rawad.gamehelpers.log.Logger;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

public class MultiplayerGameState extends State {
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	private CustomLoader loader;
	private TerrainFileParser terrainParser;
	private SettingsFileParser settingsParser;
	
	@FXML private Messenger mess;
	@FXML private PlayerList playerList;
	@FXML private PlayerInventory inventory;
	@FXML private PauseScreen pauseScreen;
	@FXML private Label lblConnectingMessage;
	
	private ClientNetworkManager networkManager;
	
	private Entity camera;
	
	private Entity player;
	private UserComponent playerUser;
	private MovementComponent playerMovement;
	
	public MultiplayerGameState(ClientNetworkManager networkManager) {
		super();
		
//		this.networkManager = networkManager;
//		networkManager.setClient(this);
		
		player = Entity.createEntity(EEntity.PLAYER);
		player.addComponent(new UserControlComponent());
		player.addComponent(new GuiComponent());
		player.addComponent(new NetworkComponent());
		
		playerUser = new UserComponent();
		playerUser.setUsername("Player" + (int) (new Random().nextDouble() * 999d));
		player.addComponent(playerUser);
		
		playerMovement = player.getComponent(MovementComponent.class);
		
		world.addEntity(player);
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		AttachmentComponent attachmentComp = new AttachmentComponent();
		attachmentComp.setAttachedTo(player);
		camera.addComponent(attachmentComp);
		
		TransformComponent cameraTransform = camera.getComponent(TransformComponent.class);
		cameraTransform.setScaleX(1d / 2d);
		cameraTransform.setScaleY(1d / 2d);
		
		world.addEntity(camera);
		
		worldRender = new WorldRender(world, camera);
		debugRender = new DebugRender(sm.getClient(), camera);
		
		masterRender.registerRender(worldRender);
		masterRender.registerRender(debugRender);
		
		gameSystems.add(new MovementControlSystem());
		//gameSystems.add(new NetworkPlayerControlSystem(networkManager));
		gameSystems.add(new MovementSystem());
		gameSystems.add(new RollingSystem());
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			if(pauseScreen.isVisible() || inventory.isVisible()) {
				
				switch(keyEvent.getCode()) {
				
				case ESCAPE:
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
					
					if(mess.isShowing()) {
						mess.setShowing(false);
					} else {
						pauseScreen.setVisible(true);
					}
					
					break;
					
				case E:
					if(mess.isShowing()) break;
					inventory.setVisible(true);
					break;
					
				case ENTER:
					if(!mess.isShowing()) mess.setShowing(true);
					break;
					
				case TAB:
					playerList.setVisible(true);
					
					keyEvent.consume();
					
					break;
					
				case UP:
				case W:
					playerMovement.setUp(true);
					playerMovement.setDown(false);
					break;
					
				case LEFT:
				case A:
					playerMovement.setLeft(true);
					playerMovement.setRight(false);
					break;
					
				case DOWN:
				case S:
					playerMovement.setDown(true);
					playerMovement.setUp(false);
					break;
					
				case RIGHT:
				case D:
					playerMovement.setRight(true);
					playerMovement.setLeft(false);
					break;
					
				default:
					break;
				}
			}
			
		});
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			
			switch(keyEvent.getCode()) {
			
			case T:
				if(!pauseScreen.isVisible() && !inventory.isVisible()) mess.setShowing(true);
				break;
				
			case TAB:
				playerList.setVisible(false);
				break;
			
			case UP:
			case W:
				playerMovement.setUp(false);
				break;
			
			case LEFT:
			case A:
				playerMovement.setLeft(false);
				break;
			
			case DOWN:
			case S:
				playerMovement.setDown(false);
				break;
			
			case RIGHT:
			case D:
				playerMovement.setRight(false);
				break;
			
			default:
				break;
			
			}
			
		});
		
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(MenuState.class));
		
		mess.getInputArea().addEventHandler(ActionEvent.ACTION, e -> {
			
			CPacket04Message message = new CPacket04Message(player.toString(), mess.getInputArea().getText());
			
			networkManager.getConnectionManager().sendPacketToServer(message);
			
			addUserMessage("You", message.getMessage());
			mess.getInputArea().setText("");// So that it doesn't get duplicated.
			
		});
		
		Rectangle viewport = camera.getComponent(UserViewComponent.class).getViewport();
		viewport.widthProperty().bind(root.widthProperty());
		viewport.heightProperty().bind(root.heightProperty());
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		sm.getClient().addTask(new Task<Integer>() {
			
			@Override
			protected Integer call() throws Exception {
				
				Game game = sm.getGame();
				
				loader = game.getLoader(CustomLoader.class);
				
				terrainParser = game.getFileParser(TerrainFileParser.class);
				settingsParser = game.getFileParser(SettingsFileParser.class);
				
				loader.loadSettings(settingsParser, sm.getClient().getSettingsFileName());
				
				String text = "Connecting To " + settingsParser.getIp() + " ...";
				
				Logger.log(Logger.DEBUG, text);
				Platform.runLater(() -> lblConnectingMessage.setText(text));
				
				networkManager.init(settingsParser.getIp());
				
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
		
	}
	
	public void onConnect() {
		mess.setVisible(true);
		lblConnectingMessage.setVisible(false);
	}
	
	public void onDisconnect() {
		
		world.getEntitiesAsList().clear();// We're reloading terrain each time anyways
		playerList.getItems().clear();
		
		sm.requestStateChange(MenuState.class);
		
	}
	
	public void loadTerrain(String terrainName) {
		
		sm.getClient().addTask(new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				loader.loadTerrain(terrainParser, world, terrainName);
				return 0;
			}
		});
		
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
	}
	
	public Entity getPlayer() {
		return player;
	}
	
}
