package com.rawad.ballsimulator.server.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import com.rawad.ballsimulator.client.TexturesRegister;
import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.Root;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerList;
import com.rawad.ballsimulator.client.input.Input;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.ballsimulator.networking.server.tcp.SPacket03Message;
import com.rawad.ballsimulator.server.Server;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.GameTextures;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ServerGui extends AClient {
	
	private Server server;
	
	private Stage stage;
	
	private WorldViewState worldViewState;
	private Root worldViewStateRoot;
	
	private FXMLLoader loader;
	
	@FXML private TabPane tabPane;
	
	@FXML private Tab worldViewTab;
	
	@FXML private Messenger console;
	
	@FXML private CheckMenuItem debugChanger;
	
	@FXML private PlayerList playerList;
	
	private PrintStream consolePrinter;
	
	public ServerGui(Server server) {
		this.server = server;
		
		consolePrinter = new PrintStream(new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				 console.getOutputArea().appendText(String.valueOf((char) b));
			}
			
			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);				
			}
			
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				console.getOutputArea().appendText(new String(b, off, len));
			}
			
		}, true);
		
	}
	
	@Override
	public void preInit(Game game) {
		super.preInit(game);
		
		loaders.put(new Loader());
		
	}
	
	@Override
	public void init(Game game) {
		super.init(game);
		
		Platform.runLater(() -> {
			loadingTask.setOnSucceeded(e -> {
				initGameDependantGui();
			});
		});
		
	}
	
	@Override
	public void initResources() {
		
		initInputBindings();
		
		TexturesRegister.registerTextures(loaders.get(Loader.class));
		
		worldViewState = new WorldViewState();
		
		worldViewState.init(sm);// MUST init(sm) BEFORE initGui().
		worldViewState.initGui();
		
		sm.setCurrentState(worldViewState);
		
	}
	
	private void initGameDependantGui() {
		
		stage.setOnCloseRequest(e -> {
			
			requestClose();
			
			e.consume();
			
		});
		stage.setTitle(game.getName() + " " + Server.SIMPLE_NAME);
		stage.getIcons().add(ResourceManager.getTexture(GameTextures.findTexture(TexturesRegister.GAME_ICON)));
		
		debugChanger.selectedProperty().bindBidirectional(game.debugProperty());
		
		FXCollections.observableArrayList(game.getWorld().getEntities())
				.addListener((Change<? extends Entity> change) -> {
			while(change.next()) {// Consider an "addAll()" call, lots of change "representations".
				if(change.getAddedSize() > 0) {
					
					List<? extends Entity> addedEntities = change.getAddedSubList();
					
					for(Entity e: addedEntities) {
						if(e.getComponent(UserComponent.class) != null) playerList.getItems().add(e);
					}
					
				}
				
				if(change.getRemovedSize() > 0) {
					
					List<? extends Entity> removedEntities = change.getRemoved();
					
					for(Entity e: removedEntities) {
						playerList.getItems().remove(e);
					}
					
				}
			}
			
		});
		
		readyToUpdate = true;
		
		worldViewStateRoot = GuiRegister.getRoot(worldViewState);
		
		worldViewStateRoot.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			InputAction action = (InputAction) inputBindings.get(new Input(keyEvent.getCode()));
			
			switch(action) {
			
			case CLAMP:
				
				if(Mouse.isClamped()) {
					Mouse.unclamp();
				} else {
					Mouse.clamp();
				}
				
				break;
				
			case DEBUG:
				game.setDebug(!game.isDebug());							
				break;
			
			default:
				break;
			
			}
			
		});
		
		worldViewTab.setContent(worldViewStateRoot);
		
		tabPane.focusedProperty().addListener((e, oldValue, newValue) -> {
			tabPane.getSelectionModel().getSelectedItem().getContent().requestFocus();
		});
		
		server.getServerSyncs().add(new GuiSync(playerList));
		
		readyToRender = true;
		
		
	}
	
	@Override
	protected void initGui() {
		
		loader = new FXMLLoader(Loader.getFxmlLocation(getClass()));
		loader.setController(this);
		
		try {
			loader.load();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		Logger.getPrintStreams().add(consolePrinter);
		
		Platform.runLater(() -> {
			
			Scene scene = new Scene(loader.getRoot(), Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
			stage.setScene(scene);
			
			console.getInputArea().addEventHandler(ActionEvent.ACTION, e -> {
				
				String input = console.getInputArea().getText();
				
				Logger.log(Logger.DEBUG, input);
				
				parseConsoleInput(input);
				
			});
			console.getInputArea().setOnAction(e -> console.getInputArea().setText(""));
			console.getOutputArea().setWrapText(false);
			console.show();
			
			stage.setTitle(Server.SIMPLE_NAME);
			stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			stage.setOnCloseRequest(null);
			stage.sizeToScene();
			stage.show();
			
		});
		
	}
	
	private void parseConsoleInput(String input) {
		
		final String command = "/send ";
		
		if(input.length() > command.length()) {
			if (input.substring(0, command.length()).equalsIgnoreCase(command)) {
				SPacket03Message packet = new SPacket03Message(Server.SIMPLE_NAME, input.substring(command.length()));
				
				server.getNetworkManager().getConnectionManager().sendPacketToAllClients(null, packet);
				
			}
		}
		
	}
	
	@FXML private void requestClose() {
		
		server.getGame().requestStop();
		
	}
	
	@Override
	public void tick() {
		
		Mouse.update(worldViewStateRoot);
		
	}
	
	@Override
	protected void render() {
		
		Platform.runLater(() -> {
			synchronized(game.getWorld().getEntities()) {
				worldViewState.getMasterRender().render();
			}
		});
		
	}
	
	@Override
	public void stop() {
		
		readyToUpdate = false;
		readyToRender = false;
		
		ResourceManager.releaseResources();
		
		Platform.runLater(() -> stage.close());
		
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
		
		inputBindings.put(InputAction.CLAMP, new Input(KeyCode.C));
		inputBindings.put(InputAction.SHOW_WORLD, new Input(KeyCode.L));
		
		inputBindings.put(InputAction.DEBUG, new Input(KeyCode.F3));
		
	}
	
	@Override
	public void onStateChange() {
		throw new UnsupportedOperationException("Can't change state of StateManager as there should only be one State.");
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
}
