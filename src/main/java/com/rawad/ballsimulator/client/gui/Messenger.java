package com.rawad.ballsimulator.client.gui;

import com.rawad.gamehelpers.client.gui.Hideable;
import com.rawad.gamehelpers.utils.Util;
import com.rawad.jfxengine.loader.GuiLoader;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class Messenger extends BorderPane implements Hideable {
	
	@FXML private TextArea outputArea;
	@FXML private TextField inputArea;
	
	public Messenger() {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load(GuiLoader.streamLayoutFile(getClass()));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		inputArea.setOnAction(e -> {
			// setOnAction() gets called after any event handlers; allows for external text modification.
			
			appendNewLine(inputArea.getText());
			
			hide();
			
		});

		final ChangeListener<Boolean> focusKeeper = new ChangeListener<Boolean>() {
			
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				
				if(isShowing()) {
					inputArea.requestFocus();
				} else {
					getParent().requestFocus();
				}
				
			}
			
		};
		
		inputArea.visibleProperty().addListener(focusKeeper);
		outputArea.focusedProperty().addListener(focusKeeper);
		
		sceneProperty().addListener((e, prevScene, curScene) -> {
			Platform.runLater(() -> {
				
				ScrollBar bar = (ScrollBar) outputArea.lookup(".scroll-pane .scroll-bar:vertical");
				
				if(bar == null) return;
				
				if(prevScene == null) {
					
					bar.focusedProperty().addListener(focusKeeper);
					bar.setOnMouseReleased(mouseEvent -> focusKeeper.changed(null, false, false));
					
				} else if(curScene == null) {
					
					bar.focusedProperty().removeListener(focusKeeper);
					bar.setOnMouseReleased(null);
					
				}
				
			});
		});
		
	}
	
	public void appendNewLine(String text) {
		
		if(!text.isEmpty()) {
			outputArea.appendText((outputArea.getText().isEmpty()? "":Util.NL) + text);
		}
		
	}
	
	@Override
	public void show() {
		
		inputArea.setText("");
		inputArea.setVisible(true);
		
	}
	
	@Override
	public void hide() {
		
		inputArea.setVisible(false);
		inputArea.setText("");
		
	}
	
	@Override
	public boolean isShowing() {
		return inputArea.isVisible();
	}
	
	public TextArea getOutputArea() {
		return outputArea;
	}
	
	public TextField getInputArea() {
		return inputArea;
	}
	
}