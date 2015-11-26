package com.rawad.ballsimulator.client.renderengine;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.renderengine.Render;

public class DebugRender extends Render {
	
	private Camera camera;
	
	private boolean show;
	
	public DebugRender() {
		
		show = false;
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		if(show == false) return;
		
		int screenWidth = Game.SCREEN_WIDTH;
		int screenHeight = Game.SCREEN_HEIGHT;
		
		g.setColor(Color.GREEN);
		g.fillOval(screenWidth - 50, screenHeight - 50, 50, 50);
		
		g.setColor(Color.WHITE);
		g.drawString(DisplayManager.getDisplayWidth() + ", " + DisplayManager.getDisplayHeight() + " | " +
			GameManager.instance().getFPS() + " | " + GameManager.instance().getDeltaTime(), 10, 10);
		
		g.drawString(MouseInput.getX() + ", " + MouseInput.getY(), 10, 20);

		boolean useOldRendering = GameManager.instance().shouldUseOldRendering();
		
		g.drawString("Rendering: " + (useOldRendering? "Inherited Rendering":"MCV Rendering"), 10, 30);
		
		g.setColor(Color.RED);
		g.fillRect(MouseInput.getX(), MouseInput.getY(), 1, 1);
		
		g.setColor(Color.WHITE);
		g.drawString(Runtime.getRuntime().freeMemory() + "", 10, 40);
		
		if(camera != null) {
			
			g.setColor(Color.WHITE);
			g.drawString("CamScale: " + camera.getXScale() + ", " + camera.getYScale(), 10, 50);
			
		}
		
		camera = null;
		
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void setShow(boolean show) {
		this.show = show;
	}
	
}
