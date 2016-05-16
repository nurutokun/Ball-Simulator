package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.renderengine.LayeredRender;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EntityRender extends LayeredRender {
	
	@Override
	public void render(GraphicsContext g, Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
		
		if(collisionComp != null) {
			
			Rectangle hitbox = collisionComp.getHitbox();
			
			g.scale(transformComp.getScale(), transformComp.getScale());
			g.rotate(transformComp.getTheta());
			g.translate(transformComp.getX(), transformComp.getY());
			
			if(e.getComponent(MovementComponent.class) == null) {
				g.setFill(Color.RED);
			} else {
				g.setFill(Color.BLUE);
			}
			
			g.fillRect(0, 0, hitbox.getWidth(), hitbox.getHeight());
			
			g.setFill(Color.WHITE);
			g.scale(2, 2);
			
			MovementComponent movementComp = e.getComponent(MovementComponent.class);
			
			if(movementComp != null) {
				g.fillText("E: " + e, 0, 0);
				g.fillText("E-Collide: " + collisionComp.getCollidingWithEntity().get(), 0, 10);
				if(collisionComp.isCollideX()) g.fillText("X-collide", 0, 20);
				if(collisionComp.isCollideY()) g.fillText("Y-collide", 0, 30);
			}
			
			g.scale(1d/2d, 1d/2d);
			
			g.strokeRect(hitbox.getX() - transformComp.getX(), hitbox.getY() - transformComp.getY(), hitbox.getWidth(), 
					hitbox.getHeight());
			
			g.translate(-transformComp.getX(), -transformComp.getY());
			g.rotate(-transformComp.getTheta());
			g.scale(1/transformComp.getScale(), 1/transformComp.getScale());
			
		}
		
	}
	
}
