package com.rawad.ballsimulator.displaymanager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.rawad.ballsimulator.input.KeyboardInput;
import com.rawad.ballsimulator.input.MouseInput;
import com.rawad.ballsimulator.log.Logger;
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
				
				BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				
				Graphics2D g2 = buffer.createGraphics();
				
				BallSimulator.instance().render(g2);
				
				g.drawImage(buffer, 0, 0, getWidth(), getHeight(), 0, 0, buffer.getWidth(), buffer.getHeight(), null);
				
				g.dispose();
			}
			
		};
		
		frame.add(panel, BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(500, 500));
		
		setWidth(500);
		setHeight(500);
		
		frame.pack();
		frame.setVisible(true);
		
		frame.addKeyListener(l);
		
		panel.addMouseListener(l);
		panel.addMouseMotionListener(l);
		panel.addMouseWheelListener(l);
		panel.addComponentListener(l);
		
	}
	
	@Override
	public void destroy() {
		
		frame = null;
		panel = null;
		
	}
	
	private class EventHandler implements MouseMotionListener, MouseListener, MouseWheelListener,
											KeyListener, ComponentListener {
		
		@Override
		public void keyPressed(KeyEvent e) {
			KeyboardInput.setKeyDown(e.getKeyCode(), true);
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			KeyboardInput.setKeyDown(e.getKeyCode(), false);
		}
		
		@Override
		public void keyTyped(KeyEvent e) {}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			MouseInput.setMouseWheelPosition(e.getUnitsToScroll());
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseExited(MouseEvent e) {}
		
		@Override
		public void mousePressed(MouseEvent e) {
			
			if(e.isAltDown()) {
				
				Logger.log(Logger.DEBUG, "alt mouse pressed");
				
			} else if(e.isMetaDown()) {
				MouseInput.setButtonDown(MouseInput.RIGHT_MOUSE_BUTTON, true);
				
			} else {
				MouseInput.setButtonDown(MouseInput.LEFT_MOUSE_BUTTON, true);
				
			}
			
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			
			if(e.isAltDown()) {
				
				Logger.log(Logger.DEBUG, "alt mouse released");
				
			} else if(e.isMetaDown()) {
				MouseInput.setButtonDown(MouseInput.RIGHT_MOUSE_BUTTON, false);
				
			} else {
				MouseInput.setButtonDown(MouseInput.LEFT_MOUSE_BUTTON, false);
				
			}
			
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			mouseMoved(e);
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			
			MouseInput.setX(e.getX());
			MouseInput.setY(e.getY());
			
		}

		@Override
		public void componentHidden(ComponentEvent e) {}

		@Override
		public void componentMoved(ComponentEvent e) {}

		@Override
		public void componentResized(ComponentEvent e) {
			
			setWidth(e.getComponent().getWidth());
			setHeight(e.getComponent().getHeight());
			
		}

		@Override
		public void componentShown(ComponentEvent e) {}
		
	}

}
