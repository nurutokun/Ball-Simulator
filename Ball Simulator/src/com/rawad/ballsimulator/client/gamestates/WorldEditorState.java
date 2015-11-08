package com.rawad.ballsimulator.client.gamestates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.renderengine.world.WorldRender;
import com.rawad.ballsimulator.client.renderengine.world.terrain.TerrainComponentRender;
import com.rawad.ballsimulator.files.TerrainLoader;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.ballsimulator.world.World;
import com.rawad.ballsimulator.world.terrain.TerrainComponent;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.utils.Util;

public class WorldEditorState extends State {
	
	private static final String[] DIMS = {"2", "4", "8", "16", "32", "64", "128", "256", "512"};
	
	private WorldRender worldRender;
	private TerrainComponentRender tcRender;
	
	private Camera camera;
	
	private World world;
	
	private TerrainComponent comp;
	private TerrainComponent intersectedComp;
	
	private PauseOverlay pauseScreen;
	
	private TerrainLoader terrainLoader;
	
	private int x;
	private int y;
	
	private boolean prevPlaced;
	private boolean prevRemoved;
	
	public WorldEditorState() {
		super(EState.WORLD_EDITOR);
		
		camera = new Camera(null);
		
		comp = new TerrainComponent(0, 0, Util.parseInt(DIMS[0]), Util.parseInt(DIMS[0]));
		comp.setHighlightColor(Color.CYAN);
		
		addGuiComponent(new DropDown("Width", Game.SCREEN_WIDTH - 64, 32, DIMS));
		addGuiComponent(new DropDown("Height", Game.SCREEN_WIDTH - 200, 32, DIMS));
		
		pauseScreen = new PauseOverlay(Color.GRAY);
		
		pauseScreen.addComponent(new Button("Options Menu", 0, 0), 0);
		pauseScreen.alignComponents();
		
		addOverlay(pauseScreen);
		
		prevPlaced = false;
		prevRemoved = false;
		
	}
	
