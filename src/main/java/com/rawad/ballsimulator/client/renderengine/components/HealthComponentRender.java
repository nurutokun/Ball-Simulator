package com.rawad.ballsimulator.client.renderengine.components;

import com.rawad.ballsimulator.entity.HealthComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HealthComponentRender extends ComponentRender<HealthComponent> {
	
	@Override
	protected void render(GraphicsContext g, Entity e, HealthComponent comp) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
		
		final double width = renderingComp.getTexture().getWidth() * transformComp.getScaleX();
		
		final double barHeight = 10d;
		
		g.translate(0, -barHeight);
		
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, width, barHeight);
		
		final double barWidth = width * (comp.getHealth() / comp.getMaxHealth());
		final double insets = 6d;
		
		g.setFill(Color.RED);
		g.fillRect(insets / 2d, insets / 2d, barWidth - insets, barHeight - insets);
		
		g.translate(0, barHeight);
		
	}
	
	@Override
	public Class<HealthComponent> getComponentType() {
		return HealthComponent.class;
	}
	
}
