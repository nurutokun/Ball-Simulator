package com.rawad.ballsimulator.client.gui;

import com.rawad.gamehelpers.client.gui.IHideable;
import com.rawad.gamehelpers.client.gui.Transitions;
import com.rawad.gamehelpers.resources.ALoader;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class PauseScreen extends GridPane implements IHideable {
	
	@FXML private Button resume;
	@FXML private Button mainMenu;
	
	public PauseScreen() {
		
		FXMLLoader loader = new FXMLLoader(ALoader.getFxmlLocation(getClass()));
		
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
		
		Platform.runLater(() -> {
			setVisible(true);
			FadeTransition fadeIn = Transitions.fade(Duration.millis(300), Transitions.HIDDEN, Transitions.OPAQUE);
			fadeIn.setNode(this);
			fadeIn.playFromStart();
		});
		
	}
	
	@Override
	public void hide() {
		
		if(!isVisible()) return;
		
		FadeTransition fadeOut = Transitions.fade(Duration.millis(50), Transitions.OPAQUE, Transitions.HIDDEN);
		fadeOut.setOnFinished(e -> {
			setVisible(false);
			getParent().requestFocus();
		});
		fadeOut.setNode(this);
		fadeOut.playFromStart();
		
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
