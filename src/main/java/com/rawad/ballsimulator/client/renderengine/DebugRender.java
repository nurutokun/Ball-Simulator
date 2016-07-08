package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.client.renderengine.LayerRender;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.utils.Util;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DebugRender extends LayerRender {
	
	private AClient client;
	
	private TransformComponent cameraTransform;
	private UserViewComponent userView;
	
	public DebugRender(AClient client, Entity camera) {
		
		this.client = client;
		
		cameraTransform = camera.getComponent(TransformComponent.class);
		userView = camera.getComponent(UserViewComponent.class);
		
	}
	
	@Override
	public void render(GraphicsContext g) {
		
		if(GameManager.getCurrentGame().isDebug() == false) return;
		
		double screenWidth = userView.getViewport().getWidth();
		double screenHeight = userView.getViewport().getHeight();
		
		g.setFill(Color.WHITE);
		g.fillText(
				Util.NL + screenWidth + ", " + screenHeight + " | " + client.getAverageFps() + " |" 
						+ " " + GameManager.getTimePassed() + Util.NL + 
				Mouse.getX() + ", " + Mouse.getY() + Util.NL +
				Runtime.getRuntime().freeMemory() / 1E9 + " G of free memory" + Util.NL +
				"CamScale: " + cameraTransform.getScaleX() + ", " + cameraTransform.getScaleY() + Util.NL +
				"Cam (x,y): (" + cameraTransform.getX() + ", " + cameraTransform.getY() + ")" + Util.NL + 
				"Entities: " + client.getGame().getWorld().getEntities().size(), 0, 0);
		
		g.setFill(Color.LAWNGREEN);
		g.fillOval(screenWidth - 50, screenHeight - 50, 50, 50);
		
		g.setFill(Color.RED);
		g.fillRect(Mouse.getX(), Mouse.getY(), 1, 1);
		
	}
	
}
