package com.rawad.ballsimulator.client.renderengine;

import com.rawad.gamehelpers.client.renderengine.Render;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TerrainRender extends Render {
	
	public void render(GraphicsContext g, double width, double height) {
		
		g.setFill(Color.GRAY);
		g.fillRect(0, 0, width, height);
		
	}
	
}
