package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.renderengine.BackgroundRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
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
import javafx.scene.control.Tooltip;

public class OptionState extends State {
	
	private Entity camera;
	
	@FXML private ComboBox<String> resolutions;
	
	@FXML private Button btnMainMenu;
	@FXML private Button btnWorldEditor;
	
	@FXML private TextField ipHolder;
	
	private CustomLoader loader;
	
	private SettingsFileParser settings;
	
	public OptionState() {
		super();
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		world.addEntity(camera);
		
		masterRender.registerRender(new BackgroundRender(camera));
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		// Traversal keys
		
		// TextLabel on text change -> save ip
		
		btnMainMenu.setOnAction(e -> sm.requestStateChange(MenuState.class));
		btnWorldEditor.setOnAction(e -> sm.requestStateChange(WorldEditorState.class));
		
		resolutions.setOnAction(e -> Logger.log(Logger.DEBUG, "Changed resolution to: " + resolutions.getValue()));
		
		Rectangle viewport = camera.getComponent(UserViewComponent.class).getViewport();
		viewport.widthProperty().bind(root.widthProperty());
		viewport.heightProperty().bind(root.heightProperty());
		
		resolutions.setDisable(true);
		resolutions.setTooltip(new Tooltip("Coming Soon!"));
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		Game game = sm.getGame();
		
		settings = game.getFileParser(SettingsFileParser.class);
		
		loader = game.getLoader(CustomLoader.class);
		
		sm.getClient().addTask(new Task<Integer>() {
			protected Integer call() throws Exception {
				
				loader.loadSettings(settings, sm.getClient().getSettingsFileName());
				ipHolder.setText(settings.getIp());
				
				return 0;
				
			}
		});
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		sm.getClient().addTask(new Task<Integer>() {
			protected Integer call() throws Exception {
				
				loader.saveSettings(settings, sm.getClient().getSettingsFileName());
				
				return 0;
				
			}
		});
		
	}
	
}
