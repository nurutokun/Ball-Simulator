package com.rawad.ballsimulator.client.renderengine;

import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.client.renderengine.LayerRender;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DebugRender extends LayerRender {
	
	private TransformComponent cameraTransform;
	private UserViewComponent userView;
	
	private int mouseX;
	private int mouseY;
	
	private SimpleDoubleProperty displayWidth = new SimpleDoubleProperty();
	private SimpleDoubleProperty displayHeight = new SimpleDoubleProperty();
	
	public DebugRender(Entity camera) {
		
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
				"\n" + displayWidth.get() + ", " + displayHeight.get() + " | " + ((AClient) GameManager.instance()
						.getCurrentGame().getProxy()).getAverageFps() + " |" 
						+ " " + GameManager.instance().getTimePassed() + "\n" + 
				mouseX + ", " + mouseY + "\n" +
				Runtime.getRuntime().freeMemory() / 1E9 + " G of free memory" + "\n" +
				"CamScale: " + cameraTransform.getScaleX() + ", " + cameraTransform.getScaleY() + "\n" +
				"Cam (x,y): (" + cameraTransform.getX() + ", " + cameraTransform.getY() + ")", 0, 0);
		
		g.setFill(Color.LAWNGREEN);
		g.fillOval(screenWidth - 50, screenHeight - 50, 50, 50);
		
		g.setFill(Color.RED);
		g.fillRect(mouseX, mouseY, 1, 1);
		
	}
	
	/**
	 * @param mouseX the mouseX to set
	 */
	public void setMouseX(double mouseX) {
		this.mouseX = (int) mouseX;
	}
	
	/**
	 * @param mouseY the mouseY to set
	 */
	public void setMouseY(double mouseY) {
		this.mouseY = (int) mouseY;
	}
	
	/**
	 * @return the displayWidth
	 */
	public SimpleDoubleProperty widthProperty() {
		return displayWidth;
	}
	
	/**
	 * @return the displayHeight
	 */
	public SimpleDoubleProperty heightProperty() {
		return displayHeight;
	}
	
}
