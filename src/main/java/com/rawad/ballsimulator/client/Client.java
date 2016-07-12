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
import com.rawad.ballsimulator.client.input.Input;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.fileparser.ControlsFileParser;
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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Client extends AClient {
	
	// narutoget.io and watchnaruto.tv
	// 451
	
	private Stage stage;
	
	private Scene scene;
	
	private SimpleStringProperty gameTitle;
	
	@Override
	protected void initGui() {
		
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
			
			InputAction action = (InputAction) getInputBindings().get(new Input(keyEvent.getCode()));
			
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
	public void preInit(Game game) {
		super.preInit(game);
		
		gameTitle.setValue(game.getName());
		
		loaders.put(new Loader());
		
		fileParsers.put(new TerrainFileParser());
		fileParsers.put(new SettingsFileParser());
		fileParsers.put(new ControlsFileParser(inputBindings));
		
		loadingTask.setOnSucceeded(e -> {
			sm.requestStateChange(StateChangeRequest.instance(MenuState.class));
		});
		
	}
	
	@Override
	public void initResources() {
		
		initInputBindings();
		
		TexturesRegister.registerTextures(loaders.get(Loader.class));
		
		sm.addState(new MenuState());
		sm.addState(new GameState());
		sm.addState(new OptionState());
		sm.addState(new WorldEditorState());
		sm.addState(new MultiplayerGameState());
		sm.addState(new ControlState());
		
		ResourceManager.getTextureObject(GameTextures.findTexture(TexturesRegister.GAME_ICON))
				.setOnloadAction(texture -> {
						Platform.runLater(() -> stage.getIcons().add(texture.getTexture()));
				});
		
		LoadingState loadingState = new LoadingState(loadingTask);
		
		loadingState.initGui();
		
		sm.setCurrentState(loadingState);
		onStateChange();
		
		Platform.runLater(() -> {
			stage.show();
			readyToUpdate = true;			
		});
		
	}
	
	private void initInputBindings() {
		
		inputBindings.setDefaultAction(InputAction.DEFAULT);
		
		inputBindings.put(InputAction.MOVE_UP, new Input(KeyCode.UP));
		inputBindings.put(InputAction.MOVE_UP, new Input(KeyCode.W));
		inputBindings.put(InputAction.MOVE_DOWN, new Input(KeyCode.DOWN));
		inputBindings.put(InputAction.MOVE_DOWN, new Input(KeyCode.S));
		inputBindings.put(InputAction.MOVE_RIGHT, new Input(KeyCode.RIGHT));
		inputBindings.put(InputAction.MOVE_RIGHT, new Input(KeyCode.D));
		inputBindings.put(InputAction.MOVE_LEFT, new Input(KeyCode.LEFT));
		inputBindings.put(InputAction.MOVE_LEFT, new Input(KeyCode.A));
		
		inputBindings.put(InputAction.TILT_RIGHT, new Input(KeyCode.C));
		inputBindings.put(InputAction.TILT_LEFT, new Input(KeyCode.Z));
		inputBindings.put(InputAction.TILT_RESET, new Input(KeyCode.X));
		
		inputBindings.put(InputAction.GEN_POS, new Input(KeyCode.R));
		inputBindings.put(InputAction.SHOW_WORLD, new Input(KeyCode.L));
		
		inputBindings.put(InputAction.CLAMP, new Input(KeyCode.V));
		
		inputBindings.put(InputAction.SAVE, new Input(KeyCode.F));
		
		inputBindings.put(InputAction.PAUSE, new Input(KeyCode.ESCAPE));
		inputBindings.put(InputAction.INVENTORY, new Input(KeyCode.E));
		
		inputBindings.put(InputAction.SEND, new Input(KeyCode.ENTER));
		inputBindings.put(InputAction.CHAT, new Input(KeyCode.T));
		
		inputBindings.put(InputAction.DEBUG, new Input(KeyCode.F3));
		inputBindings.put(InputAction.REFRESH, new Input(KeyCode.F5));
		inputBindings.put(InputAction.FULLSCREEN, new Input(KeyCode.F11));
		
		inputBindings.put(InputAction.PLAYER_LIST, new Input(KeyCode.TAB));
		
		inputBindings.put(InputAction.PLACE, new Input(MouseButton.PRIMARY));
		inputBindings.put(InputAction.REMOVE, new Input(MouseButton.SECONDARY));
		inputBindings.put(InputAction.EXTRACT, new Input(MouseButton.MIDDLE));
		
	}
	
	@Override
	public void tick() {
		
		Background.instance().tick();
		
		Mouse.update(scene.getRoot());
		
		super.tick();
		
	}
	
	@Override
	protected void render() {
		
		Platform.runLater(() -> {
			synchronized(game.getWorld().getEntities()) {
				sm.getCurrentState().getMasterRender().render();
			}
		});
		
	}
	
	@Override
	public void stop() {
		
		readyToUpdate = false;
		readyToRender = false;
		
		Transitions.parallel(scene.getRoot(), e -> {
			
			sm.stop();
			
			ResourceManager.releaseResources();
			
			stage.close();
			
		}, Transitions.fade(Transitions.DEFAULT_DURATION, Transitions.SHOWING, Transitions.HIDDEN)).playFromStart();
		
	}
	
	@Override
	public void onStateChange() {
		
		Platform.runLater(() -> {
			
			Root oldRoot = (Root) scene.getRoot();
			Root newRoot = GuiRegister.getRoot(sm.getCurrentState());
			
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
	
}
