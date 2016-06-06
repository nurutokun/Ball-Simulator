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
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.Game;
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
	
	private ClientNetworkManager networkManager;
	
	private SimpleStringProperty gameTitle;
	
	public Client() {
		
		gameTitle = new SimpleStringProperty();
		
	}
	
	@Override
	public void initGui(Stage stage) {
		super.initGui(stage);
		
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
		
		GameTextures.registerTextures(game);
		
		sm.addState(new MenuState(this));
		sm.addState(new GameState(this));
		sm.addState(new OptionState(this));
		sm.addState(new WorldEditorState(this));
		sm.addState(new MultiplayerGameState(this, networkManager));
		
		sm.initGui();
		
		GameHelpersLoader ghLoader = game.getLoader(GameHelpersLoader.class);
		
		loadFont(ghLoader);
		
		ResourceManager.getTextureObject(game.getIconLocation()).setOnloadAction(texture -> {
			Platform.runLater(() -> stage.getIcons().add(texture.getTexture()));
		});
		
	}
	
	@Override
	public void init(Game game) {
		super.init(game);
		
//		networkManager = new ClientNetworkManager();
		
		Task<Integer> task = new Task<Integer>() {
			
			@Override
			protected Integer call() throws Exception {
				
				String message = "Initializing client resources...";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				initResources();
				
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
				
				readyToRender = true;
				
				message = "Done!";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				sm.requestStateChange(MenuState.class);
				
				return 0;
				
			}
			
		};
		
		LoadingState loadingState = new LoadingState(this, task);
		sm.addState(loadingState);
		
		addTask(task);
		
		Platform.runLater(() -> {
			
			gameTitle.setValue(game.toString());// Might as well put them together...
			
			loadingState.initGui();
			
			stage.setScene(sm.getScene());
			
			sm.setState(LoadingState.class);
			
			stage.show();
			
			readyToUpdate = true;
			
		});
		
	}
	
	@Override
	public void tick() {
		
		Background.instance().tick();
		
		Mouse.update(sm.getCurrentState().getRoot());
		
		sm.update();
		
	}
	
	@Override
	public void stop() {
		
		readyToUpdate = false;
		readyToRender = false;
		
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