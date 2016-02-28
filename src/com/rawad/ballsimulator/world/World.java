package com.rawad.ballsimulator.world;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.world.terrain.Terrain;

public class World {
	
	protected Terrain terrain;
	
	protected ArrayList<Entity> entities;
	
	protected int width;
	protected int height;
	
	public World() {
		
		entities = new ArrayList<Entity>();
		terrain = new Terrain();
		
//		width = 8192;
//		height = 8192;
		
		width = 2048;
		height = 2048;
		
	}
	
	public synchronized void update() {
		
		for(Entity e: entities) {
			e.update();
		}
		
	}
	
	public boolean entityCollision(Entity e) {
		
		for(Entity e2: entities) {
			
			if(!e2.equals(e)) {
				if(e2.intersects(e)) {
					return true;
				}
			}
			
		}
		
		return false;
		
	}
	
	public synchronized void removeEntity(Entity e) {
		entities.remove(e);
	}
	
	public synchronized void removeEntityByName(String name) {
		
		Entity entityToRemove = getEntityByName(name);
		
		if(entityToRemove != null) {
			entities.remove(entityToRemove);
		}
		
	}
	
	public Entity getEntityByName(String name) {
		
		for(Entity e: entities) {
			
			if(e.getName().equals(name)) {
				return e;
			}
			
		}
		
		return null;
	}
	
	public boolean mapCollision(Rectangle hitbox) {
		return terrain.calculateCollision(hitbox);
	}
	
	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
	
	public Terrain getTerrain() {
		return terrain;
	}
	
	public void addEntity(Entity e) {
		
		this.entities.add(e);
		
	}
	
	public void generateCoordinates(Entity e) {
		
		boolean entityBlocked = true;
		
		int width = e.getWidth();
		int height = e.getHeight();
		
		Random r = new Random();
		
		do {
			
			int x = (int) (r.nextDouble() * (this.width - (width/2d)));
			int y = (int) (r.nextDouble() * (this.height - (height/2d)));
			// (Outdated)   First one ^ keeps it from being too far left, second one ^ keeps it from being too far right.
			
			e.setX(x);
			e.setY(y);
			
			e.updateHitbox();
			
			if(!mapCollision(e.getHitbox())) {
				entityBlocked = false;
			}
			
		} while(entityBlocked);
		
	}
	
	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
