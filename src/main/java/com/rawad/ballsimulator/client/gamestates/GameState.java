package com.rawad.ballsimulator.client.gamestates;

import java.util.ArrayList;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.AttachmentComponent;
import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.GuiComponent;
import com.rawad.ballsimulator.entity.RandomPositionComponent;
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
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.entity.IListener;
import com.rawad.gamehelpers.resources.Loader;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

public class GameState extends State {
	
	private static final double PREFERRED_SCALE = 1d / 2d;
	
	private Client client;
	
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
	
	public GameState(StateManager sm) {
		super(sm);
		
		player = Entity.createEntity(EEntity.PLAYER);
		player.addComponent(new GuiComponent());
		player.addComponent(new UserControlComponent());
		
		playerRandomPositioner = new RandomPositionComponent();
		player.addComponent(playerRandomPositioner);
		
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
		
		client = game.getProxies().get(Client.class);
		
		worldRender = new WorldRender(world, camera);
		debugRender = new DebugRender(client, camera);
		
		masterRender.getRenders().put(worldRender);
		masterRender.getRenders().put(debugRender);
		
		movementControlSystem = new MovementControlSystem(client.getInputBindings());
		cameraFollowSystem = new CameraFollowSystem(world.getWidth(), world.getHeight(), PREFERRED_SCALE, 
				PREFERRED_SCALE);
		
		MovementSystem movementSystem = new MovementSystem();
		
		ArrayList<IListener<CollisionComponent>> collisionListeners = new ArrayList<IListener<CollisionComponent>>();
		collisionListeners.add(movementSystem);
		
		gameSystems.put(new PositionGenerationSystem(world.getWidth(), world.getHeight()));
		gameSystems.put(movementControlSystem);
		gameSystems.put(movementSystem);
		gameSystems.put(new CollisionSystem(collisionListeners, world.getWidth(), world.getHeight()));
		gameSystems.put(new RollingSystem());
		gameSystems.put(cameraFollowSystem);
		
		showEntireWorld = false;
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			InputAction action = (InputAction) client.getInputBindings().get(keyEvent.getCode());
			
			if(pauseScreen.isShowing() || inventory.isShowing() || mess.isShowing()) {
				switch(action) {
				
				case PAUSE:
					mess.hide();
					pauseScreen.hide();
				case INVENTORY:
					inventory.hide();
					break;
					
				default:
					break;
				}
				
				return;
				
			}
			
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
				
			case GEN_POS:
				playerRandomPositioner.setGenerateNewPosition(true);
				break;
				
			case SHOW_WORLD:
				showEntireWorld = !showEntireWorld;
				
				if(showEntireWorld) {
					cameraFollowSystem.setPreferredScaleX(Double.MIN_VALUE);
					cameraFollowSystem.setPreferredScaleY(Double.MIN_VALUE);
				} else {
					cameraFollowSystem.setPreferredScaleX(PREFERRED_SCALE);
					cameraFollowSystem.setPreferredScaleY(PREFERRED_SCALE);
				}
				
				break;
				
			case TILT_RIGHT:
				cameraTransform.setTheta(cameraTransform.getTheta() + 5);
				break;
				
			case TILT_LEFT:
				cameraTransform.setTheta(cameraTransform.getTheta() - 5);
				break;
				
			case TILT_RESET:
				cameraTransform.setTheta(0);
				break;
				
			case SEND:
				mess.show();
				break;
				
			case REFRESH:
				
				String style = Loader.getStyleSheetLocation(Client.class, "StyleSheet");
				
				root.getScene().getStylesheets().remove(style);
				root.getScene().getStylesheets().add(style);
				
				break;
				
			default:
				break;
			}
			
			
		});
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			
			InputAction action = (InputAction) client.getInputBindings().get(keyEvent.getCode());
			
			switch(action) {
			
			case CHAT:
				if(!pauseScreen.isShowing() && !inventory.isShowing()) mess.show();
				break;
			
			default:
				break;
			
			}
			
		});
		
		root.widthProperty().addListener(e -> cameraFollowSystem.requestNewViewportWidth(root.getWidth()));
		root.heightProperty().addListener(e -> cameraFollowSystem.requestNewViewportHeight(root.getHeight()));
		
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(MenuState.class));
		
		pauseScreen.visibleProperty().addListener(e -> game.setPaused(pauseScreen.isShowing()));
		inventory.visibleProperty().addListener(e -> game.setPaused(inventory.isShowing()));
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, movementControlSystem);
		root.addEventHandler(KeyEvent.KEY_RELEASED, movementControlSystem);
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		inventory.setVisible(false);
		mess.hide();
		
		game.addTask(new Task<Integer>() {
			protected Integer call() {
				
				CustomLoader loader = game.getLoaders().get(CustomLoader.class);
				
				TerrainFileParser parser = game.getFileParsers().get(TerrainFileParser.class);
				
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
			pauseScreen.hide();
		});
		
	}
	
	public Entity getPlayer() {
		return player;
	}
	
}
