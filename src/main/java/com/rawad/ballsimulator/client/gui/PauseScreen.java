package com.rawad.ballsimulator.client.gui;

import com.rawad.gamehelpers.client.gui.IHideable;
import com.rawad.gamehelpers.resources.Loader;

import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class PauseScreen extends GridPane implements IHideable {
	
	@FXML private Button resume;
	@FXML private Button mainMenu;
	
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
		
	}
	
	@Override
	public void show() {
		
		if(isVisible()) return;
		
		setVisible(true);
		
		Transitions.fade(this, Duration.millis(300), Transitions.OPAQUE).playFromStart();
		
	}
	
	@Override
	public void hide() {
		
		if(!isVisible()) return;
		
		Transition transition = Transitions.fade(this, Duration.millis(50), Transitions.HIDDEN);
		transition.setOnFinished(e -> {
			setVisible(false);
			getParent().requestFocus();
		});
		transition.playFromStart();
		
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
