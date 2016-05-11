package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.client.gui.Background;
import com.rawad.gamehelpers.client.renderengine.Render;
import com.rawad.gamehelpers.resources.ResourceManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Affine;

public class BackgroundRender extends Render {
	
	public void render(GraphicsContext g, double width, double height) {
		
		Background background = Background.instance();
		
		double x = background.getX();
		double secondX = background.getSecondX();
		
		Image texture = ResourceManager.getTexture(background.getTexture());
		Image flippedTexture = ResourceManager.getTexture(background.getFlippedTexture());
		
		if(texture == null || flippedTexture == null) return;
		
		Affine affine = g.getTransform();
		
		double scaleX = width / background.getMaxWidth();
		double scaleY = height / background.getMaxHeight();
		
		g.scale(scaleX, scaleY);
		
		g.drawImage(texture, x, 0);
		g.drawImage(flippedTexture, secondX, 0);
		
		g.setTransform(affine);
		
	}
	
}
