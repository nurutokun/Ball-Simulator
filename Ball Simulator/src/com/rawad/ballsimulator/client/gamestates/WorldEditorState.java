package com.rawad.ballsimulator.client.gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.world.WorldRender;
import com.rawad.ballsimulator.client.renderengine.world.terrain.TerrainComponentRender;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.world.World;
import com.rawad.ballsimulator.world.terrain.Terrain;
import com.rawad.ballsimulator.world.terrain.TerrainComponent;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.renderengine.BackgroundRender;
import com.rawad.gamehelpers.utils.Util;

public class WorldEditorState extends State {
	
	private static final String[] DIMS = {"2", "4", "8", "16", "32", "64", "128", "256", "512"};
	
	private WorldRender worldRender;
	private TerrainComponentRender tcRender;
	private DebugRender debugRender;
	
	private Camera camera;
	
	private World world;
	
	private TerrainComponent comp;// TODO: Fix comp getting (0,0) after unpause.
	private TerrainComponent intersectedComp;
	
	private CustomLoader loader;
	private TerrainFileParser terrainFileParser;
	
	private JPanel mainCard;
	
	private DropDown heightSelector;
	private DropDown widthSelector;
	
	private PauseOverlay pauseScreen;
	
	private AbstractAction pauseAction;
	
	private boolean prevPlaced;
	private boolean prevRemoved;
	
	// TODO: Add "Move TerrainComponent" mode?
	public WorldEditorState() {
		super(EState.WORLD_EDITOR);
		
		camera = new Camera(null);
		
		comp = new TerrainComponent(0, 0, Util.parseInt(DIMS[3]), Util.parseInt(DIMS[3]));// Make the default a size
		// you can actually see...
		comp.setHighlightColor(Color.CYAN);
		
		worldRender = new WorldRender();
		tcRender = worldRender.getTerrainComponentRender();
		
		prevPlaced = false;
		prevRemoved = false;
		
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected void initialize() {
		super.initialize();
		
		pauseScreen = new PauseOverlay();
		
		addOverlay(pauseScreen);
		
		mainCard = new JPanel() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 3532511606383035732L;
			
			{
				setIgnoreRepaint(true);
			}
			
			@Override
			public void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				
				Graphics2D g = (Graphics2D) graphics;
				
				BackgroundRender.instance().render(g);
				
				double scaleX = (double) DisplayManager.getDisplayWidth() / (double) Game.SCREEN_WIDTH;
				double scaleY = (double) DisplayManager.getDisplayHeight() / (double) Game.SCREEN_HEIGHT;
				
				g.scale(scaleX, scaleY);
				
				worldRender.render(g);

//				tcRender.addComponent(comp);
//				tcRender.render(g);
				
				debugRender.render(g);
				
				g.scale(1d/scaleX, 1d/scaleY);
				
			}
			
		};
		
