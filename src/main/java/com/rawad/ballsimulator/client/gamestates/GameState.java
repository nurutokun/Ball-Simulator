package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.gui.Root;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.AttachmentComponent;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.GuiComponent;
import com.rawad.ballsimulator.entity.RandomPositionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.CameraFollowSystem;
import com.rawad.ballsimulator.game.CollisionSystem;
import com.rawad.ballsimulator.game.MovementControlSystem;
import com.rawad.ballsimulator.game.MovementSystem;
import com.rawad.ballsimulator.game.PositionGenerationSystem;
import com.rawad.ballsimulator.game.RenderingSystem;
import com.rawad.ballsimulator.game.RollingSystem;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateChangeRequest;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

public class GameState extends State {
	
	private static final double PREFERRED_SCALE = 1d / 2d;
	
	private Client client;
	
	private MovementControlSystem movementControlSystem;
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	private Entity camera;
	private TransformComponent cameraTransform;
	private UserViewComponent cameraView;
	
	private Entity player;
	private RandomPositionComponent playerRandomPositioner;
	
	@FXML private PauseScreen pauseScreen;
	@FXML private PlayerInventory inventory;
	@FXML private Messenger mess;
	
	private boolean showEntireWorld;
	
	@Override
	public void init(StateManager sm) {
		super.init(sm);
		
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
		
		cameraView = camera.getComponent(UserViewComponent.class);
		cameraView.setPreferredScaleX(PREFERRED_SCALE);
		cameraView.setPreferredScaleY(PREFERRED_SCALE);
		
		world.addEntity(camera);
		
		this.client = game.getProxies().get(Client.class);
		
		worldRender = new WorldRender(world, camera);
		debugRender = new DebugRender(client, camera);
		
		masterRender.getRenders().put(worldRender);
		masterRender.getRenders().put(debugRender);
		
		movementControlSystem = new MovementControlSystem(client.getInputBindings());
		
		gameSystems.put(new PositionGenerationSystem(world.getWidth(), world.getHeight()));
		gameSystems.put(movementControlSystem);
		gameSystems.put(new MovementSystem());
		gameSystems.put(new CollisionSystem(world.getWidth(), world.getHeight()));
		gameSystems.put(new RollingSystem());
		gameSystems.put(new CameraFollowSystem(world.getWidth(), world.getHeight()));
		gameSystems.put(new RenderingSystem(worldRender));
		
		showEntireWorld = false;
		
	}
	
	@Override
	public void initGui() {
		
		Root root = GuiRegister.loadGui(this);
		
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
					cameraView.setPreferredScaleX(Double.MIN_VALUE);
					cameraView.setPreferredScaleY(Double.MIN_VALUE);
				} else {
					cameraView.setPreferredScaleX(PREFERRED_SCALE);
					cameraView.setPreferredScaleY(PREFERRED_SCALE);
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
				
				String style = Loader.getStyleSheetLocation(GameState.class, root.getStyleSheet());
				
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
		
		root.widthProperty().addListener(e -> cameraView.getRequestedViewport().setWidth(root.getWidth()));
		root.heightProperty().addListener(e -> cameraView.getRequestedViewport().setHeight(root.getHeight()));
		
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(MenuState.class)));
		
		pauseScreen.visibleProperty().addListener(e -> game.setPaused(pauseScreen.isShowing()));
		inventory.visibleProperty().addListener(e -> game.setPaused(inventory.isShowing()));
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, movementControlSystem);
		root.addEventHandler(KeyEvent.KEY_RELEASED, movementControlSystem);
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		inventory.hide();
		mess.hide();
		
		Loader.addTask(new Task<Void>() {
			protected Void call() throws Exception {
				
				Loader loader = client.getLoaders().get(Loader.class);
				
				TerrainFileParser parser = client.getFileParsers().get(TerrainFileParser.class);
				
				loader.loadTerrain(parser, world, "terrain");
				playerRandomPositioner.setGenerateNewPosition(true);
				
				return null;
				
			}
			
		});
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		pauseScreen.hide();
		
	}
	
	public Entity getPlayer() {
		return player;
	}
	
}
