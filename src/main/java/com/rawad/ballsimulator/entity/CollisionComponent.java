package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;
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
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public SimpleObjectProperty<Entity> getCollidingWithEntity() {
		return collidingWith;
	}
	
}
