package com.rawad.ballsimulator.client.gui;

import com.rawad.gamehelpers.client.gui.IHideable;
import com.rawad.gamehelpers.resources.Loader;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class PauseScreen extends GridPane implements IHideable {
	
	@FXML private Button resume;
	@FXML private Button mainMenu;
	
	private FadeTransition fadeTransition;
	
	public PauseScreen() {
		
		FXMLLoader loader = new FXMLLoader(Loader.getFxmlLocation(getClass()));
		
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		resume.setOnAction(e -> hide());
		
		fadeTransition = new FadeTransition(Duration.millis(300), this);
		
	}
	
	@Override
	public void show() {
		
		if(isVisible()) return;
		
		fadeTransition.setFromValue(0.1d);
		fadeTransition.setToValue(1.0d);
		fadeTransition.setOnFinished(null);
		
		setVisible(true);
		
		fadeTransition.playFromStart();
		
	}
	
	@Override
	public void hide() {
		
		if(!isVisible()) return;
		
		fadeTransition.setFromValue(1.0d);
		fadeTransition.setToValue(0.1d);
		fadeTransition.setOnFinished(e -> {
			setVisible(false);
			getParent().requestFocus();
		});
		
		fadeTransition.playFromStart();
		
	}
	
	@Override
	public boolean isShowing() {
		return isVisible();
	}
	
	public Button getResume() {
		return resume;
	}
	
	public Button getMainMenu() {
		return mainMenu;
	}
	
}
