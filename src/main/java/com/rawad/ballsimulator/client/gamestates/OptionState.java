package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.renderengine.BackgroundRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;
import com.rawad.gamehelpers.log.Logger;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class OptionState extends State {
	
	private Entity camera;
	
	@FXML private ComboBox<String> resolutions;
	
	@FXML private Button btnMainMenu;
	@FXML private Button btnWorldEditor;
	
	@FXML private TextField ipHolder;
	
	private CustomLoader loader;
	
	private SettingsFileParser settings;
	
	public OptionState(AClient client) {
		super(client);
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		world.addEntity(camera);
		
		masterRender.registerRender(new BackgroundRender(camera));
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		btnMainMenu.setOnAction(e -> sm.requestStateChange(MenuState.class));
		btnWorldEditor.setOnAction(e -> sm.requestStateChange(WorldEditorState.class));
		
		resolutions.setOnAction(e -> Logger.log(Logger.DEBUG, "Changed resolution to: " + resolutions.getValue()));
		
		Rectangle viewport = camera.getComponent(UserViewComponent.class).getViewport();
		viewport.widthProperty().bind(root.widthProperty());
		viewport.heightProperty().bind(root.heightProperty());
		
		resolutions.setDisable(true);
		resolutions.setPromptText("Coming Soon!");
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		Game game = sm.getGame();
		
		settings = game.getFileParser(SettingsFileParser.class);
		
		loader = game.getLoader(CustomLoader.class);
		
		client.addTask(new Task<Integer>() {
			protected Integer call() throws Exception {
				
				loader.loadSettings(settings, client.getSettingsFileName());
				ipHolder.setText(settings.getIp());
				
				return 0;
				
			}
		});
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		client.addTask(new Task<Integer>() {
			protected Integer call() throws Exception {
				
				loader.saveSettings(settings, client.getSettingsFileName());
				
				return 0;
				
			}
		});
		
	}
	
}
