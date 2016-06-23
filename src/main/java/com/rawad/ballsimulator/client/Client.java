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
import com.rawad.ballsimulator.client.gui.Transitions;
import com.rawad.ballsimulator.client.input.InputAction;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Client extends AClient {
	
	// narutoget.io and watchnaruto.tv
	// Episode 434.
	
	private static final String DEFAULT_FONT = "Y2K Neophyte";
	
	private SimpleStringProperty gameTitle;
	
	public Client() {
		
		gameTitle = new SimpleStringProperty();
		
	}
	
	@Override
	public void initGui(Stage stage) {
		super.initGui(stage);
		
		scene.setFill(Color.BLACK);
		
		stage.titleProperty().bind(gameTitle);
		
		stage.setFullScreenExitHint("");
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		
		stage.setOnCloseRequest(e -> {
			
			game.requestStop();
			
			e.consume();
			
		});
		
		stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			InputAction action = (InputAction) getInputBindings().get(keyEvent.getCode());
			
			switch(action) {
			
			case DEBUG:
				
				game.setDebug(!game.isDebug());
				
				break;
			
			case FULLSCREEN:
				
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
			
			keyEvent.consume();
			
		});
		
	}
	
	private void loadFont(GameHelpersLoader loader) {
		
		try {
			
			Font f = Font.createFont(Font.PLAIN, loader.loadFontFile(DEFAULT_FONT)).deriveFont(13f);
			
			LookAndFeel laf = UIManager.getLookAndFeel();
			laf.getDefaults().put("defaultFont", f);
			
		} catch(FontFormatException | IOException ex) {
			ex.printStackTrace();
			
			Logger.log(Logger.DEBUG, "Couln't load font: \"" + DEFAULT_FONT + "\"");
			
		}
		
	}
	
	public void initResources() {
		
		GameTextures.registerTextures(game);
		
		new MenuState(sm);
		new GameState(sm);
		new OptionState(sm);
		new WorldEditorState(sm);
		new MultiplayerGameState(sm);
		
		sm.initGui();
		
		GameHelpersLoader ghLoader = game.getLoaders().get(GameHelpersLoader.class);
		
		loadFont(ghLoader);
		
		ResourceManager.getTextureObject(game.getIconLocation()).setOnloadAction(texture -> {
			Platform.runLater(() -> stage.getIcons().add(texture.getTexture()));
		});
		
	}
	
	@Override
	public void init(Game game) {
		super.init(game);
		
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
				
				sm.requestStateChange(MenuState.class);// Maybe a fade-in, fade-out type thing.
				
				return 0;
				
			}
			
		};
		
		LoadingState loadingState = new LoadingState(sm, task);
		
		game.addTask(task);
		
		Platform.runLater(() -> {
			
			gameTitle.setValue(game.toString());
			
			loadingState.initGui();
			
			sm.setState(LoadingState.class);
			
			stage.show();
			
			readyToUpdate = true;
			
		});
		
	}
	
	@Override
	protected void initInputBindings() {
		
		inputBindings.setDefaultBinding(InputAction.DEFAULT);
		
		inputBindings.put(KeyCode.UP, InputAction.MOVE_UP);
		inputBindings.put(KeyCode.DOWN, InputAction.MOVE_DOWN);
		inputBindings.put(KeyCode.RIGHT, InputAction.MOVE_RIGHT);
		inputBindings.put(KeyCode.LEFT, InputAction.MOVE_LEFT);
		inputBindings.put(KeyCode.W, InputAction.MOVE_UP);
		inputBindings.put(KeyCode.S, InputAction.MOVE_DOWN);
		inputBindings.put(KeyCode.D, InputAction.MOVE_RIGHT);
		inputBindings.put(KeyCode.A, InputAction.MOVE_LEFT);
		
		inputBindings.put(KeyCode.C, InputAction.TILT_RIGHT);
		inputBindings.put(KeyCode.Z, InputAction.TILT_LEFT);
		inputBindings.put(KeyCode.X, InputAction.TILT_RESET);
		
		inputBindings.put(KeyCode.R, InputAction.GEN_POS);
		inputBindings.put(KeyCode.L, InputAction.SHOW_WORLD);
		
		inputBindings.put(KeyCode.V, InputAction.CLAMP);
		
		inputBindings.put(KeyCode.F, InputAction.SAVE);
		
		inputBindings.put(KeyCode.ESCAPE, InputAction.PAUSE);
		inputBindings.put(KeyCode.E, InputAction.INVENTORY);
		
		inputBindings.put(KeyCode.ENTER, InputAction.SEND);
		inputBindings.put(KeyCode.T, InputAction.CHAT);
		
		inputBindings.put(KeyCode.F3, InputAction.DEBUG);
		inputBindings.put(KeyCode.F5, InputAction.REFRESH);
		inputBindings.put(KeyCode.F11, InputAction.FULLSCREEN);
		
		inputBindings.put(KeyCode.TAB, InputAction.PLAYER_LIST);
		
		inputBindings.put(MouseButton.PRIMARY, InputAction.PLACE);
		inputBindings.put(MouseButton.SECONDARY, InputAction.REMOVE);
		inputBindings.put(MouseButton.MIDDLE, InputAction.EXTRACT);
		
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
		
		Transitions.exit(scene.getRoot(), Transitions.DEFAULT_DURATION, e -> {
			
			sm.stop();
			
			ResourceManager.releaseResources();
			
			stage.close();
			
		}).playFromStart();;
		
	}
	
}