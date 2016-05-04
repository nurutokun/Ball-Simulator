package com.rawad.ballsimulator.client.gamestates;

import java.util.ArrayList;
import java.util.Random;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerList;
import com.rawad.ballsimulator.client.renderengine.BackgroundRender;
import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.client.tcp.CPacket03Message;
import com.rawad.ballsimulator.server.entity.EntityPlayerMP;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.log.Logger;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

public class MultiplayerGameState extends State {
	
	private CustomLoader loader;
	private TerrainFileParser terrainParser;
	private SettingsFileParser settingsParser;
	
	@FXML private Messenger mess;
	@FXML private PlayerList playerList;
	@FXML private PlayerInventory inventory;
	@FXML private PauseScreen pauseScreen;
	@FXML private Label lblConnectingMessage;
	
	private ClientNetworkManager networkManager;
	
	private Viewport viewport;
	
	private World world;
	private Camera camera;
	
	private EntityPlayerMP player;
	
	public MultiplayerGameState(ClientNetworkManager networkManager) {
		super();
		
		this.networkManager = networkManager;
		networkManager.setClient(this);
		
		viewport = new Viewport();
		
		world = new World();
		
		player = new EntityPlayerMP(world, "Player" + (int) (new Random().nextDouble()*999), "Could be fixed if address"
				+ " wasn't final.");
		
		camera = new Camera();
		camera.setOuterBounds(new Rectangle(0, 0, world.getWidth(), world.getHeight()));
		camera.setScale(1d/2d, 1d/2d);
		
		viewport.update(world, camera);
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			if(pauseScreen.isPaused() || inventory.isVisible()) {
				
				switch(keyEvent.getCode()) {
				
				case ESCAPE:
					pauseScreen.setPaused(false);
					
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
						pauseScreen.setPaused(true);
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
					player.setUp(true);
					player.setDown(false);
					break;
					
				case LEFT:
				case A:
					player.setLeft(true);
					player.setRight(false);
					break;
					
				case DOWN:
				case S:
					player.setDown(true);
					player.setUp(false);
					break;
					
				case RIGHT:
				case D:
					player.setRight(true);
					player.setLeft(false);
					break;
					
				default:
					break;
				}
			}
			
		});
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			
			switch(keyEvent.getCode()) {
			
			case T:
				if(!pauseScreen.isPaused() && !inventory.isVisible()) mess.setShowing(true);
				break;
				
			case TAB:
				playerList.setVisible(false);
				break;
			
			case UP:
			case W:
				player.setUp(false);
				break;
			
			case LEFT:
			case A:
				player.setLeft(false);
				break;
			
			case DOWN:
			case S:
				player.setDown(false);
				break;
			
			case RIGHT:
			case D:
				player.setRight(false);
				break;
			
			default:
				break;
			
			}
			
		});
		
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(MenuState.class));
		
		mess.getInputArea().addEventHandler(ActionEvent.ACTION, e -> {
			
			CPacket03Message message = new CPacket03Message(player.getName(), mess.getInputArea().getText());
			
			networkManager.getConnectionManager().sendPacketToServer(message);
			
			addUserMessage("You", message.getMessage());
			mess.getInputArea().setText("");// So that it doesn't get duplicated.
			
		});
		
	}
	
	@Override
	public void tick() {
		
		if(networkManager.isLoggedIn()) {// Start updating world, player and camera once login is successful
			
			networkManager.updatePlayerMovement(player.isUp(), player.isDown(), player.isRight(), player.isLeft());
			
			world.update();
			
			camera.setX(player.getX() - (Game.SCREEN_WIDTH / camera.getScaleX() / 2));
			camera.setY(player.getY() - (Game.SCREEN_HEIGHT / camera.getScaleY() / 2));
			
		}
		
	}
	
	@Override
	public void render() {
		super.render();
		
		if(networkManager.isLoggedIn()) {
			viewport.render(canvas, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		} else {
			BackgroundRender.instance().render(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight());
		}
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
		
		pauseScreen.setPaused(false);
		
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
		
		ArrayList<Entity> entities = world.getEntities();
		ArrayList<Entity> entitiesToRemove = new ArrayList<Entity>();
		
		for(Entity e: entities) {
			
			if(e instanceof EntityPlayerMP && !e.equals(player)) {
				entitiesToRemove.add(e);
			}
			
		}
		
		world.getEntities().removeAll(entitiesToRemove);
		
		entitiesToRemove.add(player);
		
		playerList.getItems().remove(entitiesToRemove);
		
		sm.requestStateChange(MenuState.class);
		
	}
	
	public void loadTerrain(String terrainName) {
		
		sm.getClient().addTask(new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				world.setTerrain(loader.loadTerrain(terrainParser, terrainName));
				return 0;
			}
		});
		
	}
	
	public void addPlayer(EntityPlayerMP player) {
		playerList.getItems().add(player);
	}
	
	public void removePlayer(String username) {
		
		EntityPlayerMP playerToRemove = null;
		
		for(EntityPlayerMP player: playerList.getItems()) {
			if(player.getName().equals(username)) {
				playerToRemove = player;
				break;
			}
		}
		
		if(playerToRemove != null) playerList.getItems().remove(playerToRemove);
		
	}
	
	public void addUserMessage(String usernameOfSender, String message) {
		
		if(!usernameOfSender.isEmpty()) {
			message = usernameOfSender + "> " + message;
		}
		
		mess.appendNewLine(message);
	}
	
	public World getWorld() {
		return world;
	}
	
	public EntityPlayerMP getPlayer() {
		return player;
	}
	
}
