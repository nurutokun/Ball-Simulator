package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.input.Mouse;
import com.rawad.gamehelpers.client.renderengine.LayerRender;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.entity.Entity;

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
		
		if(GameManager.instance().getCurrentGame().isDebug() == false) return;
		
		double screenWidth = userView.getViewport().getWidth();
		double screenHeight = userView.getViewport().getHeight();
		
		g.setFill(Color.WHITE);
		g.fillText(
				"\n" + screenWidth + ", " + screenHeight + " | " + client.getAverageFps() + " |" 
						+ " " + GameManager.instance().getTimePassed() + "\n" + 
				Mouse.getX() + ", " + Mouse.getY() + "\n" +
				Runtime.getRuntime().freeMemory() / 1E9 + " G of free memory" + "\n" +
				"CamScale: " + cameraTransform.getScaleX() + ", " + cameraTransform.getScaleY() + "\n" +
				"Cam (x,y): (" + cameraTransform.getX() + ", " + cameraTransform.getY() + ")", 0, 0);
		
		g.setFill(Color.LAWNGREEN);
		g.fillOval(screenWidth - 50, screenHeight - 50, 50, 50);
		
		g.setFill(Color.RED);
		g.fillRect(Mouse.getX(), Mouse.getY(), 1, 1);
		
	}
	
}
