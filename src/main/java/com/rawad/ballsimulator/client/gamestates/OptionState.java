package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.renderengine.BackgroundRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.game.World;
import com.rawad.ballsimulator.geometry.Rectangle;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.client.states.State;
import com.rawad.gamehelpers.client.states.StateChangeRequest;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.jfxengine.gui.GuiRegister;
import com.rawad.jfxengine.gui.Root;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class OptionState extends State {
	
	private World world;
	
	private Entity camera;
	
	@FXML private Button btnMainMenu;
	@FXML private Button btnWorldEditor;
	@FXML private Button btnControls;
	
	@FXML private TextField ipHolder;
	
	private Loader loader;
	
	private SettingsFileParser settings;
	
	@Override
	public void init() {
		
		world = new World(gameEngine.getEntities());
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		Client client = game.getProxies().get(Client.class);
		
		settings = client.getFileParsers().get(SettingsFileParser.class);
		
		loader = client.getLoaders().get(Loader.class);
		
		masterRender.getRenders().put(new BackgroundRender(camera));
		
		Root root = GuiRegister.loadGui(this);
		
		btnMainMenu.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(MenuState.class)));
		btnWorldEditor.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(WorldEditorState.class)));
		btnControls.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(ControlState.class)));
		
		Rectangle viewport = camera.getComponent(UserViewComponent.class).getViewport();
		viewport.widthProperty().bind(root.widthProperty());
		viewport.heightProperty().bind(root.heightProperty());
		
	}
	
	@Override
	public void terminate() {}
	
	@Override
	protected void onActivate() {
		
		world.addEntity(camera);
		
		Loader.addTask(new Task<Void>() {
			protected Void call() throws Exception {
				
				loader.loadSettings(settings, SettingsFileParser.SETTINGS_FILE_NAME);
				ipHolder.setText(settings.getIp());
				
				return null;
				
			}
		});
		
	}
	
	@Override
	protected void onDeactivate() {
		
		Loader.addTask(new Task<Void>() {
			protected Void call() throws Exception {
				
				loader.saveSettings(settings, SettingsFileParser.SETTINGS_FILE_NAME);
				
				return null;
				
			}
		});
		
	}
}
