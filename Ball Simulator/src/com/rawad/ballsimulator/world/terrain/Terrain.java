package com.rawad.ballsimulator.world.terrain;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Terrain {
	
	private ArrayList<TerrainComponent> terrainComponents;
	
	public Terrain() {
		terrainComponents = new ArrayList<TerrainComponent>();
	}
	
	public void render(Graphics2D g) {
		
		for(TerrainComponent comp: terrainComponents) {
			comp.render(g);
		}
		
	}
	
	public boolean calculateCollision(Rectangle hitbox) {
		
		for(TerrainComponent comp: terrainComponents) {
			
			if(comp.intersects(hitbox)) {
				return true;
			}
			
		}
		
		return false;
		
	}
	
	public void addTerrainComponent(TerrainComponent comp) {
		terrainComponents.add(comp);
	}
	
}
