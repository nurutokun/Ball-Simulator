package com.rawad.ballsimulator.client.gui;

import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.utils.Util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class Messenger extends BorderPane {
	
	@FXML private TextArea outputArea;
	@FXML private TextField inputArea;
	
	private boolean addedScrollbarFocuser;
	
	public Messenger() {
		
		FXMLLoader loader = new FXMLLoader(Loader.getFxmlLocation(getClass()));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		inputArea.setOnAction(e -> {
			String text = inputArea.getText();
			
			if(!text.isEmpty()) {
				outputArea.appendText((outputArea.getText().isEmpty()? "":Util.NL) + text);
			}
			
			setShowing(false);
			
		});

		ChangeListener<Boolean> focusKeeper = new ChangeListener<Boolean>() {
			
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				
				if(isShowing()) {
					inputArea.requestFocus();
				} else {
					getParent().requestFocus();
				}
				
				if(!addedScrollbarFocuser) {
					ScrollBar bar = (ScrollBar) outputArea.lookup(".scroll-bar:vertical");
					bar.focusedProperty().addListener(this);// Keep at least until we know where scrollbar is initialized...
					bar.setOnMouseReleased(e -> changed(null, false, false));
					addedScrollbarFocuser = true;
				}
			}
			
		};
		
		inputArea.visibleProperty().addListener(focusKeeper);
		inputArea.focusedProperty().addListener(e -> inputArea.setText(""));
		outputArea.focusedProperty().addListener(focusKeeper);
		
	}
	
	public void setShowing(boolean showing) {
		inputArea.setVisible(showing);
	}
	
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