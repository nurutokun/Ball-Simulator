package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.client.gui.Background;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.client.renderengine.LayerRender;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.resources.ResourceManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Affine;

public class BackgroundRender extends LayerRender {
	
	private UserViewComponent userView;
	
	public BackgroundRender(Entity camera) {
		userView = camera.getComponent(UserViewComponent.class);
	}
	
	@Override
	public void render(GraphicsContext g) {
		
		Background background = Background.instance();
		
		double x = background.getX();
		double secondX = background.getSecondX();
		
		Image texture = ResourceManager.getTexture(background.getTexture());
		Image flippedTexture = ResourceManager.getTexture(background.getFlippedTexture());
		
		Affine affine = g.getTransform();
		
		double scaleX = userView.getViewport().getWidth() / background.getMaxWidth();
		double scaleY = userView.getViewport().getHeight() / background.getMaxHeight();
		
		g.scale(scaleX, scaleY);
		
		g.drawImage(texture, x, 0);
		g.drawImage(flippedTexture, secondX, 0);
		
		g.setTransform(affine);
		
	}
	
}
