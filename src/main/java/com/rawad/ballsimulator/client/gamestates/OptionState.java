package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.renderengine.BackgroundRender;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class OptionState extends State {
	
	@FXML private ComboBox<String> resolutions;
	
	@FXML private Button btnMainMenu;
	@FXML private Button btnWorldEditor;
	
	@FXML private TextField ipHolder;
	
	private CustomLoader loader;
	
	private SettingsFileParser settings;
	
	@Override
	public void initGui() {
		super.initGui();
		
		// Traversal keys
		
		// TextLabel on text change -> save ip
		
		btnMainMenu.setOnAction(e -> sm.requestStateChange(MenuState.class));
		btnWorldEditor.setOnAction(e -> sm.requestStateChange(WorldEditorState.class));
		
		resolutions.setOnAction(e -> Logger.log(Logger.DEBUG, "Changed resolution to: " + resolutions.getValue()));
		
	}
	
	@Override
	public void render() {
		super.render();
		
		BackgroundRender.instance().render(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight());
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		Game game = sm.getGame();
		
		settings = game.getFileParser(SettingsFileParser.class);
		
		loader = game.getLoader(CustomLoader.class);
		
		sm.getClient().addTask(new Task<Integer>() {
			protected Integer call() throws Exception {
				
				loader.loadSettings(settings, game.getSettingsFileName());
				ipHolder.setText(settings.getIp());
				
				return 0;
				
			}
		});
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		loader.saveSettings(settings, sm.getGame().getSettingsFileName());
		
	}
	
}
