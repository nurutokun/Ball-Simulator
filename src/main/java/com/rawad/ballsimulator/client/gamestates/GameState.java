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
import com.rawad.ballsimulator.entity.GuiComponent;
import com.rawad.ballsimulator.entity.RandomPositionComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.CameraFollowSystem;
import com.rawad.ballsimulator.game.CollisionSystem;
import com.rawad.ballsimulator.game.MovementControlSystem;
import com.rawad.ballsimulator.game.MovementSystem;
import com.rawad.ballsimulator.game.PositionGenerationSystem;
import com.rawad.ballsimulator.game.RollingSystem;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.resources.Loader;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

public class GameState extends State {
	
	private static final double PREFERRED_SCALE = 1d / 2d;
	
	private MovementControlSystem movementControlSystem;
	private CameraFollowSystem cameraFollowSystem;
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	private Entity camera;
	private TransformComponent cameraTransform;
	
	private Entity player;
	private RandomPositionComponent playerRandomPositioner;
	
	@FXML private PauseScreen pauseScreen;
	@FXML private PlayerInventory inventory;
	@FXML private Messenger mess;
	
	private boolean showEntireWorld;
	
	public GameState(AClient client) {
		super(client);
		
		player = Entity.createEntity(EEntity.PLAYER);
		player.addComponent(new GuiComponent());
		player.addComponent(new UserControlComponent());
		
		playerRandomPositioner = new RandomPositionComponent();
		player.addComponent(playerRandomPositioner);
		
		RenderingComponent playerRender = player.getComponent(RenderingComponent.class);
		playerRender.setTexture(GameTextures.findTexture(EEntity.PLAYER));
		
		world.addEntity(player);
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		AttachmentComponent attachmentComp = new AttachmentComponent();
		attachmentComp.setAttachedTo(player);
		camera.addComponent(attachmentComp);
		
		cameraTransform = camera.getComponent(TransformComponent.class);		
		cameraTransform.setScaleX(PREFERRED_SCALE);
		cameraTransform.setScaleY(PREFERRED_SCALE);
		
		cameraTransform.setMaxScaleX(5d);
		cameraTransform.setMaxScaleY(5d);
		
		world.addEntity(camera);
		
		worldRender = new WorldRender(world, camera);
		debugRender = new DebugRender(client, camera);
		
		masterRender.registerRender(worldRender);
		masterRender.registerRender(debugRender);
		
		movementControlSystem = new MovementControlSystem();
		cameraFollowSystem = new CameraFollowSystem(world.getWidth(), world.getHeight(), PREFERRED_SCALE, 
				PREFERRED_SCALE);
		
		MovementSystem movementSystem = new MovementSystem();
		
		CollisionComponent playerCollision = player.getComponent(CollisionComponent.class);
		playerCollision.getListeners().add(movementSystem);
		
		gameSystems.add(new PositionGenerationSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(movementControlSystem);
		gameSystems.add(movementSystem);
		gameSystems.add(new CollisionSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(new RollingSystem());
		gameSystems.add(cameraFollowSystem);
		
		showEntireWorld = false;
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
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
				
				return;
				
			}
			
			switch(keyEvent.getCode()) {
			
			case ESCAPE:
				pauseScreen.setVisible(true);
				break;
				
			case E:
				inventory.setVisible(!inventory.isVisible());
				break;
				
			case R:
				playerRandomPositioner.setGenerateNewPosition(true);
				break;
				
			case L:
				showEntireWorld = !showEntireWorld;
				
				if(showEntireWorld) {
					cameraFollowSystem.setPreferredScaleX(Double.MIN_VALUE);
					cameraFollowSystem.setPreferredScaleY(Double.MIN_VALUE);
				} else {
					cameraFollowSystem.setPreferredScaleX(PREFERRED_SCALE);
					cameraFollowSystem.setPreferredScaleY(PREFERRED_SCALE);
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
				mess.setShowing(true);
				break;
				
			case F5:
				
				String style = Loader.getStyleSheetLocation(Client.class, "StyleSheet");
				
				root.getScene().getStylesheets().remove(style);
				root.getScene().getStylesheets().add(style);
				
				break;
				
			default:
				break;
			}
			
			
		});
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			
			switch(keyEvent.getCode()) {
			
			case T:
				if(!pauseScreen.isVisible() && !inventory.isVisible()) mess.setShowing(true);
				break;
			
			default:
				break;
			
			}
			
		});
		
		root.widthProperty().addListener(e -> cameraFollowSystem.requestNewViewportWidth(root.getWidth()));
		root.heightProperty().addListener(e -> cameraFollowSystem.requestNewViewportHeight(root.getHeight()));
		
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(MenuState.class));
		
		pauseScreen.visibleProperty().addListener(e -> sm.getGame().setPaused(pauseScreen.isVisible()));
		inventory.visibleProperty().addListener(e -> sm.getGame().setPaused(inventory.isVisible()));
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, movementControlSystem);
		root.addEventHandler(KeyEvent.KEY_RELEASED, movementControlSystem);
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		client.addTask(new Task<Integer>() {
			protected Integer call() {
				
				CustomLoader loader = sm.getGame().getLoader(CustomLoader.class);
				
				TerrainFileParser parser = sm.getGame().getFileParser(TerrainFileParser.class);
				
				loader.loadTerrain(parser, world, "terrain");
				playerRandomPositioner.setGenerateNewPosition(true);
				
				return 0;
				
			}
			
		});
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		Platform.runLater(() -> {
			pauseScreen.setVisible(false);
			inventory.setVisible(false);
			mess.setShowing(false);
		});
		
	}
	
	public Entity getPlayer() {
		return player;
	}
	
}
