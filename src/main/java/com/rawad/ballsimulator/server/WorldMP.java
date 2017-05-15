package com.rawad.ballsimulator.server;

import java.util.ArrayList;
import java.util.Collection;

import com.rawad.ballsimulator.game.World;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;


/**
 * @author Rawad
 *
 */
public class WorldMP extends World {
	
	private SimpleListProperty<Entity> players = new SimpleListProperty<Entity>(FXCollections.observableArrayList());
	
	private int entityIdCounter = 0;
	
	/**
	 * 
	 * @param entities
	 */
	public WorldMP(ArrayList<Entity> entities) {
		super(entities);
	}
	
	@Override
	public boolean addEntity(Entity e) {
		
		NetworkComponent networkComp = e.getComponent(NetworkComponent.class);
		
		if(networkComp != null) {
			networkComp.setId(entityIdCounter++);
			
			if(entityIdCounter >= Integer.MAX_VALUE) entityIdCounter = Integer.MIN_VALUE;
			
			if(entityIdCounter == -1) Logger.log(Logger.SEVERE, "The absolute entity cap for this world has "
					+ "been reached.");
		}
		
		if(e.getComponent(UserComponent.class) != null) {
			players.add(e);
		}
		
		return super.addEntity(e);
		
	}
	
	/**
	 * @see com.rawad.ballsimulator.game.World#removeEntity(com.rawad.gamehelpers.game.entity.Entity)
	 */
	@Override
	public boolean removeEntity(Entity e) {
		
		players.remove(e);
		
		return super.removeEntity(e);
		
	}
	
	/**
	 * @see com.rawad.ballsimulator.game.World#removeAllEntities(java.util.Collection)
	 */
	@Override
	public boolean removeAllEntities(Collection<Entity> entitiesToRemove) {
		
		players.removeAll(entitiesToRemove);
		
		return super.removeAllEntities(entitiesToRemove);
		
	}
	
	public SimpleListProperty<Entity> playersProperty() {
		return this.players;
	}
	
}

