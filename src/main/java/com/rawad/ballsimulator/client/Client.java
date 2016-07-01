package com.rawad.ballsimulator.client;

import com.rawad.ballsimulator.client.gamestates.GameState;
import com.rawad.ballsimulator.client.gamestates.LoadingState;
import com.rawad.ballsimulator.client.gamestates.MenuState;
import com.rawad.ballsimulator.client.gamestates.MultiplayerGameState;
import com.rawad.ballsimulator.client.gamestates.OptionState;
import com.rawad.ballsimulator.client.gamestates.WorldEditorState;
import com.rawad.ballsimulator.client.gui.Background;
import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.GameTextures;
import com.rawad.gamehelpers.client.gamestates.StateChangeRequest;
import com.rawad.gamehelpers.client.gui.Transitions;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.resources.ResourceManager;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Client extends AClient {
	
	// narutoget.io and watchnaruto.tv
	// 441
	
	private Stage stage;
	
	private Scene scene;
	
	private SimpleStringProperty gameTitle;
	
	public Client() {
		super();
		
		gameTitle = new SimpleStringProperty();
		
	}
	
	@Override
	public void initGui() {
		
		scene = new Scene(new Pane(), Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		scene.setFill(Color.BLACK);
		
		stage.setScene(scene);
		
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
	
	@Override
	public void preInit(Game game) {
		super.preInit(game);
		
		loaders.put(new Loader());
		
		fileParsers.put(new TerrainFileParser());
		fileParsers.put(new SettingsFileParser());
		
	}
	
	@Override
	public void init(Game game) {
		super.init(game);
		
		loadingTask.setOnSucceeded(e -> {
			sm.requestStateChange(StateChangeRequest.instance(MenuState.class));
		});
		
		
		LoadingState loadingState = new LoadingState(loadingTask);
		
		Platform.runLater(() -> {
			
			gameTitle.setValue(game.getName());
			
			loadingState.initGui();
			
			sm.setCurrentState(loadingState);
			onStateChange();
			
			stage.show();
			
			readyToUpdate = true;
			
		});
		
	}
	
	@Override
	public void initResources() {
		
		TexturesRegister.registerTextures(loaders.get(Loader.class));
		
		sm.addState(new MenuState());
		sm.addState(new GameState());
		sm.addState(new OptionState());
		sm.addState(new WorldEditorState());
		sm.addState(new MultiplayerGameState());
		
		ResourceManager.getTextureObject(GameTextures.findTexture(TexturesRegister.GAME_ICON))
				.setOnloadAction(texture -> {
						Platform.runLater(() -> stage.getIcons().add(texture.getTexture()));
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
		
		Mouse.update(scene.getRoot());
		
		super.tick();
		
	}
	
	@Override
	protected void render() {
		
		Platform.runLater(() -> sm.getCurrentState().getMasterRender().render(GuiRegister.getRoot(sm.getCurrentState())
				.getCanvas().getGraphicsContext2D()));
		
	}
	
	@Override
	public void stop() {
		
		readyToUpdate = false;
		readyToRender = false;
		
		Transitions.parallel(scene.getRoot(), e -> {
			
			sm.stop();
			
			ResourceManager.releaseResources();
			
			stage.close();
			
		}, Transitions.fade(Transitions.DEFAULT_DURATION, Transitions.OPAQUE, Transitions.HIDDEN)).playFromStart();
		
	}
	
	@Override
	public void onStateChange() {
		
		Platform.runLater(() -> {
			scene.setRoot(GuiRegister.getRoot(sm.getCurrentState()));
			scene.getRoot().requestFocus();
		});
		
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
}
