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
	
	private boolean collidingUp = false;
	private boolean collidingDown = false;
	private boolean collidingRight = false;
	private boolean collidingLeft = false;
	
	private boolean outOfBoundsUp = false;
	private boolean outOfBoundsDown = false;
	private boolean outOfBoundsRight = false;
	private boolean outOfBoundsLeft = false;
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public SimpleObjectProperty<Entity> getCollidingWithEntity() {
		return collidingWith;
	}
	
	/**
	 * @return the collidingUp
	 */
	public boolean isCollidingUp() {
		return collidingUp;
	}
	
	/**
	 * @param collidingUp the collidingUp to set
	 */
	public void setCollidingUp(boolean collidingUp) {
		this.collidingUp = collidingUp;
	}
	
	/**
	 * @return the collidingDown
	 */
	public boolean isCollidingDown() {
		return collidingDown;
	}
	
	/**
	 * @param collidingDown the collidingDown to set
	 */
	public void setCollidingDown(boolean collidingDown) {
		this.collidingDown = collidingDown;
	}
	
	/**
	 * @return the collidingRight
	 */
	public boolean isCollidingRight() {
		return collidingRight;
	}
	
	/**
	 * @param collidingRight the collidingRight to set
	 */
	public void setCollidingRight(boolean collidingRight) {
		this.collidingRight = collidingRight;
	}
	
	/**
	 * @return the collidingLeft
	 */
	public boolean isCollidingLeft() {
		return collidingLeft;
	}
	
	/**
	 * @param collidingLeft the collidingLeft to set
	 */
	public void setCollidingLeft(boolean collidingLeft) {
		this.collidingLeft = collidingLeft;
	}
	
	/**
	 * @return the outOfBoundsUp
	 */
	public boolean isOutOfBoundsUp() {
		return outOfBoundsUp;
	}
	
	/**
	 * @param outOfBoundsUp the outOfBoundsUp to set
	 */
	public void setOutOfBoundsUp(boolean outOfBoundsUp) {
		this.outOfBoundsUp = outOfBoundsUp;
	}
	
	/**
	 * @return the outOfBoundsDown
	 */
	public boolean isOutOfBoundsDown() {
		return outOfBoundsDown;
	}
	
	/**
	 * @param outOfBoundsDown the outOfBoundsDown to set
	 */
	public void setOutOfBoundsDown(boolean outOfBoundsDown) {
		this.outOfBoundsDown = outOfBoundsDown;
	}
	
	/**
	 * @return the outOfBoundsRight
	 */
	public boolean isOutOfBoundsRight() {
		return outOfBoundsRight;
	}
	
	/**
	 * @param outOfBoundsRight the outOfBoundsRight to set
	 */
	public void setOutOfBoundsRight(boolean outOfBoundsRight) {
		this.outOfBoundsRight = outOfBoundsRight;
	}
	
	/**
	 * @return the outOfBoundsLeft
	 */
	public boolean isOutOfBoundsLeft() {
		return outOfBoundsLeft;
	}
	
	/**
	 * @param outOfBoundsLeft the outOfBoundsLeft to set
	 */
	public void setOutOfBoundsLeft(boolean outOfBoundsLeft) {
		this.outOfBoundsLeft = outOfBoundsLeft;
	}
	
}
