package com.rawad.ballsimulator.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.world.terrain.Terrain;

public class World {
	
	private Terrain terrain;
	
	private ArrayList<Entity> entities;
	
	private int width;
	private int height;
	
	public World() {
		
		entities = new ArrayList<Entity>();
		terrain = new Terrain();
		
		width = 1024;
		height = 1024;
		
	}
	
	public void update(long timePassed) {
		
		for(Entity e: entities) {
			e.update(timePassed);
		}
		
	}
	
	public void render(Graphics2D g) {
		
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, width, height);
		
		terrain.render(g);
		
		renderSquares(g);
		
		for(Entity e: entities) {
			e.render(g);
		}
		
	}
	
	private void renderSquares(Graphics2D g) {
		
		g.setColor(Color.BLACK);
		
		final int spacing = 32;
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				
				if(x % spacing == 0 && y % spacing == 0) {
					g.fillRect(x, y, 1, 1);
				}
				
			}
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
	
	public boolean mapCollision(Rectangle hitbox) {
		return terrain.calculateCollision(hitbox);
	}
	
	public Terrain getTerrain() {
		return terrain;
	}
	
	public void addEntity(Entity e) {
		
		this.entities.add(e);
	}
	
	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
