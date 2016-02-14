package com.rawad.ballsimulator.client.gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.IController;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.overlay.PauseOverlay;
import com.rawad.gamehelpers.input.EventHandler;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.Mouse;

public class GameState extends State implements IController {
	
	private PauseOverlay pauseScreen;
	
	private JPanel mainCard;
	
	private Viewport viewport;
	
	private World world;
	private Camera camera;
	
	private EntityPlayer player;
	private PlayerInventory inventory;
	
	private boolean showEntireWorld;
	private boolean paused;
	
	public GameState() {
		super(EState.GAME);
		
		viewport = new Viewport();
		
		world = new World();
		
		camera = new Camera();
		
		player = new EntityPlayer(world);
//		player.setName("Player" + (int) (new Random().nextDouble()*999));
		
		showEntireWorld = false;
		paused = false;
		
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected void initialize() {
		super.initialize();
		
		pauseScreen = new PauseOverlay();
		
		addOverlay(pauseScreen);
		
		inventory = new PlayerInventory();
		
		addOverlay(inventory);
		
		mainCard = new JPanel() {

			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = -4873917145932784820L;
			
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				viewport.drawScene(g, mainCard.getWidth(), mainCard.getHeight());
				
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
		
		container.add(mainCard, "Main Card");
		
		initializeKeyBindings();
		
	}
	
	private void initializeKeyBindings() {
		
		InputMap input = mainCard.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap action = mainCard.getActionMap();
		
		input.put(KeyStroke.getKeyStroke("ESCAPE"), pauseScreen.getId());
		action.put(pauseScreen.getId(), pauseScreen.getActiveChanger());
		
		pauseScreen.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), 
				pauseScreen.getId());
		pauseScreen.getActionMap().put(pauseScreen.getId(), pauseScreen.getActiveChanger());
		
		input.put(KeyStroke.getKeyStroke("E"), inventory.getId());
		action.put(inventory.getId(), inventory.getActiveChanger());
		
		inventory.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("E"), 
				inventory.getId());
		inventory.getActionMap().put(inventory.getId(), inventory.getActiveChanger());
		
	}
	
	@Override
	public void update() {
		super.update();
		
	}
	
	@Override
	public void tick() {
		
		if(pauseScreen.isActive()) {
			
			show(pauseScreen.getId());
			
			pauseScreen.setBackground(viewport.getMasterRender().getBuffer());
			
			paused = true;
			
		} else {
			
			if(inventory.isActive()) {
				show(inventory.getId());
				
				inventory.setBackground(viewport.getMasterRender().getBuffer());
				
				paused = true;
				
			} else {
				show("Main Card");
				
				paused = false;
				
			}
			
			handleKeyboardInput();
			handleMouseInput();
			
		}
		
		if(!paused) {
			updateGameLogic();
			
			viewport.update(world, camera);
			
			viewport.render();
			
		}
		
		
	}
	
	private void updateGameLogic() {
		
		if(showEntireWorld) {
			camera.setScale((double) Game.SCREEN_WIDTH / (double) world.getWidth(), 
				(double) Game.SCREEN_HEIGHT / (double) world.getHeight());
		} else {
			camera.setScale(1d/2d, 1d/2d);
		}
		
		KeyboardInput.setConsumeAfterRequest(false);
		
		boolean up = KeyboardInput.isKeyDown(KeyEvent.VK_UP) | KeyboardInput.isKeyDown(KeyEvent.VK_W);
		boolean down = KeyboardInput.isKeyDown(KeyEvent.VK_DOWN) | KeyboardInput.isKeyDown(KeyEvent.VK_S);
		boolean right = KeyboardInput.isKeyDown(KeyEvent.VK_RIGHT) | KeyboardInput.isKeyDown(KeyEvent.VK_D);
		boolean left = KeyboardInput.isKeyDown(KeyEvent.VK_LEFT) | KeyboardInput.isKeyDown(KeyEvent.VK_A);
		
		KeyboardInput.setConsumeAfterRequest(true);
		
		if(up || down || right || left) {
			
			// For buffered key press events.
			player.stopMoving();
			
			if(up) {
				player.moveUp();
			} else if(down) {
				player.moveDown();
			}
			
			if(right) {
				player.moveRight();
			} else if(left) {
				player.moveLeft();
			}
			
		}
		
		world.update();
		
		player.update(Mouse.getX(true) + (int) camera.getX(), Mouse.getY(true) + (int) camera.getY());
		
		camera.update(player.getX(), player.getY(), world.getWidth(), world.getHeight(), 0, 0, 
				Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		
	}
	
	private void handleCameraRotation(boolean rotRight, boolean rotLeft, boolean rotReset) {
		
		if(rotRight) {
			camera.increaseRotation(0.01d);
		} else if(rotLeft) {
			camera.increaseRotation(-0.01d);
		} else if(rotReset) {
			camera.setTheta(0d);
		}
		
	}
	
	@Override
	public void handleMouseInput() {
		
	}
	
	@Override
	public void handleKeyboardInput() {
		
		handleCameraRotation(KeyboardInput.isKeyDown(KeyEvent.VK_C), KeyboardInput.isKeyDown(KeyEvent.VK_Z), 
				KeyboardInput.isKeyDown(KeyEvent.VK_X));
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_L)) {
			showEntireWorld = !showEntireWorld;
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
			
		}
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		sm.getClient().setController(this);
		
		CustomLoader loader = sm.getGame().getLoader(CustomLoader.class);
		
		TerrainFileParser parser = sm.getGame().getFileParser(TerrainFileParser.class);
		
		world.setTerrain(loader.loadTerrain(parser, "terrain"));
		world.generateCoordinates(player);
		
		viewport.setWorld(world);
		viewport.setCamera(camera);
		
		show("Main Card");
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		inventory.setActive(false);
		pauseScreen.setActive(false);
		
	}
	
}
