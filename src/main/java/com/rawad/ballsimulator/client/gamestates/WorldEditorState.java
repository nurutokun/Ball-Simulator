package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.client.renderengine.world.WorldRender;
import com.rawad.ballsimulator.client.renderengine.world.terrain.TerrainComponentRender;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.world.World;
import com.rawad.ballsimulator.world.terrain.Terrain;
import com.rawad.ballsimulator.world.terrain.TerrainComponent;
import com.rawad.gamehelpers.client.IClientController;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.PauseScreen;
import com.rawad.gamehelpers.input.Mouse;
import com.rawad.gamehelpers.renderengine.BackgroundRender;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class WorldEditorState extends State implements IClientController {
	
	private static final Integer[] DIMS = {2, 4, 8, 16, 32, 64, 128, 256, 512};
	
	private Viewport viewport;
	
	private TerrainComponentRender tcRender;
	
	private World world;
	private Camera camera;
	
	private TerrainComponent comp;
	private TerrainComponent intersectedComp;
	
	private CustomLoader loader;
	private TerrainFileParser terrainFileParser;
	
	@FXML private PauseScreen pauseScreen;
	
	@FXML private ComboBox<Integer> widthSelector;
	@FXML private ComboBox<Integer> heightSelector;
	
	private double mouseX;
	private double mouseY;
	
	private double dx;
	private double dy;
	
	private boolean requestPlace;
	private boolean requestRemove;
	
	// TODO: Add "Move TerrainComponent" mode?
	public WorldEditorState() {
		super();
		
		viewport = new Viewport();
		
		world = new World();
		
		camera = new Camera();
		
		comp = new TerrainComponent(0, 0, DIMS[3], DIMS[3]);// Make the default a size
		// you can actually see...
		comp.setHighlightColor(Color.CYAN);
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		widthSelector.setFocusTraversable(false);// Mainly for when in pause screen.
		heightSelector.setFocusTraversable(false);
		
		widthSelector.getItems().addAll(DIMS);
		heightSelector.getItems().addAll(DIMS);
		
		widthSelector.setOnAction(e -> comp.setWidth(widthSelector.getValue()));
		heightSelector.setOnAction(e -> comp.setHeight(heightSelector.getValue()));
		
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
				dy = -5;
				break;
				
			case DOWN:
			case S:
				dy = 5;
				break;
				
			case LEFT:
			case A:
				dx = -5;
				break;
				
			case RIGHT:
			case D:
				dx = 5;
				break;
				
			default:
				break;
			}
			
		});
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			
			switch(keyEvent.getCode()) {
			
			case UP:
			case W:
				if(dy < 0) dy = 0;// Moving up; stop that.
				break;
				
			case DOWN:
			case S:
				if(dy > 0) dy = 0;// Moving Down; stop that.
				break;
				
			case RIGHT:
			case D:
				if(dx > 0) dx = 0;// Moving right; stop that.
				break;
				
			case LEFT:
			case A:
				if(dx < 0) dx = 0;// Moving left; stop that.
				break;
			
			default:
				break;
			
			}
			
		});
		
		EventHandler<MouseEvent> mouseEventHandler = mouseEvent -> {
			
			mouseX = mouseEvent.getX();
			mouseY = mouseEvent.getY();
			
		};
		
		root.addEventHandler(MouseEvent.MOUSE_MOVED, mouseEventHandler);
		root.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEventHandler);
		
		root.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
			
			switch(mouseEvent.getButton()) {
			
			case PRIMARY:
				requestPlace = true;
				break;
				
			case SECONDARY:
				requestRemove = true;
				break;
			
			default:
				break;
			
			}
			
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
	
	@Override
	public void tick() {
		
		if(!pauseScreen.isPaused()) {
			scaleView();
			checkPlacement();
			moveView();
			
			viewport.update(world, camera);
			
		}
		
		tcRender.addComponent(new TerrainComponent(comp.getX() + camera.getX(), 
				comp.getY() + camera.getY(), comp.getWidth(), comp.getHeight()));
		
	}
	
	@Override
	public void render() {
		super.render();
		
		BackgroundRender.instance().render(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight());
		
		viewport.render(canvas);
		
	}
	
	private void updatePosition(double dx, double dy) {
		
		int maxWidth = Game.SCREEN_WIDTH;
		int maxHeight = Game.SCREEN_HEIGHT;
		
		// Casting x/y to int fixes slow sliding at larger zoom levels.
		camera.update((int) (camera.getX() + (maxWidth / camera.getXScale() / 2d + dx)),
				(int) (camera.getY() + (maxHeight / camera.getYScale() / 2d + dy)), 
				(int) (world.getWidth() + maxWidth / camera.getXScale()), 
				(int) (world.getHeight() + maxHeight / camera.getYScale()), 
				-maxWidth, -maxHeight, 
				maxWidth, maxHeight);
		
	}
	
	private void scaleView() {
		
		/*
		double mouseDelta = (double) Mouse.getMouseWheelPosition()/2d;
		
		if(mouseDelta > 0) {
			camera.setScale(camera.getXScale() / mouseDelta, camera.getYScale() / mouseDelta);
			
		} else if(mouseDelta < 0) {
			mouseDelta *= -1;
			camera.setScale(camera.getXScale() * mouseDelta, camera.getYScale() * mouseDelta);
			
		}*/
		
	}
	
	private void moveView() {
		
		double mouseX = this.mouseX / camera.getXScale();// Ensures clamping is done first.
		double mouseY = this.mouseY / camera.getYScale();
		
		if(Mouse.isClamped()) {
			
			mouseX = Mouse.getClampX() / camera.getXScale();
			mouseY = Mouse.getClampY() / camera.getYScale();
			
			updatePosition(Mouse.getDx(), Mouse.getDy());
			
		} else {
			
			updatePosition(dx * camera.getXScale(), dy * camera.getYScale());
			
		}
		
		comp.setX(mouseX - (comp.getWidth()/2d));
		comp.setY(mouseY - (comp.getHeight()/2d));
		
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
				
				world.getTerrain().addTerrainComponent(compX, compY, 
						comp.getWidth(), comp.getHeight());
				
			}
			
			requestPlace = false;
			
		}
		
		if(requestRemove) {
			
			if(intersectedComp != null) {
				
				world.getTerrain().removeTerrainComponent(intersectedComp);
				
			}
			
			requestRemove = false;
			
		}
		
	}
	
	private void saveTerrain(String terrainName) {
		
		Terrain terrain = world.getTerrain();
		
		terrainFileParser.setTerrain(terrain);
		
		loader.saveTerrain(terrainFileParser, terrainName);
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		Game game = sm.getGame();
		
		loader = game.getLoader(CustomLoader.class);
		terrainFileParser = game.getFileParser(TerrainFileParser.class);
		
		world.setTerrain(loader.loadTerrain(terrainFileParser, "terrain"));
		
		viewport.setWorld(world);
		viewport.setCamera(camera);
		
		tcRender = viewport.getMasterRender().getRender(WorldRender.class).getTerrainComponentRender();
		
		pauseScreen.setPaused(false);
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		saveTerrain("terrain");
		
	}
	
}
