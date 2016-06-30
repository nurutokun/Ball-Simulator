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
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.ballsimulator.networking.server.tcp.SPacket03Message;
import com.rawad.ballsimulator.server.Server;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.GameTextures;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.resources.ResourceManager;

import javafx.application.Platform;
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
		
		loaders.put(new CustomLoader());
		
	}
	
	@Override
	public void init(Game game) {
		super.init(game);
		
		loadingTask.setOnSucceeded(e -> {
			initGameDependantGui();
		});

		
	}
	
	@Override
	public void initResources() {
		
		TexturesRegister.registerTextures(loaders.get(CustomLoader.class));
		
		worldViewState = new WorldViewState();
		
		worldViewState.initGui();// Order doesn't really matter here.
		worldViewState.init(sm);
		
	}
	
	private void initGameDependantGui() {
		
		stage.setOnCloseRequest(e -> {
			
			requestClose();
			
			e.consume();
			
		});
		stage.setTitle(game.getName() + " " + Server.SIMPLE_NAME);
		stage.getIcons().add(ResourceManager.getTexture(GameTextures.findTexture(GameTextures.GAME_ICON)));
		
		debugChanger.selectedProperty().bindBidirectional(game.debugProperty());
		
		game.getWorld().getEntities().addListener((Change<? extends Entity> change) -> {
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
						if(e.getComponent(UserComponent.class) != null) playerList.getItems().remove(e);
					}
					
				}
			}
			
		});
		
		readyToUpdate = true;
		
		worldViewStateRoot = GuiRegister.getRoot(worldViewState);
		
		worldViewStateRoot.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			InputAction action = (InputAction) inputBindings.get(keyEvent.getCode());
			
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
	public void initGui() {
		
		loader = new FXMLLoader(Loader.getFxmlLocation(getClass()));
		loader.setController(this);
		
		try {
			loader.load();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
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
		
		Logger.getPrintStreams().add(consolePrinter);
		
		stage.setTitle(Server.SIMPLE_NAME);
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.setOnCloseRequest(null);
		stage.sizeToScene();
		stage.show();
		
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
		Platform.runLater(() -> worldViewState.getMasterRender().render(worldViewStateRoot.getCanvas()
				.getGraphicsContext2D()));
	}
	
	@Override
	public void stop() {
		
		readyToUpdate = false;
		readyToRender = false;
		
		ResourceManager.releaseResources();
		
		Platform.runLater(() -> stage.close());
		
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
		
		inputBindings.put(KeyCode.C, InputAction.CLAMP);
		inputBindings.put(KeyCode.L, InputAction.SHOW_WORLD);
		
		inputBindings.put(KeyCode.F3, InputAction.DEBUG);
		
	}
	
	@Override
	public void onStateChange() {
		throw new UnsupportedOperationException("Can't change state of StateManager as there should only be one State.");
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
}