		mainCard.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(20dlu;default):grow"),
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("5dlu"),
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("5dlu"),},
			new RowSpec[] {
				RowSpec.decode("max(10dlu;min)"),
				RowSpec.decode("fill:pref"),}));
		
		widthSelector = new DropDown("Width", DIMS[3], DIMS);
		mainCard.add(widthSelector, "2, 2, fill, fill");
		
		heightSelector = new DropDown("Height", DIMS[3], DIMS);
		mainCard.add(heightSelector, "4, 2, fill, fill");
		
		InputMap input = mainCard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap action = mainCard.getActionMap();
		
		pauseAction = new AbstractAction() {
			
			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = 5131682721309115959L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pauseScreen.setActive(!pauseScreen.isActive());
			}
			
		};
		
		input.put(KeyStroke.getKeyStroke("ESCAPE"), "doPause");
		action.put("doPause", pauseAction);
		
		pauseScreen.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "doPause");
		pauseScreen.getActionMap().put("doPause", pauseAction);
		
		container.add(mainCard, "Main Card");
		
	}
	
	@Override
	public void update() {
		
		checkForGamePause();
		
		if(DisplayManager.isCloseRequested()) {
			saveTerrain("terrain");
		}
		
		if(!pauseScreen.isActive()) {
			
			updateView();
			
			handleMouseInput();
			
			double mouseDelta = MouseInput.getMouseWheelPosition()/2d;
			
			if(mouseDelta > 0) {
				camera.setScale(camera.getXScale() / mouseDelta, camera.getYScale() / mouseDelta);
			} else if(mouseDelta < 0) {
				mouseDelta *= -1;
				camera.setScale(camera.getXScale() * mouseDelta, camera.getYScale() * mouseDelta);
			}
			
		}
		
		worldRender.setWorld(world);
		worldRender.setCamera(camera);
		
		tcRender.addComponent(new TerrainComponent(comp.getX() + camera.getX(), 
				comp.getY() + camera.getY(), comp.getWidth(), comp.getHeight()));
		
		debugRender.setCamera(camera);
		
		mainCard.repaint();
		
	}
	
	private void checkForGamePause() {
		
		if(pauseScreen.isActive()) {
			
			saveTerrain("terrain");
			
			MouseInput.setClamped(false, 0, 0);
			
			show(pauseScreen.getId());
			
		} else {
			
			show("Main Card");
			
		}
		
	}
	
	private void updateView() {
		
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
		
		double x = MouseInput.getX(true);
		double y = MouseInput.getY(true);
		
		if(MouseInput.isClamped()) {
			updatePosition(x, y);// Should x/y be scaled to move based on zoom?
			
		} else {
			
			updatePosition(right? 5:left? -5:0, up? -5:down? 5:0);
			
		}
		
		x /= camera.getXScale();
		y /= camera.getYScale();
		
		comp.setX(x - (comp.getWidth()/2d));
		comp.setY(y - (comp.getHeight()/2d));
		
	}
	
	private void updatePosition(double dx, double dy) {
		
		camera.setX(camera.getX() + dx);
		camera.setY(camera.getY() + dy);
		
	}
	
	private void handleMouseInput() {
		
		int mouseX = (int) (MouseInput.getX(true) / camera.getXScale());
		int mouseY = (int) (MouseInput.getY(true) / camera.getYScale());
		
		int camX = (int) camera.getX();
		int camY = (int) camera.getY();
		
		TerrainComponent prevIntersected = intersectedComp;
		
		intersectedComp = world.getTerrain().calculateCollision(mouseX + camX, mouseY + camY);
		
		if(intersectedComp != prevIntersected) {
			
			if(intersectedComp != null) {
				intersectedComp.setSelected(true);
			} else {
				prevIntersected.setSelected(false);
			}
			
		}
		
		boolean leftButtonDown = MouseInput.isButtonDown(MouseInput.LEFT_MOUSE_BUTTON);
		boolean rightButtonDown = MouseInput.isButtonDown(MouseInput.RIGHT_MOUSE_BUTTON);
		
		if(!leftButtonDown) {
			prevPlaced = false;
		}
		
		if(!rightButtonDown) {
			prevRemoved = false;
		}
		
		if(leftButtonDown && !prevPlaced) {
			
			double compX = comp.getX() + (camX);
			double compY = comp.getY() + (camY);
			
			if(intersectedComp == null)// For now at least...
			if(compX >= 0 && compY >= 0 && compX + comp.getWidth() <= world.getWidth() &&
					compY + comp.getHeight() <= world.getHeight()) {
				
				world.getTerrain().addTerrainComponent(compX, compY, 
						comp.getWidth(), comp.getHeight());
				
			}
			
			prevPlaced = true;
			
		} else if(rightButtonDown && !prevRemoved) {
			
			if(intersectedComp != null) {
				
				world.getTerrain().removeTerrainComponent(intersectedComp);
				
			}
			
			prevRemoved = true;
			
		}
		
	}
	
	@Override
	public void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Resume":
			
			pauseScreen.setActive(false);
			
			break;
			
		case "Main Menu":
			
			sm.setState(EState.MENU);
			
			break;
			
		case "Options Menu":
			
			sm.setState(EState.OPTION);
			
			break;
			
		}
		
	}
	
	@Override
	public void handleDropDownMenuSelect(DropDown drop) {
		
		switch(drop.getId()) {
		
		case "Width":
			comp.setWidth(Util.parseInt((String) drop.getSelectedItem()));
			break;
			
		case "Height":
			comp.setHeight(Util.parseInt((String) drop.getSelectedItem()));
			break;
			
		}
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		Game game = sm.getGame();
		
		loader = game.getLoader(game.toString());
		terrainFileParser = game.getFileParser(TerrainFileParser.class);
		
		if(world == null) {
			world = loadWorld();
			
		}
		
		debugRender = game.getMasterRender().getRender(DebugRender.class);
		
		pauseScreen.setActive(false);
		
		show("Main Card");
		
	}
	
	private World loadWorld() {
		World re = new World();
		
		re.setTerrain(loader.loadTerrain(terrainFileParser, "terrain"));
		
		return re;
	}
	
	private void saveTerrain(String terrainName) {
		
		Terrain terrain = world.getTerrain();
		
		terrainFileParser.setTerrain(terrain);
		
		CustomLoader loader = sm.getGame().getLoader(sm.getGame().toString());
		
		loader.saveTerrain(terrainFileParser, terrainName);
		
	}
	
}
