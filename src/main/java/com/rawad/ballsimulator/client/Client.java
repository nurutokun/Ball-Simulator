package com.rawad.ballsimulator.client;

import com.rawad.ballsimulator.client.gamestates.ControlState;
import com.rawad.ballsimulator.client.gamestates.GameState;
import com.rawad.ballsimulator.client.gamestates.LoadingState;
import com.rawad.ballsimulator.client.gamestates.MenuState;
import com.rawad.ballsimulator.client.gamestates.MultiplayerGameState;
import com.rawad.ballsimulator.client.gamestates.OptionState;
import com.rawad.ballsimulator.client.gamestates.WorldEditorState;
import com.rawad.ballsimulator.client.gui.Background;
import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.gui.Root;
import com.rawad.ballsimulator.client.gui.Transitions;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.client.input.InputBindings;
import com.rawad.ballsimulator.client.input.Mouse;
import com.rawad.ballsimulator.fileparser.ControlsFileParser;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.client.gamestates.IStateChangeListener;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateChangeRequest;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.client.renderengine.IRenderable;
import com.rawad.gamehelpers.client.renderengine.RenderingTimer;
import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.log.Logger;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Client extends Proxy implements IStateChangeListener, IRenderable {
	
	// narutoget.io and watchnaruto.tv
	// 472
	
	private static final int TARGET_FPS = 60;
	
	private Stage stage;
	
	private Scene scene;
	
	private StateManager sm;
	
	private InputBindings inputBindings;
	
	private Task<Void> entityBlueprintLoadingTask;
	private Task<Void> loadingTask;
	
	private RenderingTimer renderingTimer;
	
	private SimpleStringProperty gameTitle;
	
	@Override
	public void preInit(Game game) {
		super.preInit(game);
		
		initializeGui();
		
		gameTitle.setValue(game.getName());
		
		Loader loader = new Loader();
		loaders.put(loader);
		
		GameTextures.loadGameIcon(loader);
		
		EntityFileParser entityBlueprintParser = new EntityFileParser();
		
		fileParsers.put(entityBlueprintParser);
		fileParsers.put(new TerrainFileParser());
		fileParsers.put(new SettingsFileParser());
		fileParsers.put(new ControlsFileParser());
		
		entityBlueprintLoadingTask = Loader.getEntityBlueprintLoadingTask(loader, entityBlueprintParser);
		
		renderingTimer = new RenderingTimer(this, TARGET_FPS);
		
		sm = new StateManager(game, this);
		
		loadingTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				String message = "Initializing client resources...";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				try {
					
					initResources();
					
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				
				renderingTimer.start();
				
				message = "Done!";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				return null;
				
			}
		};
		
		loadingTask.setOnSucceeded(e -> {
			sm.requestStateChange(StateChangeRequest.instance(MenuState.class));
		});
		
	}
	
	protected void initializeGui() {
		
		scene = new Scene(new Root(new StackPane(), ""), Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		scene.setFill(Color.BLACK);
		
		gameTitle = new SimpleStringProperty("Game");
		
		stage.titleProperty().bind(gameTitle);
		
		stage.setFullScreenExitHint("");
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		
		stage.setOnCloseRequest(e -> {
			
			game.requestStop();
			
			e.consume();
			
		});
		
		stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			InputAction action = inputBindings.get(keyEvent.getCode());
			
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
		
		Platform.runLater(() -> stage.setScene(scene));
		
	}
	
	@Override
	public void init() {
		
		Loader.addTask(entityBlueprintLoadingTask);
		Loader.addTask(loadingTask);
		
	}
	
	/** Called from the Loading Thread to initialize anything that might take an extended period of time. */
	public void initResources() {
		
		LoadingState loadingState = new LoadingState(loadingTask);
		
		loadingState.init();
		
		sm.setCurrentState(loadingState);
		onStateChange(loadingState, loadingState);
		
		Platform.runLater(() -> {
			
			stage.getIcons().add(GameTextures.findTexture(GameTextures.TEXTURE_GAME_ICON));
			stage.show();
			
			update = true;
			
		});
		
		initializeInputBindings();
		
		GameTextures.loadTextures(loaders.get(Loader.class));
		
		sm.addState(new MenuState());
		sm.addState(new GameState());
		sm.addState(new OptionState());
		sm.addState(new WorldEditorState());
		sm.addState(new MultiplayerGameState());
		sm.addState(new ControlState());
		
	}
	
	private void initializeInputBindings() {
		
		inputBindings = new InputBindings();
		
		inputBindings.put(InputAction.MOVE_UP, KeyCode.UP);
		inputBindings.put(InputAction.MOVE_DOWN, KeyCode.DOWN);
		inputBindings.put(InputAction.MOVE_RIGHT, KeyCode.RIGHT);
		inputBindings.put(InputAction.MOVE_LEFT, KeyCode.LEFT);
		
		inputBindings.put(InputAction.TILT_RIGHT, KeyCode.C);
		inputBindings.put(InputAction.TILT_LEFT, KeyCode.Z);
		inputBindings.put(InputAction.TILT_RESET, KeyCode.X);
		
		inputBindings.put(InputAction.GEN_POS, KeyCode.R);
		inputBindings.put(InputAction.SHOW_WORLD, KeyCode.L);
		
		inputBindings.put(InputAction.CLAMP, KeyCode.V);
		
		inputBindings.put(InputAction.SAVE, KeyCode.F);
		
		inputBindings.put(InputAction.PAUSE, KeyCode.ESCAPE);
		inputBindings.put(InputAction.INVENTORY, KeyCode.E);
		
		inputBindings.put(InputAction.SEND, KeyCode.ENTER);
		inputBindings.put(InputAction.CHAT, KeyCode.T);
		
		inputBindings.put(InputAction.DEBUG, KeyCode.F3);
		inputBindings.put(InputAction.REFRESH, KeyCode.F5);
		inputBindings.put(InputAction.FULLSCREEN, KeyCode.F11);
		
		inputBindings.put(InputAction.PLAYER_LIST, KeyCode.TAB);
		
		inputBindings.put(InputAction.PLACE, MouseButton.PRIMARY);
		inputBindings.put(InputAction.REMOVE, MouseButton.SECONDARY);
		inputBindings.put(InputAction.EXTRACT, MouseButton.MIDDLE);
		
		loaders.get(Loader.class).loadInputBindings(fileParsers.get(ControlsFileParser.class), inputBindings, "inputs");
		
	}
	
	@Override
	public void tick() {
		
		Background.instance().tick();
		
		Mouse.update(GuiRegister.getRoot(sm.getCurrentState()));
		
		sm.update();
		
	}
	
	@Override
	public void render() {
		Platform.runLater(() -> sm.render());
	}
	
	@Override
	public void terminate() {
		
		update = false;
		
		renderingTimer.stop();
		
		Transitions.parallel(scene.getRoot(), e -> {
			
			sm.terminate();
			
			GameTextures.deleteTextures();
			
			stage.close();
			
		}, Transitions.fade(Transitions.DEFAULT_DURATION, Transitions.SHOWING, Transitions.HIDDEN)).playFromStart();
		
	}
	
	@Override
	public void onStateChange(State oldState, State newState) {
		
		Platform.runLater(() -> {
			
			Root oldRoot = GuiRegister.getRoot(oldState);
			Root newRoot = GuiRegister.getRoot(newState);
			
			StackPane oldGuiContainer = oldRoot.getGuiContainer();
			StackPane newGuiContainer = newRoot.getGuiContainer();
			
			Transitions.parallel(oldGuiContainer, e -> {
				
				scene.setRoot(newRoot);
				scene.getRoot().requestFocus();
				
				oldGuiContainer.setOpacity(Transitions.SHOWING);
				
				Transitions.parallel(newGuiContainer, e2 -> {
					newGuiContainer.setOpacity(Transitions.SHOWING);
					newGuiContainer.setTranslateX(0);
				}, 
						Transitions.fade(Transitions.DEFAULT_DURATION, Transitions.HIDDEN, Transitions.SHOWING), 
						Transitions.slideHorizontally(Transitions.DEFAULT_DURATION, -newGuiContainer.getWidth(), 0))
				.playFromStart();
				
			}, Transitions.fade(Transitions.DEFAULT_DURATION, Transitions.SHOWING, Transitions.HIDDEN)).playFromStart();
		});
		
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public RenderingTimer getRenderingTimer() {
		return renderingTimer;
	}
	
	public InputBindings getInputBindings() {
		return inputBindings;
	}
	
}
