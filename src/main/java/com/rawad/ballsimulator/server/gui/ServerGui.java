package com.rawad.ballsimulator.server.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.server.tcp.SPacket03Message;
import com.rawad.ballsimulator.server.Server;
import com.rawad.ballsimulator.server.ServerController;
import com.rawad.ballsimulator.server.main.ServerStart;
import com.rawad.ballsimulator.server.world.WorldMP;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.resources.TextureResource;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ServerGui extends Application {
	
	private Server server;
	
	private Stage stage;
	
	private FXMLLoader loader;
	
	@FXML private TabPane tabPane;
	@FXML private StackPane canvasContainer;
	@FXML private Canvas canvas;
	@FXML private TextArea consoleOutput;
	@FXML private TextField consoleInput;
	@FXML private CheckMenuItem debugChanger;
	
	private PrintStream consolePrinter;
	
	private Viewport viewport;
	private Camera camera;
	
	private boolean up;
	private boolean down;
	private boolean right;
	private boolean left;
	
	private boolean freeRoam;
	
	public ServerGui(Server server) {
		this.server = server;
		
		viewport = new Viewport();
		camera = new Camera();
		
		viewport.update(server.<ServerController>getController().getWorld(), camera);
		
		consolePrinter = new PrintStream(new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				appendTextToConsole(String.valueOf((char) b));
			}
			
			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);				
			}
			
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				appendTextToConsole(new String(b, off, len));
			}
			
		}, true);
		
		Logger.getPrintStreams().add(consolePrinter);
		
	}
	
	public ServerGui() {
		this(ServerStart.server);
		
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		this.stage = stage;
		
		loader = new FXMLLoader(Loader.getFxmlLocation(getClass()));
		loader.setController(this);
		
		try {
			loader.load();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		Platform.runLater(() -> stage.getScene().setRoot(loader.getRoot()));
		
		debugChanger.selectedProperty().bindBidirectional(server.getGame().debugProperty());
		
		tabPane.focusedProperty().addListener((e, oldValue, newValue) -> {
			tabPane.getSelectionModel().getSelectedItem().getContent().requestFocus();
		});
		tabPane.getSelectionModel().selectedItemProperty().addListener((e, oldValue, newValue) -> {
			tabPane.getSelectionModel().getSelectedItem().getContent().requestFocus();
		});
		
		camera.getCameraBounds().widthProperty().bind(canvasContainer.widthProperty());
		camera.getCameraBounds().heightProperty().bind(canvasContainer.heightProperty());
		
		canvasContainer.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			switch(keyEvent.getCode()) {
			
			case F3:
				server.getGame().setDebug(!server.getGame().isDebug());
				break;
			
			case L:
				freeRoam = !freeRoam;
				break;
			
			case UP:
			case W:
				up = true;
				break;
				
			case DOWN:
			case S:
				down = true;
				break;
				
			case RIGHT:
			case D:
				right = true;
				break;
				
			case LEFT:
			case A:
				left = true;
				break;
				
			default:
				break;
			
			}
			
		});
		
		canvasContainer.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			
			switch(keyEvent.getCode()) {

			case UP:
			case W:
				up = false;
				break;
				
			case DOWN:
			case S:
				down = false;
				break;
				
			case RIGHT:
			case D:
				right = false;
				break;
				
			case LEFT:
			case A:
				left = false;
				break;
				
			default:
				break;
			}
			
		});
		
		server.addTask(new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				
				Logger.log(Logger.DEBUG, "Registering textures...");
				
				ResourceManager.registerUnkownTexture();
				
				server.getGame().registerTextures();
				
				ResourceManager.getTextureObject(server.getGame().getIconLocation()).setOnloadAction(() -> {
					Platform.runLater(() -> stage.getIcons().add(ResourceManager.getTexture(server.getGame()
							.getIconLocation())));
				});
				
				EntityPlayer.registerTextures(server.getGame().getLoader(CustomLoader.class));
				
				Logger.log(Logger.DEBUG, "Loading textures...");
				
				for(TextureResource texture: ResourceManager.getRegisteredTextures().values()) {
					
					ResourceManager.loadTexture(texture);
					
				}
				
				Thread guiUpdater = new Thread(() -> {
					
					while(server.getGame().isRunning()) {
						
						update();
						
						try {
							Thread.sleep(GameManager.instance().getSleepTime());
						} catch(Exception ex) {
							ex.printStackTrace();
							break;
						}
						
					}
					
				}, "Gui Updater");
				guiUpdater.setDaemon(true);
				guiUpdater.start();
				
				WorldMP world = server.<ServerController>getController().getWorld();
				
				camera.setOuterBounds(new Rectangle(0, 0, world.getWidth(), world.getHeight()));
				
				Logger.log(Logger.DEBUG, "Done!");
				
				return 0;
				
			}
		});
		
		stage.setTitle(server.getGame().toString() + " Server");
		
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		
		stage.setOnCloseRequest(e -> {
			
			requestClose();
			
			e.consume();
			
		});
		
		Scene scene = new Scene(new StackPane(), Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
		
	}
	
	private void update() {
		
		if(freeRoam) {
			camera.setScale(1d/2d, 1d/2d);
		} else {
			camera.setScale(Double.MIN_VALUE, Double.MIN_VALUE);
		}
		
		camera.update(up, down, right, left);
		
		Platform.runLater(() -> viewport.render(canvas));
		
	}
	
	@FXML private void handleConsoleInput() {
		
		String input = consoleInput.getText();
		
		Logger.log(Logger.DEBUG, input);
		
		parseConsoleInput(input);
		
		consoleInput.setText("");
		
	}
	
	private void parseConsoleInput(String input) {
		
		final String command = "/send";
		
		if(input.length() > command.length()) {
			if (input.substring(0, command.length()).equalsIgnoreCase(command)) {
				SPacket03Message packet = new SPacket03Message(Server.SIMPLE_NAME, input.substring(command.length()));
				
				server.getNetworkManager().getConnectionManager().sendPacketToAllClients(null, packet);
				
			}
		}
		
	}
	
	private void appendTextToConsole(String text) {
		consoleOutput.appendText(text);
	}
	
	@FXML private void requestClose() {
		server.getGame().requestStop();
		
		Platform.runLater(() -> stage.close());
		
	}
	
}
