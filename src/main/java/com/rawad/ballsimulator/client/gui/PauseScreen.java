package com.rawad.ballsimulator.client.gui;

import com.rawad.gamehelpers.client.gui.Hideable;
import com.rawad.jfxengine.loader.GuiLoader;
import com.rawad.jfxengine.transitions.Transitions;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class PauseScreen extends GridPane implements Hideable {
	
	@FXML private Button resume;
	@FXML private Button mainMenu;
	
	public PauseScreen() {
		super();
		
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load(GuiLoader.streamLayoutFile(getClass()));
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
			FadeTransition fadeIn = Transitions.fade(Duration.millis(300), Transitions.HIDDEN, Transitions.SHOWING);
			fadeIn.setNode(this);
			fadeIn.playFromStart();
		});
		
	}
	
	@Override
	public void hide() {
		
		if(!isVisible()) return;
		
		FadeTransition fadeOut = Transitions.fade(Duration.millis(50), Transitions.SHOWING, Transitions.HIDDEN);
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
