package com.rawad.ballsimulator.server.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import com.rawad.ballsimulator.client.GameTextures;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.client.gui.entity.player.PlayerList;
import com.rawad.ballsimulator.networking.server.tcp.SPacket04Message;
import com.rawad.ballsimulator.server.Server;
import com.rawad.ballsimulator.server.entity.UserComponent;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.resources.TextureResource;

import javafx.application.Platform;
import javafx.collections.ListChangeListener.Change;
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
	
	/**
	 * 
	 * @param server
	 * @param game Used only for registering textures.
	 */
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
	public void initGui(Stage stage) {
		super.initGui(stage);
		
		loader = new FXMLLoader(Loader.getFxmlLocation(getClass()));
		loader.setController(this);
		
		try {
			loader.load();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		Scene scene = new Scene(loader.getRoot(), Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		stage.setScene(scene);
		
		debugChanger.selectedProperty().bindBidirectional(server.getGame().debugProperty());
		
		tabPane.focusedProperty().addListener((e, oldValue, newValue) -> {
			tabPane.getSelectionModel().getSelectedItem().getContent().requestFocus();
		});
		
		tabPane.getSelectionModel().selectedItemProperty().addListener((e, oldValue, newValue) -> {
			tabPane.getSelectionModel().getSelectedItem().getContent().requestFocus();
		});
		
		getGame().getWorld().getObservableEntities().addListener((Change<? extends Entity> change) -> {
			
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
			
		});
		
		console.getInputArea().addEventHandler(ActionEvent.ACTION, e -> {
			
			String input = console.getInputArea().getText();
			
			Logger.log(Logger.DEBUG, input);
			
			parseConsoleInput(input);
			
		});
		console.getInputArea().setOnAction(e -> console.getInputArea().setText(""));
		console.getOutputArea().setWrapText(false);
		console.setShowing(true);
		
		Logger.getPrintStreams().add(consolePrinter);
		
		WorldViewState worldViewState = new WorldViewState(this, server.getGame().getWorld());
		worldViewState.initGui();
		
		sm.setState(worldViewState);
		
		worldViewRoot = worldViewState.getRoot();
		
		worldViewTab.setContent(worldViewRoot);
		
		server.addTask(new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				
				GameTextures.registerTextures(game);
				
				ResourceManager.getTextureObject(server.getGame().getIconLocation()).setOnloadAction((texture) -> {
					Platform.runLater(() -> stage.getIcons().add(texture.getTexture()));
				});
				
				String message = "Loading textures...";
				
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
		
		Platform.runLater(() -> getStage().close());
		
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