	@Override
	public void update(MouseEvent me, KeyboardEvent ke) {
		
		int x = me.getX();
		int y = me.getY();
		
		checkForGamePause();
		
		if(DisplayManager.isCloseRequested()) {
			saveTerrain("terrain");
		}
		
		if(!pauseScreen.isPaused()) {
			
			super.update(me, ke);
			
			if(!ke.isConsumed()) {
				handleKeyInput(ke);
			}
			
			camera.update(this.x, this.y, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
			
			comp.setX(x - (comp.getWidth()/2));
			comp.setY(y - (comp.getHeight()/2));
			
			if(!me.isConsumed()) {
				handleMouseInput(me);
			}
			
			double mouseDelta = MouseInput.getMouseWheelPosition()/2d;
			
			if(mouseDelta > 0) {
				camera.setScale(camera.getXScale() / mouseDelta, camera.getYScale() / mouseDelta);
			} else if(mouseDelta < 0) {
				mouseDelta *= -1;
				camera.setScale(camera.getXScale() * mouseDelta, camera.getYScale() * mouseDelta);
			}
			
		} else {
			super.updateOverlays(me, ke);// Because we need to update the pause overlay when the game is paused but not 
			//any other components.
			
		}
		
		worldRender.setWorld(world);
		worldRender.setCamera(camera);
		
		tcRender.addComponent(comp);
		
	}
	
	private void checkForGamePause() {
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_ESCAPE, true)) {
			pauseScreen.setPaused(!pauseScreen.isPaused());
			
			if(pauseScreen.isPaused()) {
				saveTerrain("terrain");
			}
			
		}
		
	}
	
	private void handleKeyInput(KeyboardEvent e) {

		KeyboardInput.setConsumeAfterRequest(false);
		
		boolean up = KeyboardInput.isKeyDown(KeyEvent.VK_UP) | KeyboardInput.isKeyDown(KeyEvent.VK_W);
		boolean down = KeyboardInput.isKeyDown(KeyEvent.VK_DOWN) | KeyboardInput.isKeyDown(KeyEvent.VK_S);
		boolean right = KeyboardInput.isKeyDown(KeyEvent.VK_RIGHT) | KeyboardInput.isKeyDown(KeyEvent.VK_D);
		boolean left = KeyboardInput.isKeyDown(KeyEvent.VK_LEFT) | KeyboardInput.isKeyDown(KeyEvent.VK_A);
		
		KeyboardInput.setConsumeAfterRequest(true);
		
		if(up) {
			y -= 5;
		} else if(down) {
			y += 5;
		}
		
		if(right) {
			x += 5;
		} else if(left) {
			x -= 5;
		}
		
	}
	
	private void handleMouseInput(MouseEvent e) {
		
		TerrainComponent prevIntersected = intersectedComp;
		
		intersectedComp = world.getTerrain().calculateCollision(e.getX() + (int) camera.getX(), 
					e.getY() + (int) camera.getY());
		
		if(intersectedComp != prevIntersected) {
			
			if(intersectedComp != null) {
				intersectedComp.setSelected(true);
			} else {
				prevIntersected.setSelected(false);
			}
			
		}
		
		if(!e.isLeftButtonDown()) {
			prevPlaced = false;
		}
		
		if(!e.isRightButtonDown()) {
			prevRemoved = false;
		}
		
		if(e.isLeftButtonDown() && !prevPlaced) {
			
			double compX = comp.getX() + camera.getX();
			double compY = comp.getY() + camera.getY();
			
			if(intersectedComp == null)// For now at least...
			if(compX >= 0 && compY >= 0 && compX + comp.getWidth() <= world.getWidth() &&
					compY + comp.getHeight() <= world.getHeight()) {
				
				world.getTerrain().addTerrainComponent(compX, compY, 
						comp.getWidth(), comp.getHeight());
				
			}
			
			e.consume();
			prevPlaced = true;
			
		} else if(e.isRightButtonDown() && !prevRemoved) {
			
			if(intersectedComp != null) {
				
				world.getTerrain().removeTerrainComponent(intersectedComp);
				
			}
			
			e.consume();
			prevRemoved = true;
			
		}
		
	}
	
	@Override
	public void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Resume":
			
			pauseScreen.setPaused(false);
			
			break;
			
		case "Main Menu":
			
			pauseScreen.setPaused(false);
			sm.setState(EState.MENU);
			
			break;
			
		case "Options Menu":
			
			pauseScreen.setPaused(false);
			sm.setState(EState.OPTION);
			
			break;
			
		}
		
	}
	
	@Override
	public void handleDropDownMenuSelect(DropDown drop) {
		
		switch(drop.getId()) {
		
		case "Width":
			comp.setWidth(Util.parseInt(drop.getCurrentSelectedItem()));
			break;
			
		case "Height":
			comp.setHeight(Util.parseInt(drop.getCurrentSelectedItem()));
			break;
		
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		AffineTransform af = g.getTransform();
		
		g.translate(-camera.getX(), -camera.getY());
		
		g.scale(camera.getXScale(), camera.getYScale());
		
		world.render(g);
		
		g.setTransform(af);
		
		super.render(g);
		
//		comp.render(g);
		
		g.setColor(Color.BLACK);
		g.fillRect((int) (comp.getX() + camera.getX()), (int) (comp.getY() + camera.getY()), 5, 5);
		
		if(pauseScreen.isPaused()) {
//			pauseScreen.render(g);
		}
		
	}
	
	@Override
	protected void onActivate() {
		
		if(world == null) {
			world = loadWorld();
			
			terrainLoader = (TerrainLoader) sm.getGame().getFiles().get(TerrainLoader.class);
			
		}
		
		worldRender = (WorldRender) sm.getGame().getMasterRender().getRender(WorldRender.class);
		tcRender = worldRender.getTerrainComponentRender();
		
	}
	
	private World loadWorld() {
		World re = new World();
		
		re.setTerrain(Loader.loadTerrain(sm.getGame(), "terrain"));
		
		return re;
	}
	
	private void saveTerrain(String terrainName) {
		terrainLoader.setComponents(world.getTerrain().getTerrainComponents());
		
		Loader.saveTerrain(sm.getGame(), world.getTerrain(), "terrain");
	}
	
}
