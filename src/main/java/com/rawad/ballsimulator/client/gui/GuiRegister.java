package com.rawad.ballsimulator.client.gui;

import java.io.IOException;
import java.util.HashMap;

import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.Loader;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Holds bindings of {@code State} objects to {@code Parent} root objects which is what the {@code Client} needs to use
 * when changing between states.
 * 
 * @author Rawad
 *
 */
public final class GuiRegister {
	
	private static final HashMap<State, Root> roots = new HashMap<State, Root>();
	
	private static final String DEFAULT_STYLESHEET = "Stylesheet";
	
	private GuiRegister() {}
	
	public static final Root loadGui(State state, String styleSheet) {
		
		FXMLLoader loader = new FXMLLoader(Loader.getFxmlLocation(state.getClass()));
		loader.setController(state);
		
		StackPane guiContainer = new StackPane();
		loader.setRoot(guiContainer);
		
		Root root = new Root(guiContainer);
		root.getStylesheets().add(Loader.getStyleSheetLocation(state.getClass(), styleSheet));		
		
		try {
			
			loader.load();
			
		} catch(IOException ex) {
			ex.printStackTrace();
			
			guiContainer.getChildren().add(new Label("Error initializing this state " + state.getClass().getSimpleName() 
					+ "; " + ex.getMessage()));
		}
		
		roots.put(state, root);
		
		return root;
		
	}
	
	public static final Root loadGui(State state) {
		return loadGui(state, DEFAULT_STYLESHEET);
	}
	
	public static final Root getRoot(State state) {
		Root root = roots.get(state);
		
		if(root == null) {
			root = new Root(new StackPane());
			Logger.log(Logger.DEBUG, "GUI for " + state + " wasn't initialized.");
		}
		
		return root;
	}
	
}
