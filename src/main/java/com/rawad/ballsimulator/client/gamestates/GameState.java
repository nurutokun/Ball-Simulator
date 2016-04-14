package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.client.IClientController;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.PauseScreen;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GameState extends State implements IClientController {
	
	private Viewport viewport;
	
	private World world;
	private Camera camera;
	
	private EntityPlayer player;
	
	@FXML private PauseScreen pauseScreen;
	
	@FXML private PlayerInventory inventory;
	
	@FXML private Messenger mess;
	
	private DebugRender debugRender;
	
	private boolean showEntireWorld;
	
	public GameState() {
		super();
		
		viewport = new Viewport();
		
		world = new World();
		
		player = new EntityPlayer(world);
		
		camera = new Camera();
		
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
					world.generateCoordinates(player);
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
					camera.setTheta(0);
					break;
					
				case ENTER:
					if(mess.isShowing()) break;
				case T:
					if(!inventory.isVisible()) mess.setShowing(true);
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
						(int) (mouseEvent.getX() / root.getWidth() * (double) Game.SCREEN_WIDTH / camera.getXScale() 
								+ camera.getX()), 
						(int) (mouseEvent.getY() / root.getHeight() * (double) Game.SCREEN_HEIGHT / camera.getYScale() 
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
			updateGameLogic();
			
			viewport.update(world, camera);
			
		}
		
	}
	
	@Override
	public void render() {
		super.render();
		
		viewport.render(canvas, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		
	}
	
	private void updateGameLogic() {
		
		if(showEntireWorld) {
			camera.setScale((double) Game.SCREEN_WIDTH / (double) world.getWidth(), 
				(double) Game.SCREEN_HEIGHT / (double) world.getHeight());
		} else {
			camera.setScale(1d/2d, 1d/2d);
		}
		
		world.update();
		
		camera.update(player.getX(), player.getY(), world.getWidth(), world.getHeight(), 0, 0, 
				Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		CustomLoader loader = sm.getGame().getLoader(CustomLoader.class);
		
		TerrainFileParser parser = sm.getGame().getFileParser(TerrainFileParser.class);
		
		world.setTerrain(loader.loadTerrain(parser, "terrain"));
		world.generateCoordinates(player);
		
		viewport.setWorld(world);
		viewport.setCamera(camera);
		
		pauseScreen.setPaused(false);
		
	}
	
}
