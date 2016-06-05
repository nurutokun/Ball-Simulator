package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.GameTextures;
import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.PlaceableComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.CameraRoamingSystem;
import com.rawad.ballsimulator.game.CollisionSystem;
import com.rawad.ballsimulator.game.EntityPlacementSystem;
import com.rawad.ballsimulator.game.EntitySelectionSystem;
import com.rawad.ballsimulator.game.MovementControlSystem;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.Game;
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
	
	private MovementControlSystem movementControlSystem;
	private CameraRoamingSystem cameraRoamingSystem;
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	private TransformComponent cameraTransform;
	
	private TransformComponent toBePlacedTransform;
	private PlaceableComponent placeableComp;
	
	private CustomLoader loader;
	private TerrainFileParser terrainFileParser;
	
	@FXML private PauseScreen pauseScreen;
	
	@FXML private ComboBox<Double> widthSelector;
	@FXML private ComboBox<Double> heightSelector;
	
	public WorldEditorState() {
		super();
		
		Entity camera = Entity.createEntity(EEntity.CAMERA);
		
		camera.addComponent(new MovementComponent());
		camera.addComponent(new UserControlComponent());
		
		cameraTransform = camera.getComponent(TransformComponent.class);
		
		cameraTransform.setScaleX(1d);
		cameraTransform.setScaleY(1d);
		
		cameraTransform.setMaxScaleX(5D);
		cameraTransform.setMaxScaleY(5D);
		
		world.addEntity(camera);
		
		worldRender = new WorldRender(world, camera);
		debugRender = new DebugRender(camera);
		
		masterRender.registerRender(worldRender);
		masterRender.registerRender(debugRender);
		
		movementControlSystem = new MovementControlSystem();
		cameraRoamingSystem = new CameraRoamingSystem(true, world.getWidth(), world.getHeight());
		
		gameSystems.add(movementControlSystem);
		gameSystems.add(cameraRoamingSystem);
		gameSystems.add(new CollisionSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(new EntitySelectionSystem(cameraTransform));
		gameSystems.add(new EntityPlacementSystem(cameraTransform));
		
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
			
			switch(keyEvent.getCode()) {
			
			case C:
				
				if(Mouse.isClamped()) {
					Mouse.unclamp();
				} else {
					Mouse.clamp();
				}
				
				break;
			
			case S:
				if(keyEvent.isControlDown()) saveTerrain("terrain");
				break;
				
			case ESCAPE:
				pauseScreen.setVisible(!pauseScreen.isVisible());
				break;
				
			default:
				break;
			}
			
		});
		
		root.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
			
			switch(mouseEvent.getButton()) {
			
			case PRIMARY:
				placeableComp.setPlaceRequested(true);
				break;
				
			case SECONDARY:
				placeableComp.setRemoveRequested(true);
				break;
			
			case MIDDLE:
				placeableComp.setExtractRequested(true);
				break;
				
			default:
				break;
			
			}
			
		});
		
		root.addEventHandler(ScrollEvent.SCROLL, e -> {
			
			double scaleFactor = e.getDeltaY() / 100d;
			
			cameraRoamingSystem.requestScaleX(cameraTransform.getScaleX() + scaleFactor);
			cameraRoamingSystem.requestScaleY(cameraTransform.getScaleY() + scaleFactor);
			
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
		
		optionsButton.setOnAction(e -> sm.requestStateChange(OptionState.class));
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(MenuState.class));
		
		pauseScreen.visibleProperty().addListener((e, prevVisible, currentlyVisible) -> {
			
			if(currentlyVisible) {
				Mouse.unclamp();
			}
			
		});
		
		root.widthProperty().addListener(e -> cameraRoamingSystem.requestNewViewportWidth(root.getWidth()));
		root.heightProperty().addListener(e -> cameraRoamingSystem.requestNewViewportHeight(root.getHeight()));
		
		pauseScreen.visibleProperty().addListener(e -> sm.getGame().setPaused(pauseScreen.isVisible()));
		
	}
	
	private void saveTerrain(String terrainName) {
		sm.getClient().addTask(new Task<Integer>() {
			protected Integer call() throws Exception {
				
				loader.saveTerrain(terrainFileParser, terrainName);
				
				return 0;
			}
		});
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		sm.getClient().addTask(new Task<Integer>() {
			protected Integer call() throws Exception {
				
				Game game = sm.getGame();
				
				loader = game.getLoader(CustomLoader.class);
				terrainFileParser = game.getFileParser(TerrainFileParser.class);
				
				loader.loadTerrain(terrainFileParser, world, "terrain");
				
				return 0;
			}
		});
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		Platform.runLater(() -> pauseScreen.setVisible(false));
		
//		saveTerrain("terrain");
		
	}
	
}
