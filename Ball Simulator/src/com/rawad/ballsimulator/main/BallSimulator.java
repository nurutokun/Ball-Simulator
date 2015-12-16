package com.rawad.ballsimulator.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gamestates.EState;
import com.rawad.ballsimulator.client.gamestates.GameState;
import com.rawad.ballsimulator.client.gamestates.MenuState;
import com.rawad.ballsimulator.client.gamestates.MultiplayerGameState;
import com.rawad.ballsimulator.client.gamestates.OptionState;
import com.rawad.ballsimulator.client.gamestates.WorldEditorState;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.world.WorldRender;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.gui.Background;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.renderengine.gui.BackgroundRender;
import com.rawad.gamehelpers.renderengine.gui.GuiRender;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.strings.FontData;

public class BallSimulator extends Game {
	
	/**
	 * Temporary
	 */
	public static final String NAME = "Ball Simulator";
	
	private static int ICON;
	
	private Client client;
	
	private Background background;
	private BackgroundRender bcRender;
	
	private WorldRender worldRender;
	
	private GuiRender guiRender;
	
	private DebugRender debugRender;
	
	private FontData fontData;// here...?
	
	private boolean showSquares;
	 
	public BallSimulator() {
		super();
		
	}
	
	static {
		
		ICON = ResourceManager.UNKNOWN;
		
	}
	
	@Override
	public void clientInit() {
		super.clientInit();
		
		CustomLoader loader = new CustomLoader();
		
		loaders.put(NAME, loader);
		
		if(ICON == ResourceManager.UNKNOWN) {
			ICON = loader.loadTexture("", "game_icon");
		}
		
		SettingsFileParser settings = new SettingsFileParser();
		
		fileParsers.put(SettingsFileParser.class, settings);
		fileParsers.put(TerrainFileParser.class, new TerrainFileParser());
		
		loader.loadSettings(settings, getSettingsFileName());
		
		DisplayManager.changeFullScreenResolution(settings.getFullScreenResolution());
		
		fontData = new FontData();
//		fontData.readFile("arial(SD)");
//		fontData.setTextureId(Loader.loadTexture("", ""));
		
		bcRender = new BackgroundRender();
		worldRender = new WorldRender();
		guiRender = new GuiRender();
		debugRender = new DebugRender();
		
		bcRender.setBackground(background);
		
		masterRender.registerRender(bcRender);
		masterRender.registerRender(worldRender);
		masterRender.registerRender(guiRender);
		masterRender.registerRender(debugRender);
		
		client = new Client(masterRender);
		
		background = new Background();
		
		sm.init();
		
		sm.addState(new MenuState());
		sm.addState(new GameState(client));
		sm.addState(new OptionState());
		sm.addState(new WorldEditorState());
		sm.addState(new MultiplayerGameState(client));
		
		sm.setState(EState.MENU);
		
		showSquares = false;
		
	}
	
	@Override
	public void update(long timePassed) {
		
		background.update(timePassed);
		
		bcRender.setBackground(background);
		
		handleKeyboardInput();
		
		debugRender.setShow(isDebug());
		debugRender.setCamera(client.getViewport().getCamera());
		
		sm.update();
		
	}
	
	private void handleKeyboardInput() {
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_F3, true)) {
			debug = !debug;
			
		} else if(KeyboardInput.isKeyDown(KeyEvent.VK_F4, true)) {
			showSquares = !showSquares;
			
		} else if(KeyboardInput.isKeyDown(KeyEvent.VK_F5)) {
			GameManager.instance().changeUseOldRendering();
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
		background.render(g);
			
		sm.render(g);
		
		if(debug) {
			renderDebugOverlay(g);
		}
		
		if(showSquares) {
			renderSquares(g);
		}
		
	}
	
	private void renderDebugOverlay(Graphics2D g) {
		
		int screenWidth = Game.SCREEN_WIDTH;
		int screenHeight = Game.SCREEN_HEIGHT;
		
		g.setColor(Color.GREEN);
		g.fillOval(screenWidth - 50, screenHeight - 50, 50, 50);
		
		g.setColor(Color.WHITE);
		g.drawString(DisplayManager.getDisplayWidth() + ", " + DisplayManager.getDisplayHeight() + " | " +
			GameManager.instance().getFPS() + " | " + GameManager.instance().getDeltaTime(), 10, 10);
		
		g.drawString(MouseInput.getX() + ", " + MouseInput.getY(), 10, 20);
		
		boolean useOldRendering = GameManager.instance().shouldUseOldRendering();
		
		g.drawString("Rendering: " + (useOldRendering? "Inherited Rendering":"MCV Rendering"), 10, 30);
		
		g.setColor(Color.RED);
		g.fillRect(MouseInput.getX(), MouseInput.getY(), 1, 1);
		
		g.setColor(Color.WHITE);
		g.drawString(Runtime.getRuntime().freeMemory() + "", 10, 40);
		
		// Could get camera here, but that would be tough... 1 point for MCV rendering.
		
	}
	
	private void renderSquares(Graphics2D g) {
		
		g.setColor(Color.WHITE);
		
		for(int i = 0; i < Game.SCREEN_WIDTH; i++) {
			for(int j = 0; j < Game.SCREEN_HEIGHT; j++) {
				
				if(i%2 == 0 && j%2 == 0) {
					g.fillRect(i, j, 1, 1);
				}
				
			}	
		}
		
	}
	
	@Override
	public BufferedImage getIcon() {
		return ResourceManager.getTexture(ICON);
	}
	
	@Override
	public String toString() {
		return NAME;
	}
	
	@Override
	public String getSettingsFileName() {
		return "settings";
	}
	
}
