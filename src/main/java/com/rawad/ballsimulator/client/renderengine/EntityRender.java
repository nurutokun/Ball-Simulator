package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.entity.CollisionComponent;
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
		CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
		RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
		
		if(collisionComp != null) {
			
			Rectangle hitbox = collisionComp.getHitbox();
			
			g.translate(transformComp.getX() + (hitbox.getWidth() / 2d), transformComp.getY() + (hitbox.getHeight() / 2d));
			g.rotate(transformComp.getTheta());
			
			Image texture = renderingComp.getTexture();
			
			g.drawImage(texture, -hitbox.getWidth() / 2d, -hitbox.getHeight() / 2d, texture.getWidth() * 
					transformComp.getScaleX(), texture.getHeight() * transformComp.getScaleY());
			
			g.rotate(-transformComp.getTheta());
			g.translate(-hitbox.getWidth() / 2d, -hitbox.getHeight() / 2d);
			
			g.setFill(Color.WHITE);
			g.scale(2d, 2d);
			
			g.scale(1d / 2d, 1d / 2d);
			
			g.translate(-transformComp.getX(), -transformComp.getY());
			
			if(GameManager.instance().getCurrentGame().isDebug()) 
				g.strokeRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
			
		}
		
	}
	
}
