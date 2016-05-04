package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.PauseScreen;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.entity.RandomPositionComponent;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.PhysicsSystem;
import com.rawad.ballsimulator.game.PlayerControlSystem;
import com.rawad.ballsimulator.game.PositionGenerationSystem;
import com.rawad.ballsimulator.game.RenderingSystem;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.client.IClientController;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.resources.Loader;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class GameState extends State implements IClientController {
	
	private Viewport viewport;
	
	private World world;
	private Camera camera;
	
	private Entity player;
	
	@FXML private PauseScreen pauseScreen;
	
	@FXML private PlayerInventory inventory;
	
	@FXML private Messenger mess;
	
	private DebugRender debugRender;
	
	private boolean showEntireWorld;
	
	public GameState() {
		super();
		
		viewport = new Viewport();
		
		world = new World();
		
		player = new Entity();
		
		camera = new Camera();
		camera.setOuterBounds(new Rectangle(0, 0, world.getWidth(), world.getHeight()));
		
		viewport.update(world, camera);
		
		gameSystems.add(new PositionGenerationSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(new PlayerControlSystem());
		gameSystems.add(new PhysicsSystem(world.getWidth(), world.getHeight()));
		gameSystems.add(new RenderingSystem());
		
		showEntireWorld = false;
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			if(pauseScreen.isPaused()) {
				
				switch(keyEvent.getCode()) {
				
				case ESCAPE:
					pauseScreen.setPaused(false);
					break;
				
				default:
					break;
				}
				
			} else {

				switch(keyEvent.getCode()) {
				
				case ESCAPE:
					
					if(inventory.isVisible()) {
						inventory.setVisible(false);
					} else if(mess.isShowing()) {
						mess.setShowing(false);
					} else {
						pauseScreen.setPaused(true);
					}
					
					break;
					
				case E:
					if(mess.isShowing()) break;
					inventory.setVisible(!inventory.isVisible());
					break;
					
				case R:
					player.getComponent(RandomPositionComponent.class).setGenerateNewPosition(true);
					break;
					
				case L:
					showEntireWorld = !showEntireWorld;
					break;
					
				case C:
					camera.increaseRotation(0.1);
					break;
					
				case Z:
					camera.increaseRotation(-0.1);
					break;
					
				case X:
					camera.setRotation(0);
					break;
					
				case ENTER:
					if(!mess.isShowing() && !inventory.isVisible()) mess.setShowing(true);
					break;
					
				case F5:
					
					String style = Loader.getStyleSheetLocation(Client.class, "StyleSheet");
					
					root.getScene().getStylesheets().remove(style);
					root.getScene().getStylesheets().add(style);
					
					break;
					
				case UP:
				case W:
					player.setUp(true);
					player.setDown(false);
					break;
					
				case LEFT:
				case A:
					player.setLeft(true);
					player.setRight(false);
					break;
					
				case DOWN:
				case S:
					player.setDown(true);
					player.setUp(false);
					break;
					
				case RIGHT:
				case D:
					player.setRight(true);
					player.setLeft(false);
					break;
					
				default:
					break;
				}
			}
			
		});
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			
			switch(keyEvent.getCode()) {
			
			case T:
				if(!pauseScreen.isPaused() && !inventory.isVisible()) mess.setShowing(true);
				break;
			
			case UP:
			case W:
				player.setUp(false);
				break;
			
			case LEFT:
			case A:
				player.setLeft(false);
				break;
				
			case DOWN:
			case S:
				player.setDown(false);
				break;
				
			case RIGHT:
			case D:
				player.setRight(false);
				break;
			
			default:
				break;
			
			}
			
		});
		
		root.addEventHandler(MouseEvent.ANY, mouseEvent -> {
			
			if(mouseEvent.isPrimaryButtonDown()) {
				if(player.intersects(
						(int) (mouseEvent.getX() / root.getWidth() * (double) Game.SCREEN_WIDTH / camera.getScaleX() 
								+ camera.getX()), 
						(int) (mouseEvent.getY() / root.getHeight() * (double) Game.SCREEN_HEIGHT / camera.getScaleY() 
								+ camera.getY()))) {
					
					player.hit(0.2d);
					
				}
				
			}
			
			debugRender.setMouseX(mouseEvent.getX());
			debugRender.setMouseY(mouseEvent.getY());
			
		});
		
		debugRender = viewport.getMasterRender().getRender(DebugRender.class);
		
		debugRender.widthProperty().bind(root.widthProperty());
		debugRender.heightProperty().bind(root.heightProperty());
		
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(MenuState.class));
		
	}
	
	@Override
	public void tick() {
		
		if(!pauseScreen.isPaused() && !inventory.isVisible()) {
			
			if(showEntireWorld) {
				camera.setScale(Double.MIN_VALUE, Double.MIN_VALUE);
			} else {
				camera.setScale(1d/2d, 1d/2d);
			}
			
			world.update();
			
			camera.setX(player.getX() - (Game.SCREEN_WIDTH / camera.getScaleX() / 2d));
			camera.setY(player.getY() - (Game.SCREEN_HEIGHT / camera.getScaleY() / 2d));
			
		}
		
	}
	
	@Override
	public void render() {
		super.render();
		
		viewport.render(canvas, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		sm.getClient().addTask(new Task<Integer>() {
			
			protected Integer call() throws Exception {
				
				CustomLoader loader = sm.getGame().getLoader(CustomLoader.class);
				
				TerrainFileParser parser = sm.getGame().getFileParser(TerrainFileParser.class);
				
				world.setTerrain(loader.loadTerrain(parser, "terrain"));
				world.generateCoordinates(player);
				
				return 0;
				
			}
			
		});
		
		pauseScreen.setPaused(false);
		
	}
	
}
