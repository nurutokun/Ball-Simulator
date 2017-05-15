package com.rawad.ballsimulator.entity;

import com.rawad.ballsimulator.geometry.Rectangle;
import com.rawad.gamehelpers.game.entity.Component;

/**
 * Gives {@code Entity} a hitbox and allows for colliding with.
 * 
 * @author Rawad
 *
 */
public class CollisionComponent extends Component {
	
	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);
	
	public void setHitbox(Rectangle hitbox) {// Mainly needed for xml.
		this.hitbox = hitbox;
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof CollisionComponent) {
			
			CollisionComponent collisionComp = (CollisionComponent) comp;
			
			Rectangle hitbox = collisionComp.getHitbox();
			
			if(!hitbox.xProperty().isBound()) hitbox.setX(this.hitbox.getX());
			if(!hitbox.yProperty().isBound()) hitbox.setY(this.hitbox.getY());
			if(!hitbox.widthProperty().isBound()) hitbox.setWidth(this.hitbox.getWidth());
			if(!hitbox.heightProperty().isBound()) hitbox.setHeight(this.hitbox.getHeight());
			
		}
		
		return comp;
	}
	
}
