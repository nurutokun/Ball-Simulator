package com.rawad.ballsimulator.gamestates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.ballsimulator.world.World;
import com.rawad.ballsimulator.world.terrain.TerrainComponent;
import com.rawad.gamehelpers.displaymanager.DisplayManager;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gamestates.StateEnum;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.MouseEvent;

public class WorldEditorState extends State {
	
	private Camera camera;
	
	private World world;
	
	private TerrainComponent comp;
	
	private PauseOverlay pauseScreen;
	
	private int x;
	private int y;
	
	public WorldEditorState() {
		super(StateEnum.WORLDEDITOR_STATE);
		
		camera = new Camera(null);
		
		world = loadWorld();
		
		String[] dims = {"2", "4", "8", "16", "32", "64", "128", "256", "512"};
		
		comp = new TerrainComponent(0, 0, Integer.valueOf(dims[0]), Integer.valueOf(dims[0]));
		
		addGuiComponent(new DropDown("Width", DisplayManager.getScreenWidth() - 64, 32, dims));
		addGuiComponent(new DropDown("Height", DisplayManager.getScreenWidth() - 200, 32, dims));
		
		pauseScreen = new PauseOverlay(Color.GRAY, 0, 0, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
		
		addOverlay(pauseScreen);
		
	}
	
	@Override
	public void update(MouseEvent e) {
		super.update(e);
		
		int x = e.getX();
		int y = e.getY();
		
		checkForGamePause();
		
		if(!pauseScreen.isPaused()) {
			handleKeyInput();
			
			camera.update(this.x, this.y, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
			
			comp.setX(x - (comp.getWidth()/2));
			comp.setY(y - (comp.getHeight()/2));
			
			if(e.isButtonDown() && !e.isConsumed()) {
				
				double compX = comp.getX() + camera.getX();
				double compY = comp.getY() + camera.getY();
				
				if(compX >= 0 && compY >= 0 && compX + comp.getWidth() <= world.getWidth() &&
						compY + comp.getHeight() <= world.getHeight()) {
					world.getTerrain().addTerrainComponent(new TerrainComponent(compX, compY, comp.getWidth(), comp.getHeight()));
				}
				
				e.consume();
				
			}
		}
		
	}
	
	private void checkForGamePause() {
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_ESCAPE)) {
			pauseScreen.setPaused(!pauseScreen.isPaused());
			
			if(pauseScreen.isPaused()) {
				Loader.saveTerrain(world.getTerrain(), "terrain");
			}
			
		}
		
	}
	
	private void handleKeyInput() {
		
		KeyboardInput.setConsumeAfterRequest(false);
		
		boolean up = KeyboardInput.isKeyDown(KeyEvent.VK_W) | KeyboardInput.isKeyDown(KeyEvent.VK_UP);
		boolean down = KeyboardInput.isKeyDown(KeyEvent.VK_S) | KeyboardInput.isKeyDown(KeyEvent.VK_DOWN);
		boolean right = KeyboardInput.isKeyDown(KeyEvent.VK_D) | KeyboardInput.isKeyDown(KeyEvent.VK_RIGHT);
		boolean left = KeyboardInput.isKeyDown(KeyEvent.VK_A) | KeyboardInput.isKeyDown(KeyEvent.VK_LEFT);
		
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
		
		KeyboardInput.setConsumeAfterRequest(true);
		
	}
	
	@Override
	public void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Resume":
			
			pauseScreen.setPaused(false);
			
			break;
			
		case "Main Menu":
			
			pauseScreen.setPaused(false);
			sm.setState(StateEnum.MENU_STATE);
			
			break;
		
		}
		
	}
	
	@Override
	public void handleDropDownMenuSelect(DropDown drop) {
		
		switch(drop.getId()) {
		
		case "Width":
			comp.setWidth(Integer.valueOf(drop.getCurrentSelectedItem()));
			break;
			
		case "Height":
			comp.setHeight(Integer.valueOf(drop.getCurrentSelectedItem()));
			break;
		
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		AffineTransform af = g.getTransform();
		
		g.translate(-camera.getX(), -camera.getY());
		
		world.render(g);
		
		g.setTransform(af);
		
		super.render(g);
		
		comp.render(g);
		
		if(pauseScreen.isPaused()) {
			pauseScreen.render(g);
		}
		
	}
	
	private World loadWorld() {
		World re = new World();
		
		re.setTerrain(Loader.loadTerrain("terrain"));
		
		return re;
	}
	
}
