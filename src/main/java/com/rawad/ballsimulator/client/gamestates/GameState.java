package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.MasterRender;
import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.RandomPositionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.CollisionSystem;
import com.rawad.ballsimulator.game.MovementSystem;
import com.rawad.ballsimulator.game.PlayerControlSystem;
import com.rawad.ballsimulator.game.PositionGenerationSystem;
import com.rawad.ballsimulator.game.RenderingSystem;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.client.IClientController;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.resources.Loader;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class GameState extends State implements IClientController {
	
	private MasterRender masterRender;
	
	private RenderingSystem renderingSystem;
	
	private Entity player;
	private TransformComponent playerTransform;
	private MovementComponent playerMovement;
	private RandomPositionComponent playerRandomPositioner;
	
	@FXML private PauseScreen pauseScreen;
	@FXML private PlayerInventory inventory;
	@FXML private Messenger mess;
	
	private boolean showEntireWorld;
	
	public GameState(MasterRender masterRender) {
		super();
		
		this.masterRender = masterRender;
		
		camera.setOuterBounds(new Rectangle(0, 0, world.getWidth(), world.getHeight()));
		
		player = Entity.createEntity(EEntity.USER_CONTROLLABLE_PLAYER);
		playerTransform = player.getComponent(TransformComponent.class);
		playerMovement = player.getComponent(MovementComponent.class);
		playerRandomPositioner = player.getComponent(RandomPositionComponent.class);
		
		com.rawad.gamehelpers.geometry.Rectangle playerHitbox = player.getComponent(CollisionComponent.class).getHitbox();
		playerHitbox.setWidth(40);
		playerHitbox.setHeight(40);
		
		world.addEntity(player);
		
		renderingSystem = new RenderingSystem();
		
		gameSystems.add(new PositionGenerationSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(new PlayerControlSystem());
		gameSystems.add(new MovementSystem());
		gameSystems.add(new CollisionSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(renderingSystem);
		
		showEntireWorld = false;
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			if(pauseScreen.isPaused()) {
				
				switch(keyEvent.getCode()) {
				
				case ESCAPE:
					pauseScreen.setPaused(false);
					break;
				
				default:
					break;
				}
				
			} else {

				switch(keyEvent.getCode()) {
				
				case ESCAPE:
					
					if(inventory.isVisible()) {
						inventory.setVisible(false);
					} else if(mess.isShowing()) {
						mess.setShowing(false);
					} else {
						pauseScreen.setPaused(true);
					}
					
					break;
					
				case E:
					if(mess.isShowing()) break;
					inventory.setVisible(!inventory.isVisible());
					break;
					
				case R:
					player.getComponent(RandomPositionComponent.class).setGenerateNewPosition(true);
					break;
					
				case L:
					showEntireWorld = !showEntireWorld;
					break;
					
				case C:
					camera.increaseRotation(5);
					break;
					
				case Z:
					camera.increaseRotation(-5);
					break;
					
				case X:
					camera.setRotation(0);
					break;
					
				case ENTER:
					if(!mess.isShowing() && !inventory.isVisible()) mess.setShowing(true);
					break;
					
				case F5:
					
					String style = Loader.getStyleSheetLocation(Client.class, "StyleSheet");
					
					root.getScene().getStylesheets().remove(style);
					root.getScene().getStylesheets().add(style);
					
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
				if(!pauseScreen.isPaused() && !inventory.isVisible()) mess.setShowing(true);
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
		
		DebugRender debugRender = masterRender.getDebugRender();
		
		root.addEventHandler(MouseEvent.ANY, mouseEvent -> {
			
			debugRender.setMouseX(mouseEvent.getX());
			debugRender.setMouseY(mouseEvent.getY());
			
		});
		
		debugRender.widthProperty().bind(root.widthProperty());
		debugRender.heightProperty().bind(root.heightProperty());
		
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(MenuState.class));
		
	}
	
	@Override
	public void tick() {
		
		if(!pauseScreen.isPaused() && !inventory.isVisible()) {
			
			if(showEntireWorld) {
				camera.setScale(Double.MIN_VALUE, Double.MIN_VALUE);
			} else {
				camera.setScale(1d/2d, 1d/2d);
			}
			
			camera.setX(playerTransform.getX() - (camera.getCameraBounds().getWidth() / camera.getScaleX() / 2d));
			camera.setY(playerTransform.getY() - (camera.getCameraBounds().getHeight() / camera.getScaleY() / 2d));
			
		}
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		sm.getClient().addTask(new Task<Integer>() {
			
			protected Integer call() {
				
				CustomLoader loader = sm.getGame().getLoader(CustomLoader.class);
				
				TerrainFileParser parser = sm.getGame().getFileParser(TerrainFileParser.class);
				
				loader.loadTerrain(parser, world, "terrain");
				playerRandomPositioner.setGenerateNewPosition(true);
				
				return 0;
				
			}
			
		});
		
		pauseScreen.setPaused(false);
		
		masterRender.setRenderingSystem(renderingSystem);
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		masterRender.setRenderingSystem(null);
		
	}
	
}
