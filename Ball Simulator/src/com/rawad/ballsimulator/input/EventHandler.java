package com.rawad.ballsimulator.input;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.rawad.ballsimulator.displaymanager.DisplayManager;

public class EventHandler implements MouseMotionListener, MouseListener, MouseWheelListener,
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
	public void mouseClicked(MouseEvent e) {
		
		if(e.isAltDown()) {
			MouseInput.setButtonClicked(MouseInput.MIDDLE_MOUSE_BUTTON, true);
			
		} else if(e.isMetaDown()) {
			MouseInput.setButtonClicked(MouseInput.RIGHT_MOUSE_BUTTON, true);
			
		} else {
			MouseInput.setButtonClicked(MouseInput.LEFT_MOUSE_BUTTON, true);
			
		}
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		if(e.isAltDown()) {
			MouseInput.setButtonDown(MouseInput.MIDDLE_MOUSE_BUTTON, true);
		
		} else if(e.isMetaDown()) {
			MouseInput.setButtonDown(MouseInput.RIGHT_MOUSE_BUTTON, true);
		
		} else {
			MouseInput.setButtonDown(MouseInput.LEFT_MOUSE_BUTTON, true);
		
		}
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		if(e.isAltDown()) {
			MouseInput.setButtonDown(MouseInput.MIDDLE_MOUSE_BUTTON, false);
			
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
		
		double xScale = (double) DisplayManager.getWidth()/(double) e.getComponent().getWidth();
		double yScale = (double) DisplayManager.getHeight()/(double) e.getComponent().getHeight();
		
		int newX = (int) (e.getX() * xScale);
		int newY = (int) (e.getY() * yScale);
		
		MouseInput.setX(newX);
		MouseInput.setY(newY);
		
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {}
	
	@Override
	public void componentMoved(ComponentEvent e) {}
	
	@Override
	public void componentResized(ComponentEvent e) {
		
		//setWidth(e.getComponent().getWidth());
		//setHeight(e.getComponent().getHeight());
		
	}
	
	@Override
	public void componentShown(ComponentEvent e) {}
	
}
