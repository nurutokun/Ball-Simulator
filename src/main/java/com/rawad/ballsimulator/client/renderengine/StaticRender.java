package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.gamehelpers.client.renderengine.LayeredRender;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class StaticRender extends LayeredRender {
	
	@Override
	public void render(GraphicsContext g, Entity e) {
		
		CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
		
		if(collisionComp != null) {
			
			Rectangle hitbox = collisionComp.getHitbox();
			
			g.setStroke(Color.RED);
			g.strokeRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
			
		}
		
	}
	
}
