package com.rawad.ballsimulator.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.rawad.ballsimulator.log.Logger;
import com.rawad.ballsimulator.main.BallSimulator;

public abstract class GuiComponent {
	
	protected static final String BASE_FOLDER_PATH = "res/";
	protected static final String TEXTURE_FILE_EXTENSION = ".png";
	
	protected String id;
	
	protected BufferedImage background;
	protected BufferedImage foreground;
	
	protected Rectangle hitbox;// For hit-detection with mouse; dynamic
	
	// Used for drawing, exclusively
	protected int x;
	protected int y;
	
	protected int width;
	protected int height;
	//
	
	private int prevMouseX;
	private int prevMouseY;
	
	protected boolean mouseDragged;
	
	private boolean hovered;
	private boolean pressed;
	
	public GuiComponent(String id, BufferedImage background, BufferedImage foreground, int x, int y) {
		
		this.id = id;
		
		this.background = background;
		this.foreground = foreground;
		
		this.width = background.getWidth();
		this.height = background.getHeight();
		
		this.x = x - (this.width/2);
		this.y = y - (this.height/2);
		
		this.hitbox = new Rectangle(this.x, this.y, width, height);
		
		hovered = false;
		
	}
	
	public GuiComponent(String id, Color backgroundColor, Color foregroundColor, int x, int y, int width, int height) {
		this(id, getMonotoneImage(backgroundColor, width, height), getMonotoneImage(foregroundColor, width, height), x, y);
	}
	
	public GuiComponent(String id, BufferedImage background, BufferedImage foreground, int x, int y, int width, int height) {
		this(id, getScaledImage(background, width, height), getScaledImage(foreground, width, height), x, y);
	}
	
	/**
	 * Update position and/or highlighted-status and/or typing cursor etc.
	 * 
	 * @param x X-coordinate of mouse position
	 * @param y Y-coordinate of mouse position
	 */
	public void update(int x, int y, boolean mouseButtonDown) {
		
		if(!intersects(x, y) && mouseButtonDown && prevMouseX != x && prevMouseY != y) {// Mouse dragged outside component
			mouseDragged = true;
		}
		
		if(intersects(x, y)) {
			
			if(mouseButtonDown && hovered) {
				
				if(!pressed && !mouseDragged) {// If not already pressed
					mousePressed();
					
				}
				
				pressed = true;
				
			} else {
				
				if(pressed) {
					mouseReleased();
					
					if(intersects(prevMouseX, prevMouseY) && !mouseDragged) {
						mouseClicked();
						
					}
					
					mouseDragged = false;
					pressed = false;
					
				}
				
				
			}
			
			if(!hovered) {
				mouseEntered();
				
				hovered = true;
				
			}
			
		} else {
			
			if(hovered) {
				mouseExited();
				
				hovered = false;
			}
			
			if(!mouseButtonDown) {
				mouseDragged = false;
			}
			
			pressed = false;
			
		}
		
		prevMouseX = x;
		prevMouseY = y;
		
	}
	
	public void render(Graphics2D g) {
		
		if(BallSimulator.instance().isDebug()) {
			g.setColor(Color.BLACK);
			g.drawRect(hitbox.x - 1, hitbox.y - 1, hitbox.width + 1, hitbox.height + 1);
		}
		
	}
	
	protected void mouseClicked() {}
	
	protected void mousePressed() {}
	
	protected void mouseReleased() {}
	
	protected void mouseEntered() {}
	
	protected void mouseExited() {}
	
	public boolean intersects(int x, int y) {
		
		return hitbox.intersects(x, y, 1, 1);
		
//		if( x >= this.x && x <= this.x + width &&
//			y >= this.y && y <= this.y + height) {
//			
//			return true;
//		}
//		
//		return false;
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
	
	protected static BufferedImage getScaledImage(BufferedImage original, int width, int height) {
		
		Image scaled = original.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		
		return toBufferedImage(scaled);
		
	}
	
	private static BufferedImage toBufferedImage(Image image) {
		
		if(image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		
		BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = bImage.createGraphics();
		
		bGr.drawImage(image, 0, 0, null);
		bGr.dispose();
		
		return bImage;
		
	}
	
	protected static BufferedImage getMonotoneImage(Color color, int width, int height) {
		
		BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < temp.getWidth(); i++) {
			for(int j = 0; j < temp.getHeight(); j++) {
				
				temp.setRGB(i, j, color.getRGB());
				
			}
		}
		
		return temp;
		
	}
	
	protected static BufferedImage[] loadTextures(String textureFolder, String[] textureFileNames) {
		
		BufferedImage[] loadedImages = new BufferedImage[textureFileNames.length];
		
		for(int i = 0; i < loadedImages.length; i++) {
			
			BufferedImage temp = null;
			
			try {
				
				temp = ImageIO.read(new File(BASE_FOLDER_PATH + textureFolder + textureFileNames[i] + TEXTURE_FILE_EXTENSION));
				
			} catch(Exception ex) {
				
				Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; couldn't load image file.");
				
			} finally {
				loadedImages[i] = temp;
			}
			
		}
		
		return loadedImages;
	}
	
}
