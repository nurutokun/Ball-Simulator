package com.rawad.ballsimulator.entity;

import javax.xml.bind.annotation.XmlTransient;

import com.rawad.ballsimulator.geometry.Rectangle;
import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;

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
	
	private boolean collideX = false;
	private boolean collideY = false;
	
	public void setHitbox(Rectangle hitbox) {// Mainly needed for xml.
		this.hitbox = hitbox;
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public SimpleObjectProperty<Entity> getCollidingWithEntity() {
		return collidingWith;
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
	@XmlTransient public void setCollideX(boolean collideX) {
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
	@XmlTransient public void setCollideY(boolean collideY) {
		this.collideY = collideY;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof CollisionComponent) {
			
			CollisionComponent collisionComp = (CollisionComponent) comp;
			
			collisionComp.setCollideX(isCollideX());
			collisionComp.setCollideY(isCollideY());
			
			Rectangle hitbox = collisionComp.getHitbox();
			
			if(!hitbox.xProperty().isBound()) hitbox.setX(this.hitbox.getX());
			if(!hitbox.yProperty().isBound()) hitbox.setY(this.hitbox.getY());
			if(!hitbox.widthProperty().isBound()) hitbox.setWidth(this.hitbox.getWidth());
			if(!hitbox.heightProperty().isBound()) hitbox.setHeight(this.hitbox.getHeight());
			
			collisionComp.getCollidingWithEntity().set(getCollidingWithEntity().get());
			
		}
		
		return comp;
	}
	
}
