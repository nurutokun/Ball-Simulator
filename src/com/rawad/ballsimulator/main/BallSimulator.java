package com.rawad.ballsimulator.main;

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
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.gui.Background;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.resources.ResourceManager;

public class BallSimulator extends Game {
	
	/**
	 * @Temporary
	 */
	public static final String NAME = "Ball Simulator";
	
	private static int ICON;
	
	private Client client;
	
	private Background background;
	
	private WorldRender worldRender;
	
	private DebugRender debugRender;
	
	private CustomLoader loader;
	
	private boolean showSquares;
	
	public BallSimulator() {
		super();
		
	}
	
	@Override
	public void clientInit() {
		super.clientInit();
		
		SettingsFileParser settings = new SettingsFileParser();
		fileParsers.put(SettingsFileParser.class, settings);
		loader.loadSettings(settings, getSettingsFileName());
		
		DisplayManager.changeFullScreenResolution(settings.getFullScreenResolution());
		
		worldRender = new WorldRender();
		debugRender = new DebugRender();
		
		masterRender.registerRender(worldRender);
		masterRender.registerRender(debugRender);
		
		client = new Client(masterRender);

		sm.addState(new MenuState());
		sm.addState(new GameState(client));
		sm.addState(new OptionState());
		sm.addState(new WorldEditorState());
		sm.addState(new MultiplayerGameState(client));
		
		background = Background.instance();
		
		showSquares = false;
		
	}
	
	@Override
	public void initGUI() {
		super.initGUI();
		
		debugRender.initGUI();
		
		client.initGUI();
		
		sm.initialize();
		
		sm.requestStateChange(EState.MENU);
		
	}
	
	@Override
	protected void init() {
		super.init();
		
		loader = new CustomLoader();
		
		loaders.put(NAME, loader);
		
		ICON = loader.loadTexture("", "game_icon");
		
		fileParsers.put(TerrainFileParser.class, new TerrainFileParser());
		
	}
	
	@Override
	public void update(long timePassed) {
		
		handleKeyboardInput();
		
		background.update(timePassed);
		
		sm.update();
		
		debugRender.setShow(isDebug());
		
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
