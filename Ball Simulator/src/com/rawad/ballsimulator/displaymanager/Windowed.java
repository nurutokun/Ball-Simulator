package com.rawad.ballsimulator.displaymanager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.rawad.ballsimulator.input.EventHandler;
import com.rawad.ballsimulator.input.KeyboardInput;
import com.rawad.ballsimulator.main.BallSimulator;

public class Windowed extends DisplayMode {
	
	private JFrame frame;
	private JPanel panel;
	
	private EventHandler l;
	
	public Windowed() {
		
		l = new EventHandler();
		
	}
	
	@Override
	public void create() {
		
		frame = new JFrame(BallSimulator.NAME);
		panel = new JPanel() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7964464010671011714L;
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				BufferedImage buffer = new BufferedImage(DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight(),
						BufferedImage.TYPE_INT_ARGB);
				
				Graphics2D g2 = buffer.createGraphics();
				
				g2.setColor(DisplayManager.DEFAULT_BACKGROUND_COLOR);
				g2.fillRect(0, 0, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
				
				BallSimulator.instance().render(g2);
				
				g.drawImage(buffer.getScaledInstance(DisplayManager.getDisplayWidth(), DisplayManager.getDisplayHeight(),
						BufferedImage.SCALE_FAST), 0, 0, null);
				
				buffer = null;
				
				g.dispose();
				g2.dispose();
			}
			
		};
		
		panel.setPreferredSize(new Dimension(DisplayManager.getDisplayWidth(), DisplayManager.getDisplayHeight()));
		
		frame.add(panel, BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.addKeyListener(l);
		
		panel.addMouseListener(l);
		panel.addMouseMotionListener(l);
		panel.addMouseWheelListener(l);
		// Don't actually need this; width/height are set constants; the image is scaled over and over to fit the screen properly instead
		panel.addComponentListener(l);
		
		frame.pack();
		frame.setVisible(true);
		
	}
	
	@Override
	public void destroy() {
		
		frame.dispose();
		
		frame = null;
		panel = null;
		
	}
	
	@Override
	public synchronized void repaint() {
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_F11)) {
			DisplayManager.setDisplayMode(DisplayManager.Mode.FULLSCREEN);
			KeyboardInput.setKeyDown(KeyEvent.VK_F11, false);
			return;
		}
		
		panel.repaint();
		
	}
	
}
