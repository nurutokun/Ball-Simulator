package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.gui.Transitions;
import com.rawad.ballsimulator.client.renderengine.BackgroundRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.animation.Transition;
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
	public Transition getOnActivateTransition() {
		// TODO: Change default in Transitions to slide to right then customize translate transition for this one.
		// Note: Transition isn't be shown right after the LoadingState is done so it shouldn't look weird/won't need to
		// keep track of next/prev states to determine different animations to play.
		// Note: This Transition doesn't seem to be sliding properly anyways.
		return Transitions.stateOnActivate(guiContainer, Transitions.DEFAULT_DURATION);
	}
	
	@Override
	public Transition getOnDeactivateTransition() {
		return Transitions.stateOnDeactivate(guiContainer, Transitions.DEFAULT_DURATION);
	}
	
}
