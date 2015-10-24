package com.rawad.ballsimulator.networking.server.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.input.EventHandler;

public class ViewportShell extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2677329891767235880L;
	
	private Viewport viewport;
	
	private double prevX;
	private double prevY;
	
	/**
	 * If true, world scale is 1:1 and manual movement will be activated; false and the world will be scaled to the JPanel's dimensions.
	 */
	private boolean freeRoam;
	
	private boolean coordsRecorded;
	private boolean cameraCoordsSet;
	
	public ViewportShell(Viewport viewport) {
		super();
		
		this.viewport = viewport;
		
		setFocusable(true);
		setRequestFocusEnabled(true);
		
		addKeyListener(new EventHandler());
		addMouseListener(new FocusKeeper());
		
		setIgnoreRepaint(true);
		
	}
	
	public void update(long timePassed, int dx, int dy) {
		
		if(!freeRoam) {
			dx = 0;
			dy = 0;
			
			Camera camera = viewport.getCamera();
			
			if(!coordsRecorded) {
				prevX = camera.getX();
				prevY = camera.getY();
				
				coordsRecorded = true;
				cameraCoordsSet = false;
			}
			
			viewport.getCamera().setX(0);
			viewport.getCamera().setY(0);
			
		} else {
			
			if(!cameraCoordsSet) {
				
				Camera camera = viewport.getCamera();
				
				camera.setX(prevX);
				camera.setY(prevY);
				
				cameraCoordsSet = true;
				coordsRecorded = false;
			}
			
		}
		
		viewport.update(timePassed, dx, dy);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		if(viewport != null) {
			scaleCamera(viewport.getCamera(), viewport.getWorld(), getWidth(), getHeight());
			
			try {
				viewport.render(g2);
			} catch(NullPointerException ex) {
				// For WindowBuilder parsing, game.isDebug() again...
			}
			
		}
		
		g.dispose();
	}
	
	private void scaleCamera(Camera camera, World world, int width, int height) {
		
		double xScale = 1;
		double yScale = 1;
		
		if(!freeRoam) {
			xScale = (double) width/(double) world.getWidth();
			yScale = (double) height/(double) world.getHeight();
		}
		
		camera.setScale(xScale, yScale);
		
	}
	
	private class FocusKeeper implements MouseListener {
		
		@Override
		public void mouseClicked(MouseEvent e) {}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// I do not like this.
			if(!ViewportShell.this.isFocusOwner()) {
				ViewportShell.this.requestFocusInWindow();
			}
			
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseExited(MouseEvent e) {}
		
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
