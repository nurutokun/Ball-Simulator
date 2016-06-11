package com.rawad.ballsimulator.client.gui;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;

/**
 * Used instead of <code>Canvas</code> when it doesn't want to resize properly.
 * 
 * @author Rawad
 *
 */
public class CanvasPane extends Region {
	
	private final Canvas canvas;
	
	public CanvasPane() {
		
		canvas = new Canvas();
		
		getChildren().add(canvas);
		
	}
	
	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		
		final double width = getWidth();
		final double height = getHeight();
		
		final Insets insets = getInsets();
		
		final double contentX = insets.getLeft();
		final double contentY = insets.getTop();
		final double contentWidth = Math.max(0, width - (contentX + insets.getRight()));
		final double contentHeight = Math.max(0, height - (contentY + insets.getBottom()));
		
		canvas.relocate(contentX, contentY);
		canvas.setWidth(contentWidth);
		canvas.setHeight(contentHeight);
		
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
}
