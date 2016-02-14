package com.rawad.ballsimulator.client.renderengine;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.input.Mouse;
import com.rawad.gamehelpers.renderengine.LayeredRender;

public class DebugRender extends LayeredRender {
	
	private Camera camera;
	
	public DebugRender() {
		
	}
	
	/*/
	public void initGUI() {	
		debugInfo = new TextLabel("");
		debugInfo.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
		debugInfo.setDrawBackground(false);
		debugInfo.setForeground(Color.WHITE);
		debugInfo.setIgnoreRepaint(true);
		debugInfo.setBounds(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		debugInfo.setHorizontalAlignment(SwingConstants.LEFT);
		debugInfo.setVerticalAlignment(SwingConstants.TOP);
	}/**/
	
	@Override
	public void render(Graphics2D g) {
		
		if(GameManager.instance().getCurrentGame().isDebug() == false) return;
		
		int screenWidth = Game.SCREEN_WIDTH;
		int screenHeight = Game.SCREEN_HEIGHT;
		
		/*/
		debugInfo.setText("<html>"
					+ DisplayManager.getDisplayWidth() + ", " + DisplayManager.getDisplayHeight() + " | "
					+ GameManager.instance().getFPS() + " | " + GameManager.instance().getDeltaTime() + "<br/>"
					+ Mouse.getX(true) + ", " + Mouse.getY(true) + "<br/>"
					+ Runtime.getRuntime().freeMemory() + "<br/>"
					+ (camera == null? "":"CamScale: " + camera.getXScale() + ", " + camera.getYScale() + "<br/>"
							+ "Cam (x,y): (" + camera.getX() + ", " + camera.getY() + ")") + "<br/>"
					+ "</html>");/**/
		
		g.setColor(Color.WHITE);
		drawLines(g, 0, 0,
				DisplayManager.getDisplayWidth() + ", " + DisplayManager.getDisplayHeight() + " | "
						+ GameManager.instance().getFPS() + " | " + GameManager.instance().getDeltaTime(),
						Mouse.getX(true) + ", " + Mouse.getY(true),
						Runtime.getRuntime().freeMemory() + "",
						(camera == null? "":"CamScale: " + camera.getXScale() + ", " + camera.getYScale()),
						(camera == null? "":"Cam (x,y): (" + camera.getX() + ", " + camera.getY() + ")"));
		
		g.setColor(Color.GREEN);
		g.fillOval(screenWidth - 50, screenHeight - 50, 50, 50);
		
		g.setColor(Color.RED);
		g.fillRect(Mouse.getX(true), Mouse.getY(true), 1, 1);
		
	}
	
	/**
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * 			- Upper bound of rectangle this text is to be drawn in.
	 * @param lines
	 */
	private void drawLines(Graphics2D g, int x, int y, String... lines) {
		
		for(String line: lines) {
			
			y += g.getFontMetrics().getHeight();
			
			g.drawString(line, x, y);
			
		}
		
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
}
