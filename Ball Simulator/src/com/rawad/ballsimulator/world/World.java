package com.rawad.ballsimulator.world;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.rawad.ballsimulator.entity.Entity;

public class World {
	
	private ArrayList<Entity> entities;
	
	public World() {
		
		entities = new ArrayList<Entity>();
		
	}
	
	public void update(long timePassed) {
		
		for(Entity e: entities) {
			e.update(timePassed);
		}
		
	}
	
	public void render(Graphics2D g) {
		
		for(Entity e: entities) {
			e.render(g);
		}
		
	}
	
	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}
	
}
