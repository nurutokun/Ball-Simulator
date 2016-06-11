package com.rawad.ballsimulator.client.renderengine.components;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SelectionComponentRender extends ComponentRender<SelectionComponent> {
	
	@Override
	protected void render(GraphicsContext g, Entity e, SelectionComponent comp) {
		
		if(comp.isHighlighted()) {
			
			final double lineWidth = g.getLineWidth();
			g.setLineWidth(lineWidth * 2d);
			
			Rectangle hitbox = e.getComponent(CollisionComponent.class).getHitbox();
			
			g.setStroke(Color.AQUA);
			g.strokeRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
			
			g.setLineWidth(lineWidth);
			
		}
		
	}
	
	@Override
	public Class<SelectionComponent> getComponentType() {
		return SelectionComponent.class;
	}
	
}
