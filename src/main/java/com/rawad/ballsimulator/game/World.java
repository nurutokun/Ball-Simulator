package com.rawad.ballsimulator.game;

import java.util.ArrayList;
import java.util.Collection;

import com.rawad.gamehelpers.game.entity.Entity;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class World {
	
	private final SimpleListProperty<Entity> entitiesProperty;
	private final ArrayList<Entity> entities;
	
	private double width = 4096d;
	private double height = 4096d;
	
	/**
	 * 
	 * @param entities
	 */
	public World(ArrayList<Entity> entities) {
		super();
		
		entitiesProperty = new SimpleListProperty<Entity>(FXCollections.observableArrayList(entities));
		
		this.entities = entities;
		
	}
	
	public boolean removeAllEntities(Collection<Entity> entitiesToRemove) {
		return entities.removeAll(entitiesToRemove);
	}
	
	public boolean addEntity(Entity e) {
		return entities.add(e);
	}
	
	public boolean removeEntity(Entity e) {
		return entities.remove(e);
	}
	
	public ObservableList<Entity> getEntities() {
		return entitiesProperty().getValue();
	}
	
	public SimpleListProperty<Entity> entitiesProperty() {
		return entitiesProperty;
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
}
