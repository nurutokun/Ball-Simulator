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
				
				BufferedImage buffer = new BufferedImage(DisplayManager.getWidth(), DisplayManager.getHeight(), BufferedImage.TYPE_INT_ARGB);
				
				Graphics2D g2 = buffer.createGraphics();
				
				g2.setColor(DisplayManager.DEFAULT_BACKGROUND_COLOR);
				g2.fillRect(0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());
				
				BallSimulator.instance().render(g2);
				
				g.drawImage(buffer.getScaledInstance(getWidth(), getHeight(), BufferedImage.SCALE_FAST), 0, 0, null);
				
				buffer = null;
				
				g.dispose();
				g2.dispose();
			}
			
		};
		
		panel.setPreferredSize(new Dimension(DisplayManager.getWidth(), DisplayManager.getHeight()));
		
		frame.add(panel, BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.addKeyListener(l);
		frame.addWindowStateListener(l);
		
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
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_C)) {
			DisplayManager.setDisplayMode(DisplayManager.Mode.FULLSCREEN);
			KeyboardInput.setKeyDown(KeyEvent.VK_C, false);
			return;
		}
		
		panel.repaint();
		
	}
	
	@Override
	public void update() {
		
		BallSimulator.instance().update(DisplayManager.getDeltaTime());
		
	}
	
}
