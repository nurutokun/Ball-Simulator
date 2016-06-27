package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.renderengine.BackgroundRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateChangeRequest;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class OptionState extends State {
	
	private Entity camera;
	
	@FXML private Button btnMainMenu;
	@FXML private Button btnWorldEditor;
	@FXML private Button btnControls;
	
	@FXML private TextField ipHolder;
	
	private CustomLoader loader;
	
	private SettingsFileParser settings;
	
	public OptionState(StateManager sm) {
		super(sm);
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		world.addEntity(camera);
		
		masterRender.getRenders().put(new BackgroundRender(camera));
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		btnMainMenu.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(MenuState.class)));
		btnWorldEditor.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(WorldEditorState.class)));
//		btnControls.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(ControlsState.class)));
		
		Rectangle viewport = camera.getComponent(UserViewComponent.class).getViewport();
		viewport.widthProperty().bind(root.widthProperty());
		viewport.heightProperty().bind(root.heightProperty());
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		settings = game.getFileParsers().get(SettingsFileParser.class);
		
		loader = game.getLoaders().get(CustomLoader.class);
		
		game.addTask(new Task<Void>() {
			protected Void call() throws Exception {
				
				loader.loadSettings(settings, game.getProxies().get(Client.class).getSettingsFileName());
				ipHolder.setText(settings.getIp());
				
				return null;
				
			}
		});
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		game.addTask(new Task<Void>() {
			protected Void call() throws Exception {
				
				loader.saveSettings(settings, game.getProxies().get(Client.class).getSettingsFileName());
				
				return null;
				
			}
		});
		
	}
	
	public static final int getColumnIndex() {
		return 1;
	}
	
	public static final int getRowIndex() {
		return 2;
	}
	
}
