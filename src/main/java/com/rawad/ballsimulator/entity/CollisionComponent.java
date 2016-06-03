package com.rawad.ballsimulator.entity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.entity.IListener;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.beans.property.SimpleObjectProperty;

/**
 * Gives {@code Entity} a hitbox and allows for colliding with.
 * 
 * @author Rawad
 *
 */
@XmlRootElement
public class CollisionComponent extends Component {
	
	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);
	
	@XmlTransient private SimpleObjectProperty<Entity> collidingWith = new SimpleObjectProperty<Entity>();
	
	@XmlTransient private ArrayList<IListener<CollisionComponent>> listeners = 
			new ArrayList<IListener<CollisionComponent>>();
	
	private boolean collideX = false;
	private boolean collideY = false;
	
	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public SimpleObjectProperty<Entity> getCollidingWithEntity() {
		return collidingWith;
	}
	
	public ArrayList<IListener<CollisionComponent>> getListeners() {
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
			
			collisionComp.getListeners().addAll(getListeners());
			
			collisionComp.getCollidingWithEntity().set(getCollidingWithEntity().get());
			
			return collisionComp;
			
		}
		
		return comp;
	}
	
}
