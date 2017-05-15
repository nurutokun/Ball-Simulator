package com.rawad.ballsimulator.server.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import com.rawad.ballsimulator.client.GameTextures;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerList;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.ballsimulator.networking.server.tcp.SPacket03Message;
import com.rawad.ballsimulator.server.Server;
import com.rawad.ballsimulator.server.WorldMP;
import com.rawad.gamehelpers.client.renderengine.Renderable;
import com.rawad.gamehelpers.client.renderengine.RenderingTimer;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.jfxengine.client.input.InputBindings;
import com.rawad.jfxengine.client.input.Mouse;
import com.rawad.jfxengine.gui.GuiRegister;
import com.rawad.jfxengine.gui.Root;
import com.rawad.jfxengine.loader.GuiLoader;

import javafx.application.Platform;
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
	
	private static final int SCREEN_WIDTH = 640;
	private static final int SCREEN_HEIGHT = 480;
	
	private static final int TARGET_FPS = 60;
	
	private Server server;
	
	private WorldMP world;
	
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
		super();
		
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
		
		loader = new FXMLLoader();
		loader.setController(this);
		
		try {
			
			loader.load(GuiLoader.streamLayoutFile(this.getClass()));
			
			Logger.getPrintStreams().add(consolePrinter);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Override
	public void preInit(Game game) {
		super.preInit(game);
		
		world = server.getWorld();
		
		renderingTimer = new RenderingTimer(this, TARGET_FPS);
		
		loadingTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				initResources();
				
				renderingTimer.start();
				
				Platform.runLater(() -> initGameDependantGui());
				
				return null;
			}
		};
		
		Loader.addTask(loadingTask);
		
	}
	
	@Override
	public void init() {
		
	}
	
	public void initResources() {
		
		initInputBindings();
		
		Loader loader = server.getLoaders().get(Loader.class);
		
		GameTextures.loadTextures(loader);
		
		GameTextures.loadGameIcon(loader);
		
		worldViewState = new WorldViewState();
		worldViewState.setGame(game);
		worldViewState.init();
		
	}
	
	/**
	 * Initializes GUI related things that require some key elements of the game to be loaded already (e.g. textures).
	 * This does modify {@code Nodes} that are already part of the {@link stage} so it expects to be run on the
	 * JavaFX Application Thread.
	 */
	private void initGameDependantGui() {
		
		stage.setOnCloseRequest(e -> {
			
			requestClose();
			
			e.consume();
			
		});
		stage.setTitle(game.getName() + " " + Server.SIMPLE_NAME);
		stage.getIcons().add(GameTextures.findTexture(GameTextures.TEXTURE_GAME_ICON));
		
		debugChanger.setOnAction(e -> game.setDebug(debugChanger.isSelected()));
		
		// TODO: Only make entities with UserComponents be transferred.
		playerList.itemsProperty().bind(world.playersProperty());
		
		/* Back when World was in Game Helpers...
		FXCollections.observableArrayList(world.getEntities())
				.addListener((Change<? extends Entity> change) -> {
			while(change.next()) {// Consider an "addAll()" call, lots of change "representations".
				// Items added.
				if(change.getAddedSize() > 0) {
					
					List<? extends Entity> addedEntities = change.getAddedSubList();
					
					for(Entity e: addedEntities) {
						if(e.getComponent(UserComponent.class) != null) playerList.getItems().add(e);
					}
					
				}
				
				// Items removed.
				if(change.getRemovedSize() > 0) {
					
					List<? extends Entity> removedEntities = change.getRemoved();
					layerList.getItems().removeAll(removedEntities);
					
//					for(Entity e: removedEntities) {
//						playerList.getItems().remove(e);
//					}
					
				}
			}
			
		});*/
		
		update = true;
		
		worldViewStateRoot = GuiRegister.getRoot(worldViewState);
		
		worldViewStateRoot.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			Object action = inputBindings.get(keyEvent.getCode());
			
			switch((InputAction) action) {
			
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
	
	/**
	 * Should be called from the JavaFX {@link Application.start(Stage)}. This method does expect to be run on the JavaFX
	 * Application Thread.
	 * 
	 * @param stage
	 */
	public void initGui(Stage stage) {
		
		this.stage = stage;
		
		Scene scene = new Scene(loader.getRoot(), SCREEN_WIDTH, SCREEN_HEIGHT);
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
		
		/* Used t be server.getGame(); game could be null if requestClose() is called after a server instance is made but
		 * before the game is launched (i.e. preInit(Game) is called). However, game would be null regardless until
		 * preInit(Game) is called. Just don't try to close the game when it hasn't even started running yet... */
		game.requestStop();
		
	}
	
	@Override
	public void tick() {
		
		Mouse.update(worldViewStateRoot);
		
		// Removes the need for the game's 'debug' mode to be a JavaFX property.
		if(debugChanger.isSelected() != game.isDebug()) {
			Platform.runLater(() -> debugChanger.setSelected(game.isDebug()));
		}
		
	}
	
	@Override
	public void render() {
		
		Platform.runLater(() -> {
			synchronized(server.getWorld().getEntities()) {
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
		
		inputBindings.setDefaultAction(InputAction.DEFAULT);
		
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
