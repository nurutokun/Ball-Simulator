package com.rawad.ballsimulator.networking.server.main;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.gui.Background;
import com.rawad.gamehelpers.input.EventHandler;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.log.Logger;

public class ViewportShell extends JPanel {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -2677329891767235880L;
	
	private Viewport viewport;
	
	/**
	 * If true, world scale is 1:1 and manual movement will be activated; false and the world will be scaled to the JPanel's dimensions.
	 */
	private boolean freeRoam;
	
	public ViewportShell(Viewport viewport) {
		super();
		
		this.viewport = viewport;
		
		setFocusable(true);
		
		addKeyListener(EventHandler.instance());
		
		setIgnoreRepaint(true);
		
	}
	
	public void update(long timePassed) {
		
		int dx = getDelta(KeyEvent.VK_D, KeyEvent.VK_A) | getDelta(KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT);
		int dy = getDelta(KeyEvent.VK_S, KeyEvent.VK_W) | getDelta(KeyEvent.VK_DOWN, KeyEvent.VK_UP);
		
		Camera camera = viewport.getCamera();
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_C, true)) {
			Logger.log(Logger.DEBUG, "Pressed C");
			freeRoam = !freeRoam;
			
		}
		
		scaleCamera(camera, viewport.getWorld(), getWidth(), getHeight());
		
		Background.instance().update(timePassed);
		
		viewport.update(timePassed, dx, dy);
		
	}
	
	private int getDelta(int keyPositive, int keyNegative) {
		
		int delta = 0;
		
		if(KeyboardInput.isKeyDown(keyPositive, false)) {
			delta = 5;
		}
		
		if(KeyboardInput.isKeyDown(keyNegative, false)) {
			delta = -5;
		}
		
		return delta;
	}
	
	private void scaleCamera(Camera camera, World world, int width, int height) {
		
		double xScale = 1;
		double yScale = 1;
		
		if(!freeRoam) {
			xScale = (double) width / (double) world.getWidth();
			yScale = (double) height / (double) world.getHeight();
		}
		
		camera.setScale(xScale, yScale);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		viewport.render(g, getWidth(), getHeight());
		
		g.dispose();
		
	}
	
	public Viewport getViewport() {
		return viewport;
	}
	
	public void setFreeRoam(boolean freeRoam) {
		this.freeRoam = freeRoam;
	}
	
	public boolean isFreeRoam() {
		return freeRoam;
	}
	
}
