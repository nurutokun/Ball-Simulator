package com.rawad.ballsimulator.client.renderengine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.SwingConstants;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.gui.TextLabel;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.renderengine.LayeredRender;

public class DebugRender extends LayeredRender {
	
	private TextLabel debugInfo;
	
	private Camera camera;
	
	private boolean show;
	
	public DebugRender() {
		
		show = false;
		
	}
	
	public void initGUI() {
		
		debugInfo = new TextLabel("");
		debugInfo.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
		debugInfo.setDrawBackground(false);
		debugInfo.setForeground(Color.WHITE);
		debugInfo.setIgnoreRepaint(true);
		debugInfo.setBounds(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		debugInfo.setHorizontalAlignment(SwingConstants.LEFT);
		debugInfo.setVerticalAlignment(SwingConstants.TOP);
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		if(show == false) return;
		
		int screenWidth = Game.SCREEN_WIDTH;
		int screenHeight = Game.SCREEN_HEIGHT;
		
		boolean useOldRendering = GameManager.instance().shouldUseOldRendering();
		
		debugInfo.setText("<html>"
					+ DisplayManager.getDisplayWidth() + ", " + DisplayManager.getDisplayHeight() + " | "
					+ GameManager.instance().getFPS() + " | " + GameManager.instance().getDeltaTime() + "<br/>"
					+ MouseInput.getX(true) + ", " + MouseInput.getY(true) + "<br/>"
					+ "Rendering: " + (useOldRendering? "Inherited Rendering":"MCV Rendering") + "<br/>"
					+ Runtime.getRuntime().freeMemory() + "<br/>"
					+ (camera == null? "":"CamScale: " + camera.getXScale() + ", " + camera.getYScale()) + "<br/>"
					+ "</html>");
		debugInfo.paint(g);
		
		g.setColor(Color.GREEN);
		g.fillOval(screenWidth - 50, screenHeight - 50, 50, 50);
		
		g.setColor(Color.RED);
		g.fillRect(MouseInput.getX(true), MouseInput.getY(true), 1, 1);
		
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void setShow(boolean show) {
		this.show = show;
	}
	
}
