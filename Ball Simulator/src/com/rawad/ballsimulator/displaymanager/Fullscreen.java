package com.rawad.ballsimulator.displaymanager;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.rawad.ballsimulator.input.EventHandler;
import com.rawad.ballsimulator.input.KeyboardInput;
import com.rawad.ballsimulator.log.Logger;
import com.rawad.ballsimulator.main.BallSimulator;
import com.sun.glass.events.KeyEvent;

public class Fullscreen extends com.rawad.ballsimulator.displaymanager.DisplayMode {
	
	private DisplayMode[] displayModes = {
			
			// Makes it so that the dimensions can be easily changed by changing them from the DisplayManager class
			new DisplayMode(DisplayManager.getWidth(), DisplayManager.getHeight(), 32, DisplayManager.REFRESH_RATE),
			new DisplayMode(DisplayManager.getWidth(), DisplayManager.getHeight(), 24, DisplayManager.REFRESH_RATE),
			new DisplayMode(DisplayManager.getWidth(), DisplayManager.getHeight(), 16, DisplayManager.REFRESH_RATE),
			new DisplayMode(DisplayManager.getWidth(), DisplayManager.getHeight(), 32, DisplayMode.REFRESH_RATE_UNKNOWN),
			new DisplayMode(DisplayManager.getWidth(), DisplayManager.getHeight(), 24, DisplayMode.REFRESH_RATE_UNKNOWN),
			new DisplayMode(DisplayManager.getWidth(), DisplayManager.getHeight(), 16, DisplayMode.REFRESH_RATE_UNKNOWN),
			
	};
	
	private GraphicsDevice currentDevice;
	
	private Window window;
	
	private EventHandler l;
	
	public Fullscreen() {
		l = new EventHandler();
	}
	
	@Override
	public void create() {
		
		currentDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		try {
			
			DisplayMode compatibleMode = findCompatibleMode(displayModes);
			
			setFullScreen(compatibleMode);
			
			DisplayManager.setWidth(compatibleMode.getWidth());
			DisplayManager.setHeight(compatibleMode.getHeight());
			
			window = currentDevice.getFullScreenWindow();
			
			window.setBackground(Color.BLUE);
			
		} catch(Exception ex) {
			
			Logger.log(Logger.SEVERE, "Abruptly exited full screen mode...");
			
		}
		
	}
	
	@Override
	public void destroy() {
		
		exitFullScreen();
		
	}
	
	@Override
	public void repaint() {
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_C)) {
			DisplayManager.setDisplayMode(DisplayManager.Mode.WINDOWED);
			KeyboardInput.setKeyDown(KeyEvent.VK_C, false);
			return;
		}
		
		BufferStrategy bufStrat = window.getBufferStrategy();
		
		Graphics2D g = (Graphics2D) bufStrat.getDrawGraphics();
		
		g.setColor(DisplayManager.DEFAULT_BACKGROUND_COLOR);
		g.fillRect(0, 0, window.getWidth(), window.getHeight());
		
		g.setColor(Color.BLACK);
		
		if(!bufStrat.contentsLost()) {	
			
			BallSimulator.instance().render(g);
			bufStrat.show();
			
		}
		
		g.dispose();
		
	}
	
	private DisplayMode findCompatibleMode(DisplayMode[] displayModes) {
		
		DisplayMode[] goodModes = currentDevice.getDisplayModes();
		
		for(int i = 0; i < goodModes.length; i++) {
			for(int j = 0; j < displayModes.length; j++) {
				
				if(displayModesMatch(goodModes[i], displayModes[j])) {
					return goodModes[i];
				}
				
			}
		}
		
		return null;
		
	}
	
	private boolean displayModesMatch(DisplayMode m1, DisplayMode m2) {
		
		if(m1.getWidth() != m1.getWidth() || m1.getHeight() != m2.getHeight()) {
			
			return false;
		}
		
		if(	m1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
			&& m2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
			&& m1.getBitDepth() != m2.getBitDepth()) {
			
			return false;
		}
		
		if(	m1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN &&
			m2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN &&
			m1.getRefreshRate() != m2.getRefreshRate()) {
			
			return false;
		}

		return true;
		
	}
	
	private void setFullScreen(DisplayMode dm) {
		
		JFrame f = new JFrame();
		
		f.setTitle(BallSimulator.NAME);
		
		f.setUndecorated(true);
		f.setIgnoreRepaint(true);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.addKeyListener(l);
		f.addMouseListener(l);
		f.addMouseMotionListener(l);
		f.addMouseWheelListener(l);
		
		currentDevice.setFullScreenWindow(f);
		
		if (dm != null && currentDevice.isDisplayChangeSupported()) {
			
			currentDevice.setDisplayMode(dm);
			
		}
		
		f.createBufferStrategy(2);
	}
	
	private void exitFullScreen() {
		
		currentDevice.setFullScreenWindow(null);
		
		window.dispose();
		
		window = null;
		currentDevice = null;
		
	}
	
}
