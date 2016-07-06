package com.rawad.ballsimulator.client.gui;

import com.rawad.gamehelpers.client.gui.Transitions;

import javafx.animation.ParallelTransition;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

public final class Root extends StackPane {
	
	private final Canvas canvas;
	private final StackPane guiContainer;
	
	private final String styleSheet;
	
	public Root(StackPane guiContainer, String styleSheet) {
		super();
		
		canvas = new Canvas();
		
		canvas.widthProperty().bind(this.widthProperty());
		canvas.heightProperty().bind(this.heightProperty());
		
		this.getChildren().add(canvas);
		
		this.guiContainer = guiContainer;
		
		this.getChildren().add(guiContainer);
		
		this.styleSheet = styleSheet;
		
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public StackPane getGuiContainer() {
		return guiContainer;
	}
	
	public String getStyleSheet() {
		return styleSheet;
	}
	
	public ParallelTransition getOutTransition() {
		
		double from = 0d;
		
		ParallelTransition out = Transitions.parallel(guiContainer, null, Transitions.slideHorizontally(
				Transitions.DEFAULT_DURATION, from, from + 10));
		
		return out;
	}
	
}
