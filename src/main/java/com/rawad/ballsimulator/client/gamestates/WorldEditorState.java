package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.CameraRoamingSystem;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.client.IClientController;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class WorldEditorState extends State implements IClientController {
	
	private WorldRender worldRender;
	private DebugRender debugRender;
	
	private UserViewComponent userView;
	private TransformComponent cameraTransform;
	private MovementComponent cameraMovement;
	
//	private TerrainComponent comp;
//	private TerrainComponent intersectedComp;
	
	private CustomLoader loader;
	private TerrainFileParser terrainFileParser;
	
	@FXML private PauseScreen pauseScreen;
	
	@FXML private ComboBox<Double> widthSelector;
	@FXML private ComboBox<Double> heightSelector;
	
	private boolean requestPlace;
	private boolean requestRemove;
	private boolean requestSelect;
	
	public WorldEditorState() {
		super();
		
		Entity camera = Entity.createEntity(EEntity.USER_CONTROLLABLE_CAMERA);
		
		userView = camera.getComponent(UserViewComponent.class);
		cameraTransform = camera.getComponent(TransformComponent.class);
		cameraMovement = camera.getComponent(MovementComponent.class);
		
		cameraTransform.setMaxScaleX(5D);
		cameraTransform.setMaxScaleY(5D);
		
		world.addEntity(camera);
		
		worldRender = new WorldRender(world, camera);
		debugRender = new DebugRender(camera);
		
		masterRender.registerRender(worldRender);
		masterRender.registerRender(debugRender);
		
		gameSystems.add(new CameraRoamingSystem(world.getWidth(), world.getHeight()));
		
//		comp = new TerrainComponent(0, 0, DIMS[3], DIMS[3]);// Make the default a size you can actually see...
//		comp.setHighlightColor(Color.CYAN);
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		widthSelector.setFocusTraversable(false);// Mainly for when in pause screen.
		heightSelector.setFocusTraversable(false);
		
		widthSelector.getItems().addAll(TerrainFileParser.DIMS);
		heightSelector.getItems().addAll(TerrainFileParser.DIMS);
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			switch(keyEvent.getCode()) {
			
			case C:
				
				if(Mouse.isClamped()) {
					Mouse.unclamp();
				} else {
					Mouse.clamp(root);
				}
				
				break;
			
			case ESCAPE:
				pauseScreen.setPaused(!pauseScreen.isPaused());
				break;
				
			case UP:
			case W:
				cameraMovement.setUp(true);
				break;
				
			case DOWN:
			case S:
				cameraMovement.setDown(true);
				break;
				
			case RIGHT:
			case D:
				cameraMovement.setRight(true);
				break;
				
			case LEFT:
			case A:
				cameraMovement.setLeft(true);
				break;
				
			default:
				break;
			}
			
		});
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			
			switch(keyEvent.getCode()) {
			
			case UP:
			case W:
				cameraMovement.setUp(false);
				break;
				
			case DOWN:
			case S:
				cameraMovement.setDown(false);
				break;
				
			case RIGHT:
			case D:
				cameraMovement.setRight(false);
				break;
				
			case LEFT:
			case A:
				cameraMovement.setLeft(false);
				break;
			
			default:
				break;
			
			}
			
		});
		
		root.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
			
			switch(mouseEvent.getButton()) {
			
			case PRIMARY:
				requestPlace = true;
				break;
				
			case SECONDARY:
				requestRemove = true;
				break;
			
			case MIDDLE:
				requestSelect = true;
				break;
				
			default:
				break;
			
			}
			
		});
		
		root.addEventHandler(ScrollEvent.SCROLL, e -> {
			
			double scaleFactor = e.getDeltaY() / 100d;
			
			cameraTransform.setScaleX(cameraTransform.getScaleX() + scaleFactor);
			cameraTransform.setScaleY(cameraTransform.getScaleY() + scaleFactor);
			
		});
		
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
		
		Rectangle viewport = userView.getViewport();
		viewport.widthProperty().bind(root.widthProperty());
		viewport.heightProperty().bind(root.heightProperty());
		
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
	
	private void moveView() {
		
		double mouseX = Mouse.getX();
		double mouseY = Mouse.getY();;
		
		if(Mouse.isClamped()) {
			
			mouseX = Mouse.getClampX();
			mouseY = Mouse.getClampY();
			
			camera.setX(camera.getX() + Mouse.getDx());
			camera.setY(camera.getY() + Mouse.getDy());
			
		} else {
			
			camera.update(up, down, right, left);
			
		}
		
		comp.setX(mouseX / camera.getScaleX() - (comp.getWidth()/2d));
		comp.setY(mouseY / camera.getScaleY() - (comp.getHeight()/2d));
		
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
				
				loader.saveTerrain(terrainFileParser, terrainName);// TODO: add/remove sttic entities to/from world AND 
				// terrainFileParser.
				
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
		
//		tcRender = masterRender.getRender(WorldRender.class).getTerrainComponentRender();
		
		pauseScreen.setPaused(false);
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		saveTerrain("terrain");
		
	}
	
	@Override
	public World getWorld() {
		return world;
	}
	
}
