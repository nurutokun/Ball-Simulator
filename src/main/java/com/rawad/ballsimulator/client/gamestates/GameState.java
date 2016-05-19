package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.GameTextures;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.AttachmentComponent;
import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.RandomPositionComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.CameraFollowSystem;
import com.rawad.ballsimulator.game.CollisionSystem;
import com.rawad.ballsimulator.game.MovementSystem;
import com.rawad.ballsimulator.game.PlayerControlSystem;
import com.rawad.ballsimulator.game.PositionGenerationSystem;
import com.rawad.ballsimulator.game.RollingSystem;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.client.IClientController;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;
import com.rawad.gamehelpers.resources.Loader;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GameState extends State implements IClientController {
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	private TransformComponent cameraTransform;
	
	private Entity camera;
	
	private Entity player;
	private MovementComponent playerMovement;
	private RandomPositionComponent playerRandomPositioner;
	
	@FXML private PauseScreen pauseScreen;
	@FXML private PlayerInventory inventory;
	@FXML private Messenger mess;
	
	private boolean showEntireWorld;
	
	public GameState() {
		super();
		
		player = Entity.createEntity(EEntity.USER_CONTROLLABLE_PLAYER);
		playerMovement = player.getComponent(MovementComponent.class);
		playerRandomPositioner = player.getComponent(RandomPositionComponent.class);
		RenderingComponent playerRender = player.getComponent(RenderingComponent.class);
		
		playerRender.setTexture(GameTextures.findTexture(EEntity.PLAYER));
		playerRender.getTextureObject().setOnloadAction(texture -> {
			
			Rectangle hitbox = player.getComponent(CollisionComponent.class).getHitbox();
			hitbox.setWidth(texture.getTexture().getWidth());
			hitbox.setHeight(texture.getTexture().getHeight());
			
		});
		
		CollisionComponent playerCollision = player.getComponent(CollisionComponent.class);
		
		world.addEntity(player);
		
		camera = Entity.createEntity(EEntity.CAMERA);
		camera.getComponent(AttachmentComponent.class).setAttachedTo(player);
		
		cameraTransform = camera.getComponent(TransformComponent.class);		
		cameraTransform.setScaleX(0.5D);
		cameraTransform.setScaleY(0.5D);
		
		cameraTransform.setMaxScaleX(5D);
		cameraTransform.setMaxScaleY(5D);
		
		world.addEntity(camera);
		
		MovementSystem movementSystem = new MovementSystem();
		playerCollision.getListeners().add(movementSystem);
		
		worldRender = new WorldRender(world, camera);
		debugRender = new DebugRender(camera);
		
		masterRender.registerRender(worldRender);
		masterRender.registerRender(debugRender);
		
		gameSystems.add(new PositionGenerationSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(new PlayerControlSystem());
		gameSystems.add(movementSystem);
		gameSystems.add(new CollisionSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(new CameraFollowSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(new RollingSystem());
		
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
					
					if(showEntireWorld) {
						cameraTransform.setScaleX(Double.MIN_VALUE);
						cameraTransform.setScaleY(Double.MIN_VALUE);
					} else {
						cameraTransform.setScaleX(1d / 2d);
						cameraTransform.setScaleY(1d / 2d);
					}
					
					break;
					
				case C:
					cameraTransform.setTheta(cameraTransform.getTheta() + 5);
					break;
					
				case Z:
					cameraTransform.setTheta(cameraTransform.getTheta() - 5);
					break;
					
				case X:
					cameraTransform.setTheta(0);
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
		
		root.addEventHandler(MouseEvent.ANY, mouseEvent -> {
			
			debugRender.setMouseX(mouseEvent.getX());
			debugRender.setMouseY(mouseEvent.getY());
			
		});
		
		debugRender.widthProperty().bind(root.widthProperty());
		debugRender.heightProperty().bind(root.heightProperty());
		
		Rectangle viewport = camera.getComponent(UserViewComponent.class).getViewport();
		viewport.widthProperty().bind(root.widthProperty());
		viewport.heightProperty().bind(root.heightProperty());
		
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(MenuState.class));
		
	}
	
	@Override
	public void tick() {
		
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
		
	}
	
}
