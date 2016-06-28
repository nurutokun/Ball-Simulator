package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.GameTextures;
import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.PlaceableComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.CameraRoamingSystem;
import com.rawad.ballsimulator.game.CollisionSystem;
import com.rawad.ballsimulator.game.EntityPlacementSystem;
import com.rawad.ballsimulator.game.EntitySelectionSystem;
import com.rawad.ballsimulator.game.MovementControlSystem;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateChangeRequest;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class WorldEditorState extends State {
	
	private Client client;
	
	private MovementControlSystem movementControlSystem;
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	private TransformComponent cameraTransform;
	private UserViewComponent cameraView;
	
	private TransformComponent toBePlacedTransform;
	private PlaceableComponent placeableComp;
	
	private CustomLoader loader;
	private TerrainFileParser terrainFileParser;
	
	@FXML private PauseScreen pauseScreen;
	
	@FXML private ComboBox<Double> widthSelector;
	@FXML private ComboBox<Double> heightSelector;
	
	@Override
	public void init(StateManager sm) {
		super.init(sm);
		
		this.client = game.getProxies().get(Client.class);
		
		Entity camera = Entity.createEntity(EEntity.CAMERA);
		
		camera.addComponent(new MovementComponent());
		camera.addComponent(new UserControlComponent());
		
		cameraTransform = camera.getComponent(TransformComponent.class);
		
		cameraTransform.setScaleX(1d);
		cameraTransform.setScaleY(1d);
		
		cameraTransform.setMaxScaleX(5D);
		cameraTransform.setMaxScaleY(5D);
		
		cameraView = camera.getComponent(UserViewComponent.class);
		
		world.addEntity(camera);
		
		worldRender = new WorldRender(world, camera);
		debugRender = new DebugRender(client, camera);
		
		masterRender.getRenders().put(worldRender);
		masterRender.getRenders().put(debugRender);
		
		movementControlSystem = new MovementControlSystem(client.getInputBindings());
		
		gameSystems.put(movementControlSystem);
		gameSystems.put(new CameraRoamingSystem(true, world.getWidth(), world.getHeight()));
		gameSystems.put(new CollisionSystem(world.getWidth(), world.getHeight()));
		gameSystems.put(new EntitySelectionSystem(cameraTransform));
		gameSystems.put(new EntityPlacementSystem(cameraTransform));
		
		Entity toBePlaced = Entity.createEntity(EEntity.PLACEABLE);
		toBePlacedTransform = toBePlaced.getComponent(TransformComponent.class);
		
		placeableComp = toBePlaced.getComponent(PlaceableComponent.class);
		placeableComp.getExtractionListeners().add((e, component) -> {
			
			final double scaleX = component.getScaleX();
			final double scaleY = component.getScaleY();
			
			Platform.runLater(() -> {
				
				widthSelector.getSelectionModel().select(scaleX);
				heightSelector.getSelectionModel().select(scaleY);
				
			});
			
		});
		placeableComp.setToPlace(EEntity.STATIC);// Can be any other entity too. < and V
		toBePlaced.getComponent(SelectionComponent.class).setSelected(true);
		toBePlaced.getComponent(RenderingComponent.class).setTexture(GameTextures.findTexture(EEntity.STATIC));
		
		world.addEntity(toBePlaced);
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		widthSelector.setFocusTraversable(false);// Mainly for when in pause screen.
		heightSelector.setFocusTraversable(false);
		
		widthSelector.getItems().addAll(TerrainFileParser.DIMS);
		heightSelector.getItems().addAll(TerrainFileParser.DIMS);
		
		toBePlacedTransform.scaleXProperty().bind(widthSelector.getSelectionModel().selectedItemProperty());
		toBePlacedTransform.scaleYProperty().bind(heightSelector.getSelectionModel().selectedItemProperty());
		
		widthSelector.getSelectionModel().select(TerrainFileParser.DIMS.length / 2);
		heightSelector.getSelectionModel().select(TerrainFileParser.DIMS.length / 2);
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			InputAction action = (InputAction) client.getInputBindings().get(keyEvent.getCode());
			
			switch(action) {
			
			case CLAMP:
				
				if(Mouse.isClamped()) {
					Mouse.unclamp();
				} else {
					Mouse.clamp();
				}
				
				break;
			
			case SAVE:
				if(keyEvent.isControlDown()) saveTerrain("terrain");
				break;
				
			case PAUSE:
				if(pauseScreen.isVisible())
					pauseScreen.hide();
				else
					pauseScreen.show();
				break;
				
			default:
				break;
			}
			
		});
		
		root.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
			
			InputAction action = (InputAction) client.getInputBindings().get(mouseEvent.getButton());
			
			switch(action) {
			
			case PLACE:
				placeableComp.setPlaceRequested(true);
				break;
				
			case REMOVE:
				placeableComp.setRemoveRequested(true);
				break;
			
			case EXTRACT:
				placeableComp.setExtractRequested(true);
				break;
				
			default:
				break;
			
			}
			
		});
		
		root.addEventHandler(ScrollEvent.SCROLL, e -> {
			
			double scaleFactor = e.getDeltaY() / 100d;
			
			cameraView.setPreferredScaleX(cameraTransform.getScaleX() + scaleFactor);
			cameraView.setPreferredScaleY(cameraTransform.getScaleY() + scaleFactor);
			
		});
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, movementControlSystem);
		root.addEventHandler(KeyEvent.KEY_RELEASED, movementControlSystem);
		
		Button optionsButton = new Button("Options");
		optionsButton.setMaxWidth(Double.MAX_VALUE);
		
		int row = pauseScreen.getRowConstraints().size() / 2;
		int column = (pauseScreen.getColumnConstraints().size() - 1) / 2;
		
		pauseScreen.getRowConstraints().add(row, pauseScreen.getRowConstraints().get(row));
		pauseScreen.add(optionsButton, column, row);
		pauseScreen.getChildren().remove(pauseScreen.getMainMenu());
		pauseScreen.add(pauseScreen.getMainMenu(), column, row + 1);
		
		optionsButton.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(OptionState.class)));
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(MenuState.class)));
		
		pauseScreen.visibleProperty().addListener((e, prevVisible, currentlyVisible) -> {
			if(currentlyVisible) Mouse.unclamp();
		});
		
		root.widthProperty().addListener(e -> cameraView.getRequestedViewport().setWidth(root.getWidth()));
		root.heightProperty().addListener(e -> cameraView.getRequestedViewport().setHeight(root.getHeight()));
		
		pauseScreen.visibleProperty().addListener(e -> game.setPaused(pauseScreen.isVisible()));
		
	}
	
	private void saveTerrain(String terrainName) {
		game.addTask(new Task<Void>() {
			protected Void call() throws Exception {
				
				loader.saveTerrain(terrainFileParser, terrainName);
				
				return null;
			}
		});
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		game.addTask(new Task<Void>() {
			protected Void call() throws Exception {
				
				loader = game.getLoaders().get(CustomLoader.class);
				terrainFileParser = game.getFileParsers().get(TerrainFileParser.class);
				
				loader.loadTerrain(terrainFileParser, world, "terrain");
				
				return null;
			}
		});
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		pauseScreen.hide();
		
	}
	
}
