package com.rawad.ballsimulator.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.ballsimulator.input.MouseEvent;

public class Button extends GuiComponent {
	
	private static final BufferedImage[] DEFAULT_TEXTURES;
	
	private static final String[] TEXTURE_PATHS = {"default_background", "default_foreground", "default_onclick"};
	
	private static final String SKIN_PATH = "button_skins/";
	
	private static final int BACKGROUND_INDEX = 0;
	private static final int FOREGROUND_INDEX = 1;
	private static final int ONCLICK_INDEX = 2;
	
	private BufferedImage onclick;
	private BufferedImage disabled;
	
	protected boolean clicked;
	protected boolean highlighted;
	protected boolean pressed;
	protected boolean enabled;
	
	public Button(String text, BufferedImage background, BufferedImage foreground, BufferedImage onclick, int x, int y) {
		super(text, background, foreground, x, y);
		
		this.onclick = onclick;
		
		enabled = true;
		
	}
	
	public Button(String text, int x, int y) {
		this(text, DEFAULT_TEXTURES[BACKGROUND_INDEX], DEFAULT_TEXTURES[FOREGROUND_INDEX], DEFAULT_TEXTURES[ONCLICK_INDEX], x, y);	
	}
	
	static {
		
		DEFAULT_TEXTURES = loadTextures(SKIN_PATH, TEXTURE_PATHS);
		
	}
	
	@Override
	public void update(MouseEvent e) {
		super.update(e);
		
		if(!intersects(e.getX(), e.getY()) && !e.isButtonDown()) {
			mouseReleased();
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		Font f = g.getFont();
		FontMetrics fm = g.getFontMetrics(f);
		
		int stringWidth = fm.stringWidth(id);
		int stringHeight = fm.getHeight();
		
		int stringX = x + (width/2) - (stringWidth/2);
		int stringY = y + (height/2) + (stringHeight/4);// Since Strings are drawn from baseline (bottom y)
		
		drawButtonBase(g);
		drawText(g, stringX, stringY);
		
	}
	
	protected void drawButtonBase(Graphics2D g) {
		
		if(enabled) {
			
			g.drawImage(background, x, y, null);
			
			if(highlighted) {
				g.drawImage(foreground, x, y, null);
			}
			
			if(pressed) {
				g.drawImage(onclick, x, y, null);
			}
			
		} else {
			
			g.drawImage(disabled, x, y, null);
			
		}
		
	}
	
	protected void drawText(Graphics2D g, int stringX, int stringY) {
		
		g.setColor(Color.BLACK);
		g.drawString(id, stringX, stringY);
		
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
		
		if(mouseDragged && pressed) {
			mouseDragged = false;
		}
		
	}
	
	@Override
	protected void mouseExited() {
		highlighted = false;
	}
	
	public boolean isClicked() {
		return clicked;
	}
	
	public void setClicked(boolean clicked) {
		this.clicked = clicked;	
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public BufferedImage getOnClickTexture() {
		return onclick;
	}
	
	public BufferedImage getDisabledTexture() {
		return disabled;
	}
	
}
