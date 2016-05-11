package com.rawad.ballsimulator.client.renderengine;

import com.rawad.gamehelpers.client.renderengine.Camera;
import com.rawad.gamehelpers.client.renderengine.Render;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameManager;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DebugRender extends Render {
	
	private int mouseX;
	private int mouseY;
	
	private SimpleDoubleProperty displayWidth = new SimpleDoubleProperty();
	private SimpleDoubleProperty displayHeight = new SimpleDoubleProperty();
	
	public void render(GraphicsContext g, Camera camera) {
		
		if(GameManager.instance().getCurrentGame().isDebug() == false) return;
		
		double screenWidth = Game.SCREEN_WIDTH * camera.getScaleX();
		double screenHeight = Game.SCREEN_HEIGHT * camera.getScaleY();
		
		g.setFill(Color.WHITE);
		g.fillText(
				"\n" + displayWidth.get() + ", " + displayHeight.get() + " | " + GameManager.instance().getFPS() + " | " 
						+ GameManager.instance().getDeltaTime() + "\n" + 
				mouseX + ", " + mouseY + "\n" +
				Runtime.getRuntime().freeMemory() / 1E9 + " G of free memory" + "\n" +
				(camera == null? "":"CamScale: " + camera.getScaleX() + ", " + camera.getScaleY()) + "\n" +
				(camera == null? "":"Cam (x,y): (" + camera.getX() + ", " + camera.getY() + ")"), 0, 0);
		
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
