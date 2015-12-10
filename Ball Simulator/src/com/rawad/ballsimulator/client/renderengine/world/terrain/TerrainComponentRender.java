package com.rawad.ballsimulator.client.renderengine.world.terrain;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.rawad.ballsimulator.world.terrain.TerrainComponent;
import com.rawad.gamehelpers.renderengine.LayeredRender;

public class TerrainComponentRender extends LayeredRender {
	
	private ArrayList<TerrainComponent> components;
	
	public TerrainComponentRender() {
		components = new ArrayList<TerrainComponent>();
	}
	
	@Override
	public void render(Graphics2D g) {
		
		for(TerrainComponent comp: components) {
			comp.render(g, comp.getHighLightColor());
		}
		
		components.clear();
		
	}
	
	public void addComponent(TerrainComponent comp) {
		components.add(comp);
	}
	
}
