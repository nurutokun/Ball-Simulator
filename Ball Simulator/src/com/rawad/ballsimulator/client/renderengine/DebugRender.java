package com.rawad.ballsimulator.client.renderengine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.renderengine.LayeredRender;
import com.rawad.gamehelpers.renderengine.text.TextRender;
import com.rawad.gamehelpers.utils.Util;
import com.rawad.gamehelpers.utils.strings.RenderedString;

public class DebugRender extends LayeredRender {
	
	private TextRender textRender;
	
	private Camera camera;
	
	private RenderedString debugInfo;
	
	private boolean show;
	
	public DebugRender() {
		
		textRender = TextRender.instance();
		
		debugInfo = new RenderedString("");
		debugInfo.setFont(Font.getFont(Font.SERIF));
		debugInfo.setColor(Color.WHITE);
		debugInfo.setSize(12);
		
		show = false;
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		if(show == false) return;
		
		int screenWidth = Game.SCREEN_WIDTH;
		int screenHeight = Game.SCREEN_HEIGHT;
		
		boolean useOldRendering = GameManager.instance().shouldUseOldRendering();
		
		debugInfo.setContent(DisplayManager.getDisplayWidth() + ", " + DisplayManager.getDisplayHeight() + " | "
					+ GameManager.instance().getFPS() + " | " + GameManager.instance().getDeltaTime() + Util.NL
					+ MouseInput.getX() + ", " + MouseInput.getY() + Util.NL
					+ "Rendering: " + (useOldRendering? "Inherited Rendering":"MCV Rendering") + Util.NL
					+ Runtime.getRuntime().freeMemory() + Util.NL
					+ (camera == null? "":"CamScale: " + camera.getXScale() + ", " + camera.getYScale()));
		
		textRender.render(g, debugInfo, new Rectangle(0, 0, screenWidth, screenHeight));
		
		g.setColor(Color.GREEN);
		g.fillOval(screenWidth - 50, screenHeight - 50, 50, 50);
		
		g.setColor(Color.RED);
		g.fillRect(MouseInput.getX(), MouseInput.getY(), 1, 1);
		
		camera = null;
		
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void setShow(boolean show) {
		this.show = show;
	}
	
}
