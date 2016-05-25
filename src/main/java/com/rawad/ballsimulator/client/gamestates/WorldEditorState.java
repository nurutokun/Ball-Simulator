package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.GameTextures;
import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.PlaceableComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.CameraRoamingSystem;
import com.rawad.ballsimulator.game.EntityPlacementSystem;
import com.rawad.ballsimulator.game.MovementControlSystem;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Point;
import com.rawad.gamehelpers.geometry.Rectangle;
import com.rawad.gamehelpers.utils.Util;

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
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	private UserViewComponent userView;
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
		
		Entity camera = Entity.createEntity(EEntity.USER_CONTROLLABLE_CAMERA);
		
		userView = camera.getComponent(UserViewComponent.class);
		cameraTransform = camera.getComponent(TransformComponent.class);
		
		cameraTransform.setMaxScaleX(5D);
		cameraTransform.setMaxScaleY(5D);
		
		world.addEntity(camera);
		
		worldRender = new WorldRender(world, camera);
		debugRender = new DebugRender(camera);
		
		masterRender.registerRender(worldRender);
		masterRender.registerRender(debugRender);
		
		movementControlSystem = new MovementControlSystem();
		
		gameSystems.add(movementControlSystem);
		gameSystems.add(new CameraRoamingSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(new EntityPlacementSystem(cameraTransform));
		
		Entity toBePlaced = Entity.createEntity(EEntity.PLACEABLE);
		toBePlacedTransform = toBePlaced.getComponent(TransformComponent.class);
		
		placeableComp = toBePlaced.getComponent(PlaceableComponent.class);
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
//				requestRemove = true;// Get intersected entity and remove it?
				break;
			
			case MIDDLE:
				// Take data from interssected entity, put it into current selected one and delete original.
				break;
				
			default:
				break;
			
			}
			
		});
		
		root.addEventHandler(ScrollEvent.SCROLL, e -> {
			
			double scaleFactor = e.getDeltaY() / 100d;
			
			if(scaleFactor > 0 && (cameraTransform.getScaleX() < cameraTransform.getMaxScaleX() && 
					cameraTransform.getScaleY() < cameraTransform.getMaxScaleY())) {// Zooming in.
				
				Point mouseInWorld = cameraTransform.transformFromScreen(e.getX(), e.getY());
				
				Rectangle viewport = userView.getViewport();
				
				cameraTransform.setX(mouseInWorld.getX() - (viewport.getWidth() * cameraTransform.getScaleX()));
				cameraTransform.setY(mouseInWorld.getY() - (viewport.getHeight() * cameraTransform.getScaleY()));
				// TODO: Sort this out ^ ; trying to center view after zooming in.
			}
			
			cameraTransform.setScaleX(cameraTransform.getScaleX() + scaleFactor);
			cameraTransform.setScaleY(cameraTransform.getScaleY() + scaleFactor);
			
			clampCameraScale();
			
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
		
		root.widthProperty().addListener(e -> resizeViewport());
		root.heightProperty().addListener(e -> resizeViewport());
		
		pauseScreen.visibleProperty().addListener(e -> sm.getGame().setPaused(pauseScreen.isVisible()));
		
	}
	
	private void resizeViewport() {
		
		Rectangle viewport = userView.getViewport();
		
		viewport.setWidth(root.getWidth());
		viewport.setHeight(root.getHeight());
		
		clampCameraScale();
		
	}
	
	private void clampCameraScale() {
		
		Rectangle viewport = userView.getViewport();
		
		double minScaleX = viewport.getWidth() / world.getWidth();
		cameraTransform.setScaleX(Util.clamp(cameraTransform.getScaleX(), minScaleX, cameraTransform.getMaxScaleX()));
		
		double minScaleY = viewport.getHeight() / world.getHeight();
		cameraTransform.setScaleY(Util.clamp(cameraTransform.getScaleY(), minScaleY, cameraTransform.getMaxScaleY()));
		
	}
	
	/*/
	@Override
	public void tick() {
		
		if(!pauseScreen.isPaused()) {
			checkPlacement();
			moveView();
			
		}
		
		tcRender.addComponent(new TerrainComponent(comp.getX() + camera.getX(), 
				comp.getY() + camera.getY(), comp.getWidth(), comp.getHeight()));
		
	}
	
	private void checkPlacement() {
		
		double camX = camera.getX();
		double camY = camera.getY();
		
		TerrainComponent prevIntersected = intersectedComp;
		
		intersectedComp = world.getTerrain().calculateCollision((int) (comp.getX() + (comp.getWidth()/2d) + camX), 
				(int) (comp.getY() + (comp.getHeight()/2d) + camY));
				// Use center of component for collision detection with other components (instead of mouse).
		
		if(intersectedComp != prevIntersected) {// If they're different components (from one frame to the next).
			
			if(intersectedComp != null) {// If there is a component being intersected.
				intersectedComp.setSelected(true);
			} else {
				prevIntersected.setSelected(false);
			}
			
		}
		
		if(requestPlace) {
			
			double compX = comp.getX() + camX;
			double compY = comp.getY() + camY;
			
			if(intersectedComp == null)// For now at least...
			if(compX >= 0 && compY >= 0 && compX + comp.getWidth() <= world.getWidth() &&
					compY + comp.getHeight() <= world.getHeight()) {
				
				world.getTerrain().addTerrainComponent(new TerrainComponent(compX, compY, 
						comp.getWidth(), comp.getHeight()));
				
			}
			
			requestPlace = false;
			
		}
		
		if(requestRemove) {
			
			if(intersectedComp != null) {
				
				world.getTerrain().removeTerrainComponent(intersectedComp);
				
			}
			
			requestRemove = false;
			
		}
		
		if(requestSelect) {
			
			if(intersectedComp != null) {
				
				widthSelector.getSelectionModel().select(intersectedComp.getWidth());
				heightSelector.getSelectionModel().select(intersectedComp.getHeight());
				
			}
			
			requestSelect = false;
			
		}
		
	}/**/
	
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
