package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.renderengine.BackgroundRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.game.World;
import com.rawad.ballsimulator.geometry.Rectangle;
import com.rawad.gamehelpers.client.states.State;
import com.rawad.gamehelpers.client.states.StateChangeRequest;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.jfxengine.gui.GuiRegister;
import com.rawad.jfxengine.gui.Root;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MenuState extends State {
	
	private World world;
	
	private Entity camera;
	
	@FXML private Label lblTitle;
	
	@FXML private Button btnSingleplayer;
	@FXML private Button btnMultiplayer;
	@FXML private Button btnOptions;
	@FXML private Button btnExit;
	
	@Override
	public void init() {
		
		world = new World(gameEngine.getEntities());
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		masterRender.getRenders().put(new BackgroundRender(camera));
		
		Root root = GuiRegister.loadGui(this);
		
		lblTitle.setText(game.getName());
		
		btnSingleplayer.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(GameState.class)));
		btnMultiplayer.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(MultiplayerGameState.class)));
		btnOptions.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(OptionState.class)));
		btnExit.setOnAction(e -> game.requestStop());
		
		Rectangle viewport = camera.getComponent(UserViewComponent.class).getViewport();
		viewport.widthProperty().bind(root.widthProperty());
		viewport.heightProperty().bind(root.heightProperty());
		
	}
	
	@Override
	public void terminate() {}
	
	@Override
	protected void onActivate() {
		world.addEntity(camera);
	}
	
	@Override
	protected void onDeactivate() {}
	
}
