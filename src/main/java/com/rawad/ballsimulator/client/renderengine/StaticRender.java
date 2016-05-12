package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.renderengine.LayeredRender;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class StaticRender extends LayeredRender {
	
	@Override
	public void render(GraphicsContext g, Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
		
		if(collisionComp != null) {
			
			Rectangle hitbox = collisionComp.getHitbox();
			
			g.scale(transformComp.getScale(), transformComp.getScale());
			g.rotate(transformComp.getTheta());
			g.translate(transformComp.getX(), transformComp.getY());
			
			if(e.getComponent(MovementComponent.class) != null) {
				g.setFill(Color.BLUE);
			} else {
				g.setFill(Color.RED);
			}
			
			g.fillRect(0, 0, hitbox.getWidth(), hitbox.getHeight());
			
			g.setFill(Color.WHITE);
			g.scale(2, 2);
			
//			if(e.getComponent(MovementComponent.class) != null) {
				g.fillText("E: " + e, 0, 0);
				g.fillText("E-Collide: " + collisionComp.getCollidingWithEntity().get(), 0, 10);
				if(collisionComp.isCollidingUp()) g.fillText("Up", 0, 20);
				if(collisionComp.isCollidingDown()) g.fillText("Down", 0, 30);
				if(collisionComp.isCollidingRight()) g.fillText("Right", 0, 40);
				if(collisionComp.isCollidingLeft()) g.fillText("Left", 0, 50);
//			}
			
			g.scale(1d/2d, 1d/2d);
			
			g.translate(-transformComp.getX(), -transformComp.getY());
			g.rotate(-transformComp.getTheta());
			g.scale(1/transformComp.getScale(), 1/transformComp.getScale());
			
		}
		
	}
	
}
