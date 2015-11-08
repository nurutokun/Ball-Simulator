package com.rawad.ballsimulator.client.renderengine;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.renderengine.Render;

public class DebugRender extends Render {
	
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
		
	}
	
	public void setShow(boolean show) {
		this.show = show;
	}
	
}
