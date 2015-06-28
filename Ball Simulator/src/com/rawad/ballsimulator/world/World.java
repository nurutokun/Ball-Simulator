package com.rawad.ballsimulator.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.world.terrain.Terrain;
import com.rawad.ballsimulator.world.terrain.TerrainComponent;

public class World {
	
	private Terrain terrain;
	
	private ArrayList<Entity> entities;
	
	private int width;
	private int height;
	
	public World() {
		
		entities = new ArrayList<Entity>();
		terrain = new Terrain();
		
		terrain.addTerrainComponent(new TerrainComponent(100, 100, 20, 20));
		terrain.addTerrainComponent(new TerrainComponent(100, 130, 20, 20));
		
		width = 900;
		height = 900;
		
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
		
		for(Entity e: entities) {
			e.render(g);
		}
		
	}
	
	public boolean mapCollision(Rectangle hitbox) {
		return false;
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
