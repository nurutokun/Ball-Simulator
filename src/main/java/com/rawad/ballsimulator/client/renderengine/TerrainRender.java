package com.rawad.ballsimulator.client.renderengine;

import com.rawad.gamehelpers.client.renderengine.LayeredRender;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TerrainRender extends LayeredRender {
	
	@Override
	public void render(GraphicsContext g) {
		
		g.setFill(Color.GREEN);
		g.fillRect(0, 0, 2048, 2048);
		
	}
	
	@Override
	public void render(GraphicsContext g, Entity e) {}
	
}
