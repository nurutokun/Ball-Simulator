package com.rawad.ballsimulator.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.rawad.ballsimulator.log.Logger;

public class Button extends GuiComponent {
	
	private static final BufferedImage DEFAULT_BACKGROUND;
	private static final BufferedImage DEFAULT_FOREGROUND;
	private static final BufferedImage DEFAULT_ONCLICK;
	
	private static final String FILE_EXTENSION = ".png";
	
	private static final String BASE_FILE_PATH = "res/button_skins/";
	
	private static final String BACKGROUND_PATH = "default_background";
	private static final String FOREGROUND_PATH = "default_foreground";
	private static final String ONCLICK_PATH = "default_onclick";
	
	private BufferedImage onclick;
	
	private boolean clicked;
	private boolean highlighted;
	private boolean pressed;
	
	public Button(String text, BufferedImage background, BufferedImage foreground, BufferedImage onclick, int x, int y) {
		super(text, background, foreground, x, y);
		
		this.onclick = onclick;
		
		this.x = x - (width/2);
		this.y = y - (height/2);
		
	}
	
	public Button(String text, BufferedImage background, BufferedImage foreground, BufferedImage onclick,
			int x, int y, int width, int height) {
		
		this(text, getScaledImage(background, width, height), getScaledImage(foreground, width, height),
				getScaledImage(onclick, width, height), x, y);
	}
	
	public Button(String text, int x, int y) {
		this(text, DEFAULT_BACKGROUND, DEFAULT_FOREGROUND, DEFAULT_ONCLICK, x, y);	
	}
	
	static {
		
		BufferedImage temp0 = null;
		BufferedImage temp1 = null;
		BufferedImage temp2 = null;
		
		try {
			
			temp0 = ImageIO.read(new File(BASE_FILE_PATH + BACKGROUND_PATH + FILE_EXTENSION));
			temp1 = ImageIO.read(new File(BASE_FILE_PATH + FOREGROUND_PATH + FILE_EXTENSION));
			temp2 = ImageIO.read(new File(BASE_FILE_PATH + ONCLICK_PATH + FILE_EXTENSION));
			
		} catch(Exception ex) {
			
			Logger.log(Logger.WARNING, ex.getMessage() + "; couldn't load images");
			
		} finally {
			DEFAULT_BACKGROUND = temp0;
			DEFAULT_FOREGROUND = temp1;
			DEFAULT_ONCLICK = temp2;
			
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		g.drawImage(background, x, y, null);
		
		if(highlighted) {
			g.drawImage(foreground, x, y, null);
		}
		
		if(pressed) {
			g.drawImage(onclick, x, y, null);
		}
			
		Font f = g.getFont();
		FontMetrics fm = g.getFontMetrics(f);
		
		int stringWidth = fm.stringWidth(id);
		int stringHeight = fm.getHeight();
		
		int stringX = x + (width/2) - (stringWidth/2);
		int stringY = y + (height/2) + (stringHeight/4);// Since Strings are drawn from baseline (bottom y)
		
		g.setColor(Color.BLACK);
		g.drawString(id, stringX, stringY);
		
	}
	
	private static BufferedImage getScaledImage(BufferedImage original, int width, int height) {
		
		Image scaled = original.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		
		return toBufferedImage(scaled);
		
	}
	
	private static BufferedImage toBufferedImage(Image image) {
		
		if(image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		
		BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = bimage.createGraphics();
		
		bGr.drawImage(image, 0, 0, null);
		bGr.dispose();
		
		return bimage;
		
	}
	
	@Override
	protected void mouseClicked() {
		pressed = false;
		clicked = true;
		
	}
	
	@Override
	protected void mousePressed() {
		pressed = true;
		
	}
	
	@Override
	protected void mouseReleased() {
		pressed = false;
	}
	
	@Override
	protected void mouseEntered() {
		highlighted = true;
		
	}
	
	@Override
	protected void mouseExited() {
		highlighted = false;
		pressed = false;
	}
	
	public boolean isClicked() {
		return clicked;
	}
	
	public void setClicked(boolean clicked) {
		this.clicked = clicked;	
	}
	
}
