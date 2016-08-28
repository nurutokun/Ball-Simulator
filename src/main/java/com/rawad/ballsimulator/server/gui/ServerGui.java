package com.rawad.ballsimulator.server.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import com.rawad.ballsimulator.client.GameTextures;
import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.Root;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerList;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.client.input.InputBindings;
import com.rawad.ballsimulator.client.input.Mouse;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.ballsimulator.networking.server.tcp.SPacket03Message;
import com.rawad.ballsimulator.server.Server;
import com.rawad.gamehelpers.client.renderengine.Renderable;
import com.rawad.gamehelpers.client.renderengine.RenderingTimer;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.concurrent.Task;
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

public class ServerGui extends Proxy implements Renderable {
	
	private static final int TARGET_FPS = 60;
	
	private Server server;
	
	private Stage stage;
	
	private InputBindings inputBindings;
	
	private RenderingTimer renderingTimer;
	
	private Task<Void> loadingTask;
	
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
		
		renderingTimer = new RenderingTimer(this, TARGET_FPS);
		
		loadingTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				initResources();
				
				initGameDependantGui();
				
				renderingTimer.start();
				
				return null;
			}
		};
		
	}
	
	@Override
	public void init() {
		
		Loader.addTask(loadingTask);
		
	}
	
	public void initResources() {
		
		initInputBindings();
		
		GameTextures.loadTextures(game.getProxies().get(Server.class).getLoaders().get(Loader.class));
		
		worldViewState = new WorldViewState();
		
		worldViewState.setGame(game);
		worldViewState.init();
		
	}
	
	private void initGameDependantGui() {
		
		stage.setOnCloseRequest(e -> {
			
			requestClose();
			
			e.consume();
			
		});
		stage.setTitle(game.getName() + " " + Server.SIMPLE_NAME);
		stage.getIcons().add(GameTextures.findTexture(GameTextures.TEXTURE_GAME_ICON));
		
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
		
		update = true;
		
		worldViewStateRoot = GuiRegister.getRoot(worldViewState);
		
		worldViewStateRoot.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			InputAction action = inputBindings.get(keyEvent.getCode());
			
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
		
		
	}
	
	protected void initGui() {
		
		loader = new FXMLLoader();
		loader.setController(this);
		
		try {
			loader.load(Loader.streamLayoutFile(getClass()));
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
	public void render() {
		
		Platform.runLater(() -> {
			synchronized(game.getWorld().getEntities()) {
				worldViewState.getMasterRender().render();
			}
		});
		
	}
	
	@Override
	public void terminate() {
		
		update = false;
		
		Platform.runLater(() -> stage.close());
		
	}
	
	private void initInputBindings() {
		
		inputBindings = new InputBindings();
		
		inputBindings.put(InputAction.MOVE_UP, KeyCode.UP);
		inputBindings.put(InputAction.MOVE_UP, KeyCode.W);
		inputBindings.put(InputAction.MOVE_DOWN, KeyCode.DOWN);
		inputBindings.put(InputAction.MOVE_DOWN, KeyCode.S);
		inputBindings.put(InputAction.MOVE_RIGHT, KeyCode.RIGHT);
		inputBindings.put(InputAction.MOVE_RIGHT, KeyCode.D);
		inputBindings.put(InputAction.MOVE_LEFT, KeyCode.LEFT);
		inputBindings.put(InputAction.MOVE_LEFT, KeyCode.A);
		
		inputBindings.put(InputAction.CLAMP, KeyCode.C);
		inputBindings.put(InputAction.SHOW_WORLD, KeyCode.L);
		
		inputBindings.put(InputAction.DEBUG, KeyCode.F3);
		
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
