package com.rawad.ballsimulator.client.gui;

import java.io.IOException;
import java.util.HashMap;

import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.log.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Holds bindings of {@code State} objects to {@code Root} root objects which is what the {@code Client} needs to use
 * when changing between states.
 * 
 * @author Rawad
 *
 */
public final class GuiRegister {
	
	private static final HashMap<State, Root> roots = new HashMap<State, Root>();
	
	private static final String DEFAULT_STYLESHEET = "StyleSheet";
	
	private GuiRegister() {}
	
	public static Root loadGui(State state, String styleSheet) {
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setController(state);
		
		StackPane guiContainer = new StackPane();
		fxmlLoader.setRoot(guiContainer);
		
		Root root = new Root(guiContainer, styleSheet);
		root.getStylesheets().add(Loader.getStyleSheetLocation(state.getClass(), styleSheet));
		
		try {
			
			fxmlLoader.load(Loader.streamLayoutFile(state.getClass()));
			
		} catch(IOException ex) {
			ex.printStackTrace();
			
			guiContainer.getChildren().add(new Label("Error initializing this state " + state.getClass().getSimpleName() 
					+ "; " + ex.getMessage()));
		}
		
		roots.put(state, root);
		
		return root;
		
	}
	
	public static Root loadGui(State state) {
		return loadGui(state, DEFAULT_STYLESHEET);
	}
	
	public static Root getRoot(State state) {
		Root root = roots.get(state);
		
		if(root == null) {
			root = new Root(new StackPane(), "");
			Logger.log(Logger.WARNING, "GUI for " + state + " wasn't initialized.");
		}
		
		return root;
	}
	
}
