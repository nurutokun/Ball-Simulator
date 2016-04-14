package com.rawad.ballsimulator.client.gamestates;

import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.renderengine.BackgroundRender;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MenuState extends State {
	
	@FXML private Label lblTitle;
	
	@FXML private Button btnSingleplayer;
	@FXML private Button btnMultiplayer;
	@FXML private Button btnOptions;
	@FXML private Button btnExit;
	
	@Override
	public void initGui() {
		super.initGui();
		
		lblTitle.setText(sm.getGame().toString());
		
		btnSingleplayer.setOnAction(e -> sm.requestStateChange(GameState.class));
		btnMultiplayer.setOnAction(e -> sm.requestStateChange(MultiplayerGameState.class));
		btnOptions.setOnAction(e -> sm.requestStateChange(OptionState.class));
		btnExit.setOnAction(e -> sm.getGame().requestStop());
		
	}
	
	@Override
	public void render() {
		super.render();
		
		BackgroundRender.instance().render(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight());
		
	}
	
}
