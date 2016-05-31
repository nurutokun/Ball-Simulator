package com.rawad.ballsimulator.server.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import com.rawad.ballsimulator.client.GameTextures;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerList;
import com.rawad.ballsimulator.networking.server.tcp.SPacket04Message;
import com.rawad.ballsimulator.server.Server;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.resources.TextureResource;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ServerGui extends AClient {
	
	private Server server;
	
	private Stage stage;
	
	private StackPane worldViewRoot;
	
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
		
		Logger.getPrintStreams().add(consolePrinter);
		
	}
	
	@Override
	public void initGui(Stage stage) {
		
		loader = new FXMLLoader(Loader.getFxmlLocation(getClass()));
		loader.setController(this);
		
		try {
			loader.load();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		stage.getScene().setRoot(loader.getRoot());
		
		debugChanger.selectedProperty().bindBidirectional(server.getGame().debugProperty());
		
		tabPane.focusedProperty().addListener((e, oldValue, newValue) -> {
			tabPane.getSelectionModel().getSelectedItem().getContent().requestFocus();
		});
		
		tabPane.getSelectionModel().selectedItemProperty().addListener((e, oldValue, newValue) -> {
			tabPane.getSelectionModel().getSelectedItem().getContent().requestFocus();
		});
		
		playerList.setItems(server.getGame().getWorld().getObservableEntities());
		
		console.getInputArea().addEventHandler(ActionEvent.ACTION, e -> {
			
			String input = console.getInputArea().getText();
			
			Logger.log(Logger.DEBUG, input);
			
			parseConsoleInput(input);
			
		});
		console.getInputArea().setOnAction(e -> console.getInputArea().setText(""));
		console.getOutputArea().setWrapText(false);
		console.setShowing(true);
		
		WorldViewState worldViewState = new WorldViewState(server.getGame().getWorld());
		worldViewState.initGui();
		
		sm.setState(worldViewState);
		
		worldViewRoot = worldViewState.getRoot();
		
		worldViewTab.setContent(worldViewRoot);
		
		server.addTask(new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				
				String message = "Registering textures...";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				GameTextures.registerTextures(server.getGame());
				
				ResourceManager.getTextureObject(server.getGame().getIconLocation()).setOnloadAction((texture) -> {
					Platform.runLater(() -> stage.getIcons().add(ResourceManager.getTexture(server.getGame()
							.getIconLocation())));
				});
				
				message = "Loading textures...";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				for(TextureResource texture: ResourceManager.getRegisteredTextures().values()) {
					
					ResourceManager.loadTexture(texture);
					
				}
				
				message = "Done!";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				return 0;
				
			}
		});
		
		stage.setTitle(server.getGame().toString() + " Server");
		
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		
		stage.setOnCloseRequest(e -> {
			
			requestClose();
			
			e.consume();
			
		});
		
		Scene emptyScene = new Scene(new StackPane(), Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		
		stage.setScene(emptyScene);
		stage.sizeToScene();
		stage.show();
		
		readyToUpdate = true;
		readyToRender = true;
		
	}
	
	private void parseConsoleInput(String input) {
		
		final String command = "/send ";
		
		if(input.length() > command.length()) {
			if (input.substring(0, command.length()).equalsIgnoreCase(command)) {
				SPacket04Message packet = new SPacket04Message(Server.SIMPLE_NAME, input.substring(command.length()));
				
				server.getNetworkManager().getConnectionManager().sendPacketToAllClients(null, packet);
				
			}
		}
		
	}
	
	@FXML private void requestClose() {
		
		server.getGame().requestStop();
		
		Platform.runLater(() -> stage.close());
		
	}

	@Override
	public void tick() {
		
		Mouse.update(worldViewRoot);
		
	}
	
	@Override
	public void stop() {
		
		readyToUpdate = false;
		readyToRender = false;
		
		ResourceManager.releaseResources();
		
		Platform.runLater(() -> stage.close());
		
	}
	
}
