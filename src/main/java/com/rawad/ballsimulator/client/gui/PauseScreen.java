package com.rawad.ballsimulator.client.gui;

import com.rawad.gamehelpers.resources.Loader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class PauseScreen extends GridPane {
	
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
		
		resume.setOnAction(e -> this.setVisible(false));
		
		visibleProperty().addListener((e, prevVisible, currentlyVisible) -> {
			
			if(!currentlyVisible) {
				getParent().requestFocus();
			}
			
		});
		
	}
	
	public Button getResume() {
		return resume;
	}
	
	public Button getMainMenu() {
		return mainMenu;
	}
	
}
