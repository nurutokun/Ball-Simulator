package com.rawad.ballsimulator.client.renderengine.components;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CollisionComponentRender extends ComponentRender<CollisionComponent> {
	
	@Override
	public void render(GraphicsContext g, Entity e, CollisionComponent comp) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		
		if(GameManager.instance().getCurrentGame().isDebug()) {
			
			Rectangle hitbox = comp.getHitbox();
			
			g.setStroke(Color.BLACK);
			g.strokeRect(hitbox.getX() - transformComp.getX(), hitbox.getY() - transformComp.getY(), hitbox.getWidth(), 
					hitbox.getHeight());
			
		}
		
	}
	
	@Override
	public Class<CollisionComponent> getComponentType() {
		return CollisionComponent.class;
	}
	
}
