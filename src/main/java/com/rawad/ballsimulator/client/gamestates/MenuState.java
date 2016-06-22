package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.renderengine.BackgroundRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.animation.TranslateTransition;
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
	
	public MenuState(StateManager sm) {
		super(sm);
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		world.addEntity(camera);
		
		masterRender.getRenders().put(new BackgroundRender(camera));
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		lblTitle.setText(sm.getGame().toString());
		
		btnSingleplayer.setOnAction(e -> sm.requestStateChange(GameState.class));
		btnMultiplayer.setOnAction(e -> sm.requestStateChange(MultiplayerGameState.class));
		btnOptions.setOnAction(e -> sm.requestStateChange(OptionState.class));
		btnExit.setOnAction(e -> game.requestStop());
		
		Rectangle viewport = camera.getComponent(UserViewComponent.class).getViewport();
		viewport.widthProperty().bind(root.widthProperty());
		viewport.heightProperty().bind(root.heightProperty());
		
	}
	
	@Override
	public TranslateTransition getSlideTransition() {
		TranslateTransition slide = super.getSlideTransition();
		slide.setFromX(0);
		slide.setToX(-guiContainer.getWidth());
		return slide;
	}
	
}
