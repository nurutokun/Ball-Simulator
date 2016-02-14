package com.rawad.ballsimulator.client.gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

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
import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.client.renderengine.world.WorldRender;
import com.rawad.ballsimulator.client.renderengine.world.terrain.TerrainComponentRender;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.world.World;
import com.rawad.ballsimulator.world.terrain.Terrain;
import com.rawad.ballsimulator.world.terrain.TerrainComponent;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.IController;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;
import com.rawad.gamehelpers.input.EventHandler;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.Mouse;
import com.rawad.gamehelpers.utils.Util;

public class WorldEditorState extends State implements IController {
	
	private static final String[] DIMS = {"2", "4", "8", "16", "32", "64", "128", "256", "512"};
	
	private Viewport viewport;
	
	private TerrainComponentRender tcRender;
	
	private World world;
	private Camera camera;
	
	private TerrainComponent comp;// TODO: Fix comp getting (0,0) after unpause.
	private TerrainComponent intersectedComp;
	
	private CustomLoader loader;
	private TerrainFileParser terrainFileParser;
	
	private JPanel mainCard;
	
	private DropDown heightSelector;
	private DropDown widthSelector;
	
	private PauseOverlay pauseScreen;
	
	private boolean prevPlaced;
	private boolean prevRemoved;
	
	// TODO: Add "Move TerrainComponent" mode?
	public WorldEditorState() {
		super(EState.WORLD_EDITOR);
		
		viewport = new Viewport();
		
		world = new World();
		
		camera = new Camera();
		
		comp = new TerrainComponent(0, 0, Util.parseInt(DIMS[3]), Util.parseInt(DIMS[3]));// Make the default a size
		// you can actually see...
		comp.setHighlightColor(Color.CYAN);
		
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
			
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				viewport.drawScene(g, getWidth(), getHeight());
				
			}
			
			@Override
			public void transferFocus() {
				container.transferFocus();
			}
			
		};
		
		EventHandler l = EventHandler.instance();
		
		mainCard.addKeyListener(l);
		mainCard.addMouseListener(l);
		mainCard.addMouseMotionListener(l);
		mainCard.addMouseWheelListener(l);
		mainCard.addComponentListener(l);
		
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
		
		InputMap input = mainCard.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap action = mainCard.getActionMap();
		
		input.put(KeyStroke.getKeyStroke("ESCAPE"), pauseScreen.getId());
		action.put(pauseScreen.getId(), pauseScreen.getActiveChanger());
		
		pauseScreen.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), 
				pauseScreen.getId());
		pauseScreen.getActionMap().put(pauseScreen.getId(), pauseScreen.getActiveChanger());
		
		container.add(mainCard, "Main Card");
		
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void tick() {
		
		checkForGamePause();
		
		if(!pauseScreen.isActive()) {
			
			handleKeyboardInput();
			handleMouseInput();
			
			viewport.update(world, camera);
			
			tcRender.addComponent(new TerrainComponent(comp.getX() + camera.getX(), 
					comp.getY() + camera.getY(), comp.getWidth(), comp.getHeight()));
			
			viewport.render();
			
		}
		
	}
	
	@Override
	public void handleMouseInput() {
		
		scaleView();

		checkPlacement();
		
	}
	
	@Override
	public void handleKeyboardInput() {
		
		moveView();
		
	}
	
	private void checkForGamePause() {
		
		if(pauseScreen.isActive()) {
			
			Mouse.setClamped(false, 0, 0);
			
			pauseScreen.setBackground(viewport.getMasterRender().getBuffer());
			
			show(pauseScreen.getId());
			
		} else {
			
			show("Main Card");
			
		}
		
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
		
		double mouseDelta = (double) Mouse.getMouseWheelPosition()/2d;
		
		if(mouseDelta > 0) {
			camera.setScale(camera.getXScale() / mouseDelta, camera.getYScale() / mouseDelta);
			
		} else if(mouseDelta < 0) {
			mouseDelta *= -1;
			camera.setScale(camera.getXScale() * mouseDelta, camera.getYScale() * mouseDelta);
			
		}
		
	}
	
	private void moveView() {
		
		boolean clampMouse = KeyboardInput.isKeyDown(KeyEvent.VK_C);
		
		if(clampMouse) {
			Mouse.setClamped(!Mouse.isClamped(), Game.SCREEN_WIDTH/2, Game.SCREEN_HEIGHT/2);
		}
		
		double mouseX = Mouse.getX(true) / camera.getXScale();// Ensures clamping is done first.
		double mouseY = Mouse.getY(true) / camera.getYScale();
		
		if(Mouse.isClamped()) {
			
			updatePosition(mouseX, mouseY);// Should x/y be scaled to move based on zoom?
			
			mouseX = Mouse.getClampX() / camera.getXScale();
			mouseY = Mouse.getClampY() / camera.getYScale();
			
		} else {
			
			KeyboardInput.setConsumeAfterRequest(false);
			
			boolean up = KeyboardInput.isKeyDown(KeyEvent.VK_UP) | KeyboardInput.isKeyDown(KeyEvent.VK_W);
			boolean down = KeyboardInput.isKeyDown(KeyEvent.VK_DOWN) | KeyboardInput.isKeyDown(KeyEvent.VK_S);
			boolean right = KeyboardInput.isKeyDown(KeyEvent.VK_RIGHT) | KeyboardInput.isKeyDown(KeyEvent.VK_D);
			boolean left = KeyboardInput.isKeyDown(KeyEvent.VK_LEFT) | KeyboardInput.isKeyDown(KeyEvent.VK_A);
			
			KeyboardInput.setConsumeAfterRequest(true);
			
			updatePosition(right? 5:left? -5:0, up? -5:down? 5:0);
			
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
		
		boolean leftButtonDown = Mouse.isButtonDown(Mouse.LEFT_MOUSE_BUTTON);
		boolean rightButtonDown = Mouse.isButtonDown(Mouse.RIGHT_MOUSE_BUTTON);
		
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
			
			sm.requestStateChange(EState.MENU);
			
			break;
			
		case "Options Menu":
			
			sm.requestStateChange(EState.OPTION);
			
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
		
		loader = game.getLoader(CustomLoader.class);
		terrainFileParser = game.getFileParser(TerrainFileParser.class);
		
		world.setTerrain(loader.loadTerrain(terrainFileParser, "terrain"));
		
		game.getProxy().setController(this);
		
		pauseScreen.setActive(false);
		
		viewport.setWorld(world);
		viewport.setCamera(camera);
		
		tcRender = viewport.getMasterRender().getRender(WorldRender.class).getTerrainComponentRender();
		
		show("Main Card");
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		saveTerrain("terrain");
		
	}
	
	private void saveTerrain(String terrainName) {
		
		Terrain terrain = world.getTerrain();
		
		terrainFileParser.setTerrain(terrain);
		
		loader.saveTerrain(terrainFileParser, terrainName);
		
	}
	
}
