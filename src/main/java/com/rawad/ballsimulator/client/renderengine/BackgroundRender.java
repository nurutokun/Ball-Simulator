package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.client.gui.Background;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.client.renderengine.LayerRender;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.jfxengine.gui.GuiRegister;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

public class BackgroundRender extends LayerRender {
	
	private UserViewComponent userView;
	
	public BackgroundRender(Entity camera) {
		userView = camera.getComponent(UserViewComponent.class);
	}
	
	@Override
	public void render() {
		
		GraphicsContext g = GuiRegister.getRoot(masterRender.getState()).getCanvas().getGraphicsContext2D();
		
		Affine transform = g.getTransform();
		
		Background background = Background.instance();
		
		double x = background.getX();
		double secondX = background.getSecondX();
		
		double scaleX = userView.getViewport().getWidth() / background.getMaxWidth();
		double scaleY = userView.getViewport().getHeight() / background.getMaxHeight();
		
		g.scale(scaleX, scaleY);
		
		g.drawImage(background.getTexture(), x, 0);
		g.drawImage(background.getFlippedTexture(), secondX, 0);
		
		g.setTransform(transform);
		
	}
	
}
