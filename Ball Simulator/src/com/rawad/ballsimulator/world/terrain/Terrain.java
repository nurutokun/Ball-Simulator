package com.rawad.ballsimulator.world.terrain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

public class Terrain {
	
	private ArrayList<TerrainComponent> terrainComponents;
	
	public Terrain() {
		terrainComponents = new ArrayList<TerrainComponent>();
	}
	
	public void render(Graphics2D g) {
		
		for(TerrainComponent comp: terrainComponents) {
			comp.render(g, Color.GREEN);
		}
		
	}
	
	public synchronized boolean calculateCollision(Rectangle hitbox) {
		
		for(TerrainComponent comp: terrainComponents) {
			
			if(comp.intersects(hitbox)) {
				return true;
			}
			
		}
		
		return false;
		
	}
	
	public synchronized TerrainComponent calculateCollision(int x, int y) {
		
		for(TerrainComponent comp: terrainComponents) {
			
			if(comp.intersects(x, y)) {
				return comp;
			}
			
		}
		
		return null;
		
	}
	
	public synchronized void addTerrainComponent(double x, double y, int width, int height) {
		terrainComponents.add(new TerrainComponent(x, y, width, height));
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
