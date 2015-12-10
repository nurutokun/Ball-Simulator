package com.rawad.ballsimulator.client.gamestates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.world.WorldRender;
import com.rawad.ballsimulator.client.renderengine.world.terrain.TerrainComponentRender;
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
	
	private double x;
	private double y;
	
	private boolean prevPlaced;
	private boolean prevRemoved;
	
	public WorldEditorState() {
		super(EState.WORLD_EDITOR);
		
		camera = new Camera(null);
		
		comp = new TerrainComponent(0, 0, Util.parseInt(DIMS[3]), Util.parseInt(DIMS[3]));// Make the default a size
		// you can actually see...
		comp.setHighlightColor(Color.CYAN);
		
		addGuiComponent(new DropDown("Width", Game.SCREEN_WIDTH - 64, 32, 3, DIMS));
		addGuiComponent(new DropDown("Height", Game.SCREEN_WIDTH - 200, 32, 3, DIMS));
		
		pauseScreen = new PauseOverlay(Color.GRAY);
		
		pauseScreen.addComponent(new Button("Options Menu", 0, 0), 1);
		pauseScreen.alignComponents();
		
		addOverlay(pauseScreen);
		
		prevPlaced = false;
		prevRemoved = false;
		
	}
	
	@Override
	public void update(MouseEvent me, KeyboardEvent ke) {
		
		checkForGamePause();
		
		if(DisplayManager.isCloseRequested()) {
			saveTerrain("terrain");
		}
		
		if(!pauseScreen.isPaused()) {
			
			super.update(me, ke);
			
			updateView(me, ke);
			
			if(!me.isConsumed()) {
				handleMouseInput(me);
			}
			
			camera.update(this.x, this.y, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
			
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
		
		tcRender.addComponent(new TerrainComponent(comp.getX() + camera.getX() / camera.getXScale(), 
				comp.getY() + camera.getY() / camera.getYScale(), comp.getWidth(), comp.getHeight()));
		
		((DebugRender) sm.getGame().getMasterRender().getRender(DebugRender.class)).setCamera(camera);
		
	}
	
	private void checkForGamePause() {
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_ESCAPE, true)) {
			pauseScreen.setPaused(!pauseScreen.isPaused());
			
			if(pauseScreen.isPaused()) {
				saveTerrain("terrain");
				
				MouseInput.setClamped(false, 0, 0);
				
			}
			
		}
		
	}
	
	/**
	 * 
	 * @param x Mouse X
	 * @param y Mouse Y
	 * @param ke
	 */
	private void updateView(MouseEvent me, KeyboardEvent ke) {
		
		boolean clampMouse = KeyboardInput.isKeyDown(KeyEvent.VK_C);
		
		if(clampMouse) {
			MouseInput.setClamped(!MouseInput.isClamped(), Game.SCREEN_WIDTH/2, Game.SCREEN_HEIGHT/2);
		}
		
		KeyboardInput.setConsumeAfterRequest(false);
		
		boolean up = KeyboardInput.isKeyDown(KeyEvent.VK_UP) | KeyboardInput.isKeyDown(KeyEvent.VK_W);
		boolean down = KeyboardInput.isKeyDown(KeyEvent.VK_DOWN) | KeyboardInput.isKeyDown(KeyEvent.VK_S);
		boolean right = KeyboardInput.isKeyDown(KeyEvent.VK_RIGHT) | KeyboardInput.isKeyDown(KeyEvent.VK_D);
		boolean left = KeyboardInput.isKeyDown(KeyEvent.VK_LEFT) | KeyboardInput.isKeyDown(KeyEvent.VK_A);
		
		KeyboardInput.setConsumeAfterRequest(true);
		
		if(MouseInput.isClamped()) {
			updatePosition(me.getX(), me.getY());// Should x/y be scaled to move based on zoom?
			
			me.setX(Game.SCREEN_WIDTH/2);
			me.setY(Game.SCREEN_HEIGHT/2);
			
		} else {
			
			updatePosition(right? 5:left? -5:0, up? -5:down? 5:0);
			
		}
		
		double x = me.getX() / camera.getXScale();
		double y = me.getY() / camera.getYScale();
		
		comp.setX(x - (comp.getWidth()/2d));
		comp.setY(y - (comp.getHeight()/2d));
		
	}
	
	private void updatePosition(double dx, double dy) {
		
		this.x += dx;
		this.y += dy;
		
	}
	
	private void handleMouseInput(MouseEvent e) {
		
		int mouseX = (int) (e.getX() / camera.getXScale());
		int mouseY = (int) (e.getY() / camera.getYScale());
		// Use this.x and this.y => make all calculations relative to these.
		
		int camX = (int) (camera.getX() / camera.getXScale());
		int camY = (int) (camera.getY() / camera.getYScale());
		
		TerrainComponent prevIntersected = intersectedComp;
		
		intersectedComp = world.getTerrain().calculateCollision(mouseX + camX, 
					mouseY + camY);
		
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
			
			double compX = comp.getX() + (camX);
			double compY = comp.getY() + (camY);
			
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
		
		double dx = camera.getX()/camera.getXScale();
		double dy = camera.getY()/camera.getYScale();
		
		g.scale(camera.getXScale(), camera.getYScale());
		
		g.translate(-dx, -dy);
		
		world.render(g);
		
		g.translate(dx, dy);
		
		comp.render(g, Color.BLUE);
		
		g.setTransform(af);
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		if(world == null) {
			world = loadWorld();
			
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
		Loader.saveTerrain(sm.getGame(), world.getTerrain(), "terrain");
	}
	
}
