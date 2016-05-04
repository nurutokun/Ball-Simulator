package com.rawad.ballsimulator.client.renderengine.world.terrain;

import java.util.ArrayList;

import com.rawad.ballsimulator.world.terrain.TerrainComponent;
import com.rawad.gamehelpers.client.renderengine.LayeredRender;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TerrainComponentRender extends LayeredRender {
	
	private ArrayList<TerrainComponent> components;
	
	public TerrainComponentRender() {
		components = new ArrayList<TerrainComponent>();
	}
	
	@Override
	public synchronized void render(GraphicsContext g) {
		
		for(TerrainComponent comp: components) {
			
			if(comp.isSelected()) {
				g.setStroke(comp.getHighlightColor());
			} else {
				g.setStroke(Color.RED);
			}
			
			g.translate(comp.getX(), comp.getY());
			
			g.strokeRect(0, 0, comp.getWidth(), comp.getHeight());
			
			g.translate(-comp.getX(), -comp.getY());
			
		}
			
		components.clear();
			
	}
	
	public synchronized void addComponent(TerrainComponent comp) {
		components.add(comp);
	}
	
}
