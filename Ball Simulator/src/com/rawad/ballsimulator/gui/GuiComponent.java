package com.rawad.ballsimulator.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.ballsimulator.input.MouseInput;

public abstract class GuiComponent {
	
	protected String id;
	
	protected BufferedImage background;
	protected BufferedImage foreground;
	
	protected int x;
	protected int y;
	
	protected int width;
	protected int height;
	
	private int prevMouseX;
	private int prevMouseY;
	
	private boolean hovered;
	private boolean pressed;
	
	public GuiComponent(String id, BufferedImage background, BufferedImage foreground, int x, int y) {
		
		this.id = id;
		
		this.background = background;
		this.foreground = foreground;
		
		this.x = x;
		this.y = y;
		
		this.width = background.getWidth();
		this.height = background.getHeight();
		
		hovered = false;
		
	}
	
	public GuiComponent(String id, Color backgroundColor, Color foregroundColor, int x, int y, int width, int height) {
		this(id, getMonotoneImage(backgroundColor, width, height), getMonotoneImage(foregroundColor, width, height), x, y);
	}
	
	/**
	 * Update position and/or highlighted-status and/or typing cursor etc.
	 * 
	 * @param x X-coordinate of mouse position
	 * @param y Y-coordinate of mouse position
	 */
	public void update(int x, int y) {
		
		if(intersects(x, y)) {
			
			if(!hovered) {
				mouseEntered();
				
				hovered = true;
				
			}
			
			if(MouseInput.isButtonDown(MouseInput.LEFT_MOUSE_BUTTON)) {
				
				if(!pressed) {// if not already pressed
					mousePressed();
					
				}
				
				pressed = true;
				
			} else {
				
				if(pressed) {
					mouseReleased();
					
					if(intersects(prevMouseX, prevMouseY)) {
						System.out.println(getId() + " wuz clicked at coords: " + prevMouseX + ", " + prevMouseY);
						mouseClicked();
					}
					
				}
				
				pressed = false;
				
			}
			
//			hovered = true;
			
		} else {
			
			if(hovered) {
				mouseExited();
				
				hovered = false;
			}
			
			pressed = false;
			
		}
		
		prevMouseX = x;
		prevMouseY = y;
		
	}
	
	public abstract void render(Graphics2D g);
	
	protected abstract void mouseClicked();
	
	protected abstract void mousePressed();
	
	protected abstract void mouseReleased();
	
	protected abstract void mouseEntered();
	
	protected abstract void mouseExited();
	
	public boolean intersects(int x, int y) {
		
		if( x >= this.x && x <= this.x + width &&
			y >= this.y && y <= this.y + height) {
			
			return true;
		}
		
		return false;
	}
	
	public String getId() {
		return id;
	}
	
	public BufferedImage getBackground() {
		return background;
	}
	
	public void setBackground(BufferedImage background) {
		this.background = background;
	}
	
	public void setBackground(Color background) {
		this.background = getMonotoneImage(background, width, height);
	}
	
	public BufferedImage getForeground() {
		return foreground;
	}
	
	public void setForeground(Color foreground) {
		this.foreground = getMonotoneImage(foreground, width, height);
	}
	
	private static BufferedImage getMonotoneImage(Color color, int width, int height) {
		
		BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < temp.getWidth(); i++) {
			for(int j = 0; j < temp.getHeight(); j++) {
				
				temp.setRGB(i, j, color.getRGB());
				
			}
		}
		
		return temp;
		
	}
	
}
