package com.rawad.ballsimulator.client;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import com.rawad.ballsimulator.client.gamestates.GameState;
import com.rawad.ballsimulator.client.gamestates.LoadingState;
import com.rawad.ballsimulator.client.gamestates.MenuState;
import com.rawad.ballsimulator.client.gamestates.MultiplayerGameState;
import com.rawad.ballsimulator.client.gamestates.OptionState;
import com.rawad.ballsimulator.client.gamestates.WorldEditorState;
import com.rawad.ballsimulator.client.gui.Background;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.game.PhysicsSystem;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameEngine;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.resources.TextureResource;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Client extends AClient {
	
	// episode 427
	
	private static final String DEFAULT_FONT = "Y2K Neophyte";
	
	private StateManager sm;
	
	private ClientNetworkManager networkManager;
	
	private SimpleStringProperty gameTitle;
	
	public Client() {
		
		gameTitle = new SimpleStringProperty();
		
	}
	
	public void initGui(Stage stage) {
		
		this.stage = stage;
		
		stage.titleProperty().bind(gameTitle);
		
		stage.setFullScreenExitHint("");
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		
		stage.setOnCloseRequest(e -> {
			
			game.requestStop();
			
			e.consume();
			
		});
		
		stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
		
			switch(e.getCode()) {
			
			case F3:
				
				game.setDebug(!game.isDebug());
				
				break;
			
			case F11:
				
				if(!stage.isFullScreen()) {
					stage.setFullScreen(true);
				} else {
					stage.setFullScreen(false);
					stage.getScene().getRoot().resize(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
					stage.sizeToScene();
				}
				
				break;
				
			default:
				break;
				
			}
			
			e.consume();
			
		});
		
	}
	
	private void loadFont(GameHelpersLoader loader) {
		
		try {
			
			Font f = Font.createFont(Font.PLAIN, loader.loadFontFile(DEFAULT_FONT)).deriveFont(13f);
			
			LookAndFeel laf = UIManager.getLookAndFeel();
			laf.getDefaults().put("defaultFont", f);
			
		} catch (FontFormatException | IOException ex) {
			ex.printStackTrace();
			
			Logger.log(Logger.DEBUG, "Couln't load font: \"" + DEFAULT_FONT + "\"");
			
		}
		
	}
	
	public void initResources() {
		
		sm.addState(new MenuState());
		sm.addState(new GameState());
		sm.addState(new OptionState());
		sm.addState(new WorldEditorState());
		sm.addState(new MultiplayerGameState(networkManager));
		
		sm.initGui();
		
		GameHelpersLoader ghLoader = game.getLoader(GameHelpersLoader.class);
		CustomLoader loader = game.getLoader(CustomLoader.class);
		
		loadFont(ghLoader);
		
		game.registerTextures();
		
		ResourceManager.getTextureObject(game.getIconLocation()).setOnloadAction(new Runnable() {
			
			@Override
			public void run() {
				Platform.runLater(() -> stage.getIcons().add(ResourceManager.getTexture(game.getIconLocation())));
			}
			
		});
		
		Background.registerTextures(loader);
		
		EntityPlayer.registerTextures(loader);
		
		GameEngine gameEngine = game.getGameEngine();
		
		gameEngine.addGameSystem(new PhysicsSystem());
		
	}
	
	@Override
	public void init(Game game) {
		super.init(game);
		
		networkManager = new ClientNetworkManager();
		
		sm = new StateManager(game, this);
		
		Task<Integer> task = new Task<Integer>() {
			
			@Override
			protected Integer call() throws Exception {
				
				String message = "Initializing client resources...";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				initResources();
				
				message = "Registering unknown texture...";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				ResourceManager.registerUnkownTexture();
				
				message = "Loading textures...";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				int progress = 0;
				
				for(TextureResource texture: ResourceManager.getRegisteredTextures().values()) {
					
					message = "Loading \"" + texture.getPath() + "\"...";
					updateMessage(message);
					
					ResourceManager.loadTexture(texture);
					
					updateProgress(progress++, ResourceManager.getRegisteredTextures().size());
					
				}
				
				message = "Done!";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				sm.requestStateChange(MenuState.class);
				
				return 0;
				
			}
			
		};
		
		LoadingState loadingState = new LoadingState(task);
		sm.addState(loadingState);
		
		addTask(task);
		
		Platform.runLater(() -> {
			
			gameTitle.setValue(game.toString());// Might as well put them together...
			
			loadingState.initGui();
			
			stage.setScene(sm.getScene());
			
			sm.requestStateChange(LoadingState.class);
			
			stage.show();
			
		});
		
	}
	
	@Override
	public void tick() {
		super.tick();
		
		Background.instance().tick();
		
		Mouse.update();
		
		sm.update();
		
	}
	
	@Override
	public void stop() {
		super.stop();
		
		sm.stop();
		
		ResourceManager.releaseResources();
		
		Platform.runLater(() -> stage.close());
		
	}
	
	public void connectToServer(String serverAddress) {
		networkManager.init(serverAddress);
	}
	
	public ClientNetworkManager getNetworkManager() {
		return networkManager;
	}
	
}