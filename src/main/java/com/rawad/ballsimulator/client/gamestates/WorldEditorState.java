package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.renderengine.MasterRender;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.RenderingSystem;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.client.IClientController;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.world.World;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Rectangle;

public class WorldEditorState extends State implements IClientController {
	
	private static final Double[] DIMS = {2D, 4D, 8D, 16D, 32D, 64D, 128D, 256D, 512D};
	
	private MasterRender masterRender;
	
	private RenderingSystem renderingSystem;
	
//	private TerrainComponent comp;
//	private TerrainComponent intersectedComp;
	
	private CustomLoader loader;
	private TerrainFileParser terrainFileParser;
	
	@FXML private PauseScreen pauseScreen;
	
	@FXML private ComboBox<Double> widthSelector;
	@FXML private ComboBox<Double> heightSelector;
	
	private boolean up;
	private boolean down;
	private boolean right;
	private boolean left;
	
	private boolean requestPlace;
	private boolean requestRemove;
	private boolean requestSelect;
	
	public WorldEditorState(MasterRender masterRender) {
		super();
		
		this.masterRender = masterRender;
		
		camera.setOuterBounds(new Rectangle(0, 0, world.getWidth(), world.getHeight()));
		
		renderingSystem = new RenderingSystem();
		
		gameSystems.add(renderingSystem);
		
//		comp = new TerrainComponent(0, 0, DIMS[3], DIMS[3]);// Make the default a size you can actually see...
//		comp.setHighlightColor(Color.CYAN);
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		widthSelector.setFocusTraversable(false);// Mainly for when in pause screen.
		heightSelector.setFocusTraversable(false);
		
		widthSelector.getItems().addAll(DIMS);
		heightSelector.getItems().addAll(DIMS);
		
//		comp.widthProperty().bind(widthSelector.getSelectionModel().selectedItemProperty());
//		comp.heightProperty().bind(heightSelector.getSelectionModel().selectedItemProperty());
		
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
				up = true;
				break;
				
			case DOWN:
			case S:
				down = true;
				break;
				
			case LEFT:
			case A:
				left = true;
				break;
				
			case RIGHT:
			case D:
				right = true;
				break;
				
			default:
				break;
			}
			
		});
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			
			switch(keyEvent.getCode()) {
			
			case UP:
			case W:
				up = false;
				break;
				
			case DOWN:
			case S:
				down = false;
				break;
				
			case RIGHT:
			case D:
				right = false;
				break;
				
			case LEFT:
			case A:
				left = false;
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
			
			camera.setScale(scaleFactor + camera.getScaleX(), scaleFactor + camera.getScaleY());
			
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
			
			if(!currentlyVisible) {
				Mouse.unclamp();
			}
			
		});
		
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
		
		masterRender.setRenderingSystem(renderingSystem);
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		saveTerrain("terrain");
		
		masterRender.setRenderingSystem(null);
		
	}
	
	@Override
	public World getWorld() {
		return world;
	}
	
}
