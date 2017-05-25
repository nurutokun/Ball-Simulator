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
import com.rawad.ballsimulator.game.RenderingSystem;
import com.rawad.ballsimulator.game.World;
import com.rawad.ballsimulator.game.event.EntityExtractionEvent;
import com.rawad.ballsimulator.game.event.EventType;
import com.rawad.ballsimulator.game.event.MovementControlHandler;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.client.states.State;
import com.rawad.gamehelpers.client.states.StateChangeRequest;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.jfxengine.client.input.Mouse;
import com.rawad.jfxengine.gui.GuiRegister;
import com.rawad.jfxengine.gui.Root;

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
	
	private World world;
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	private Entity camera;
	private TransformComponent cameraTransform;
	private UserViewComponent cameraView;
	
	private Entity placeable;
	private TransformComponent toBePlacedTransform;
	private PlaceableComponent placeableComp;
	
	private Loader loader;
	private TerrainFileParser terrainFileParser;
	
	@FXML private PauseScreen pauseScreen;
	
	@FXML private ComboBox<Double> widthSelector;
	@FXML private ComboBox<Double> heightSelector;
	
	@Override
	public void init() {
		
		world = new World(gameEngine.getEntities());
		
		client = game.getProxies().get(Client.class);
		
		loader = client.getLoaders().get(Loader.class);
		terrainFileParser = client.getFileParsers().get(TerrainFileParser.class);
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		camera.addComponent(new MovementComponent());
		camera.addComponent(new UserControlComponent());
		
		cameraTransform = camera.getComponent(TransformComponent.class);
		
		cameraTransform.setScaleX(1d);
		cameraTransform.setScaleY(1d);
		
		cameraTransform.setMaxScaleX(5d);
		cameraTransform.setMaxScaleY(5d);
		
		cameraView = camera.getComponent(UserViewComponent.class);
		
		worldRender = new WorldRender(world, camera);
		debugRender = new DebugRender(world, client.getRenderingTimer(), camera);
		
		masterRender.getRenders().put(worldRender);
		masterRender.getRenders().put(debugRender);
		
		gameEngine.addGameSystem(new CameraRoamingSystem(eventManager, true, world.getWidth(), world.getHeight()));
		gameEngine.addGameSystem(new CollisionSystem(eventManager, world.getWidth(), world.getHeight()));
		gameEngine.addGameSystem(new EntitySelectionSystem(cameraTransform));
		gameEngine.addGameSystem(new EntityPlacementSystem(gameEngine, world, cameraTransform));
		gameEngine.addGameSystem(new RenderingSystem(worldRender));
		
		placeable = Entity.createEntity(EEntity.PLACEABLE);
		toBePlacedTransform = placeable.getComponent(TransformComponent.class);
		
		placeableComp = placeable.getComponent(PlaceableComponent.class);
		
		placeableComp.setToPlace(EEntity.STATIC);// Can be any other entity too. < and V
		placeable.getComponent(SelectionComponent.class).setSelected(true);
		placeable.getComponent(RenderingComponent.class).setTexture(GameTextures.findTexture(EEntity.STATIC));
		
		eventManager.registerListener(EventType.ENTITY_EXTRACT, (ev) -> {
			
			EntityExtractionEvent extractionEvent = (EntityExtractionEvent) ev;
			
			Entity e = extractionEvent.getEntityToExtract();
			
			if(!placeable.equals(e)) return;
			
			PlaceableComponent placeableComp = e.getComponent(PlaceableComponent.class);
			
			Entity toExtract = placeableComp.getToExtract();
			
			TransformComponent component = toExtract.getComponent(TransformComponent.class);
			
			final double scaleX = component.getScaleX();
			final double scaleY = component.getScaleY();
			
			Platform.runLater(() -> {
				
				widthSelector.getSelectionModel().select(scaleX);
				heightSelector.getSelectionModel().select(scaleY);
				
			});
			
		});
		
		Root root = GuiRegister.loadGui(this);
		
		widthSelector.setFocusTraversable(false);// Mainly for when in pause screen.
		heightSelector.setFocusTraversable(false);
		
		widthSelector.getItems().addAll(TerrainFileParser.DIMS);
		heightSelector.getItems().addAll(TerrainFileParser.DIMS);
		
		toBePlacedTransform.scaleXProperty().bind(widthSelector.getSelectionModel().selectedItemProperty());
		toBePlacedTransform.scaleYProperty().bind(heightSelector.getSelectionModel().selectedItemProperty());
		
		widthSelector.getSelectionModel().select(TerrainFileParser.DIMS.length / 2);
		heightSelector.getSelectionModel().select(TerrainFileParser.DIMS.length / 2);
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			Object action = client.getInputBindings().get(keyEvent.getCode());
			
			if(!(action instanceof InputAction)) return;
			
			switch((InputAction) action) {
			
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
		
		MovementControlHandler movementControlSystem = new MovementControlHandler(game.getGameEngine(), 
				client.getInputBindings(), camera);
		
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
	
	@Override
	public void terminate() {}
	
	@Override
	protected void onActivate() {
		
		world.addEntity(camera);
		world.addEntity(placeable);
		
		Loader.addTask(new Task<Void>() {
			protected Void call() throws Exception {
				
				loader.loadTerrain(terrainFileParser, world, "terrain");
				
				return null;
			}
		});
		
	}
	
	@Override
	protected void onDeactivate() {
		pauseScreen.hide();
	}
	
	private void saveTerrain(String terrainName) {
		Loader.addTask(new Task<Void>() {
			protected Void call() throws Exception {
				
				loader.saveTerrain(terrainFileParser, world, terrainName);
				
				return null;
			}
		});
		
	}
	
}
