package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.gui.Root;
import com.rawad.ballsimulator.client.renderengine.BackgroundRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.geometry.Rectangle;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateChangeRequest;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MenuState extends State {
	
	private Entity camera;
	
	@FXML private Label lblTitle;
	
	@FXML private Button btnSingleplayer;
	@FXML private Button btnMultiplayer;
	@FXML private Button btnOptions;
	@FXML private Button btnExit;
	
	@Override
	public void init(StateManager sm, Game game) {
		super.init(sm, game);
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		masterRender.getRenders().put(new BackgroundRender(camera));
				
	}
	
	@Override
	public void initGui() {
		
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
	protected void onActivate() {
		super.onActivate();
		
		world.addEntity(camera);
		
	}
	
}
