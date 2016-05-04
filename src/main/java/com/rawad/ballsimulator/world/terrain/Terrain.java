package com.rawad.ballsimulator.world.terrain;

import java.util.ArrayList;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Terrain {
	
	private ArrayList<TerrainComponent> terrainComponents;
	
	public Terrain() {
		terrainComponents = new ArrayList<TerrainComponent>();
	}
	
	public synchronized boolean calculateCollision(Rectangle hitbox) {
		
		for(TerrainComponent comp: terrainComponents) {
			
			if(Shape.intersect(comp.getHitbox(), hitbox) != null) {
				return true;
			}
			
		}
		
		return false;
		
	}
	
	public synchronized TerrainComponent calculateCollision(int x, int y) {
		
		for(TerrainComponent comp: terrainComponents) {
			
			if(comp.getHitbox().contains(x, y)) {
				return comp;
			}
			
		}
		
		return null;
		
	}
	
	public synchronized void addTerrainComponent(TerrainComponent comp) {
		terrainComponents.add(comp);
	}
	
	public synchronized void removeTerrainComponent(TerrainComponent comp) {
		terrainComponents.remove(comp);
	}
	
	public synchronized ArrayList<TerrainComponent> getTerrainComponents() {
		return terrainComponents;
	}
	
	public synchronized void setTerrainComponents(ArrayList<TerrainComponent> components) {
		terrainComponents = components;
	}
	
}
