package com.rawad.ballsimulator.client.renderengine.components;

import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SelectionComponentRender extends ComponentRender<SelectionComponent> {
	
	@Override
	protected void render(GraphicsContext g, Entity e, SelectionComponent comp) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
		
		final double width = renderingComp.getTexture().getWidth() * transformComp.getScaleX();
		final double height = renderingComp.getTexture().getHeight() * transformComp.getScaleY();
		
		if(comp.isHighlighted()) {
			
			final double lineWidth = g.getLineWidth();
			g.setLineWidth(lineWidth * 5d);
			
			g.setStroke(Color.AQUA);
			g.strokeRect(0, 0, width, height);
			
			g.setLineWidth(lineWidth);
			
		}
		
	}
	
	@Override
	public Class<SelectionComponent> getComponentType() {
		return SelectionComponent.class;
	}
	
}
