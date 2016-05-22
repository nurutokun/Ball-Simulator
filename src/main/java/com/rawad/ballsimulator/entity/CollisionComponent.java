package com.rawad.ballsimulator.entity;

import java.util.ArrayList;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.entity.Listener;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.beans.property.SimpleObjectProperty;

/**
 * Gives {@code Entity} a hitbox and allows for colliding with.
 * 
 * @author Rawad
 *
 */
public class CollisionComponent extends Component {
	
	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);
	
	private SimpleObjectProperty<Entity> collidingWith = new SimpleObjectProperty<Entity>();
	
	private ArrayList<Listener<CollisionComponent>> listeners = new ArrayList<Listener<CollisionComponent>>();
	
	private boolean collideX = false;
	private boolean collideY = false;
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public SimpleObjectProperty<Entity> getCollidingWithEntity() {
		return collidingWith;
	}
	
	public ArrayList<Listener<CollisionComponent>> getListeners() {
		return listeners;
	}
	
	/**
	 * @return the collideX
	 */
	public boolean isCollideX() {
		return collideX;
	}
	
	/**
	 * @param collideX the collideX to set
	 */
	public void setCollideX(boolean collideX) {
		this.collideX = collideX;
	}
	
	/**
	 * @return the collideY
	 */
	public boolean isCollideY() {
		return collideY;
	}
	
	/**
	 * @param collideY the collideY to set
	 */
	public void setCollideY(boolean collideY) {
		this.collideY = collideY;
	}
	
}
