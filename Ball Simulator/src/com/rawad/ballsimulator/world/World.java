package com.rawad.ballsimulator.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.world.terrain.Terrain;
import com.rawad.gamehelpers.game.GameManager;

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
	
	public void update(long timePassed) {
		
		for(Entity e: entities) {
			
			if(!(e instanceof EntityPlayer)) {// Players are handled in a special way, for now.
				e.update(timePassed);
			}
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, width, height);
		
		terrain.render(g);
		
		if(GameManager.getGame() != null && GameManager.getGame().isDebug()) {
			renderSquares(g);
		}
		
		AffineTransform af = g.getTransform();
		
		for(Entity e: entities) {
			e.render(g);
			
			g.setTransform(af);// Resets transform because of the rotating entities.
		}
		
	}
	
	/**
	 * Real lagger.
	 * 
	 * @param g
	 */
	private void renderSquares(Graphics2D g) {
		
		g.setColor(Color.BLACK);
		
		final int spacing = (width+height)/32;//64 -> Any smaller and it would lag a lot. 
		
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
	
	public synchronized void removeEntity(Entity e) {
		entities.remove(e);
	}
	
	public synchronized void removeEntityByName(String name) {
		
		Entity entityToRemove = null;
		
		for(Entity e: entities) {
			
			if(e.getName().equals(name)) {
				entityToRemove = e;
			}
			
		}
		
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
