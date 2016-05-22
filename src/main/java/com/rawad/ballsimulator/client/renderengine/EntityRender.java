package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.HealthComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.renderengine.Render;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class EntityRender extends Render {
	
	public void render(GraphicsContext g, Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
		
		Image texture = renderingComp.getTexture();
		
		g.translate(transformComp.getX() + (texture.getWidth() / 2d), transformComp.getY() + (texture.getHeight() / 2d));
		g.rotate(transformComp.getTheta());
		
		g.drawImage(texture, -texture.getWidth() / 2d, -texture.getHeight() / 2d, texture.getWidth() * 
				transformComp.getScaleX(), texture.getHeight() * transformComp.getScaleY());
		
		g.rotate(-transformComp.getTheta());
		g.translate(-texture.getWidth() / 2d, -texture.getHeight() / 2d);
		
		renderHealthComponent(g, e.getComponent(HealthComponent.class), texture.getWidth());
		
		g.translate(-transformComp.getX(), -transformComp.getY());
		
		renderCollisionComponent(g, e.getComponent(CollisionComponent.class));
		
	}
	
	private void renderCollisionComponent(GraphicsContext g, CollisionComponent collisionComp) {
		
		if(collisionComp == null) return;
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		if(GameManager.instance().getCurrentGame().isDebug()) {
			g.setStroke(Color.BLACK);
			g.strokeRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		}
	}
	
	private void renderHealthComponent(GraphicsContext g, HealthComponent healthComp, double width) {
		
		if(healthComp == null) return;
		
		final double barHeight = 10d;
		
		g.translate(0, -barHeight);
		
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, width, barHeight);
		
		final double barWidth = width * (healthComp.getHealth() / healthComp.getMaxHealth());
		final double insets = 6d;
		
		g.setFill(Color.RED);
		g.fillRect(insets / 2d, insets / 2d, barWidth - insets, barHeight - insets);
		
		g.translate(0, barHeight);
		
	}
	
}
