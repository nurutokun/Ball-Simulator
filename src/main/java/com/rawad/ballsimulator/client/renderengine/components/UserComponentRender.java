package com.rawad.ballsimulator.client.renderengine.components;

import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class UserComponentRender extends ComponentRender<UserComponent> {
	
	private static final double TEXT_SCALE = 2.5d;
	
	@Override
	protected void render(GraphicsContext g, Entity e, UserComponent comp) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
		
		final double height = renderingComp.getTexture().getHeight();
		
		final String username = comp.getUsername();
		
		BlendMode blendMode = g.getGlobalBlendMode();
		TextAlignment textAlign = g.getTextAlign();
		
		g.setGlobalBlendMode(BlendMode.SRC_OVER);
		g.setTextAlign(TextAlignment.CENTER);
		
		g.rotate(-transformComp.getTheta());
		g.translate(0, -height);
		g.scale(TEXT_SCALE, TEXT_SCALE);
		
		g.setStroke(Color.BLUE);
		g.strokeText(username, 0, 0);
		
		g.scale(1d / TEXT_SCALE, 1d / TEXT_SCALE);
		g.translate(0, height);
		g.rotate(transformComp.getTheta());
		
		g.setTextAlign(textAlign);
		g.setGlobalBlendMode(blendMode);
		
	}
	
	@Override
	public Class<UserComponent> getComponentType() {
		return UserComponent.class;
	}
	
}
