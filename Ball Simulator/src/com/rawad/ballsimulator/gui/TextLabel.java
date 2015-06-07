package com.rawad.ballsimulator.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TextLabel extends GuiComponent {
	
	private static final BufferedImage[] DEFAULT_TEXTURES;
	
	private static final String[] TEXTURE_PATHS = {"default_background"};
	
	private static final String SKIN_PATH = "label_skins/";
	
	private static final int BACKGROUND_INDEX = 0;
	
	public TextLabel(String text, BufferedImage background, BufferedImage foreground, int x, int y) {
		super(text, background, foreground, x, y);
		
	}
	
	public TextLabel(String text, Color backgroundColor, int x, int y, int width, int height) {
		super(text, backgroundColor, new Color(0xFFFFFF), x, y, width, height);
		
	}
	
	public TextLabel(String text, int x, int y) {
		this(text, DEFAULT_TEXTURES[BACKGROUND_INDEX], getMonotoneImage(new Color(0xFFFFFF),
				DEFAULT_TEXTURES[BACKGROUND_INDEX].getWidth(), DEFAULT_TEXTURES[BACKGROUND_INDEX].getHeight()), x, y);
		
	}
	
	public TextLabel(String text, int x, int y, int width, int height) {
		this(text, getScaledImage(DEFAULT_TEXTURES[BACKGROUND_INDEX], width, height),
				getMonotoneImage(new Color(0xFFFFFF), width, height), x, y);
		
	}
	
	static {
		
		DEFAULT_TEXTURES = loadTextures(SKIN_PATH, TEXTURE_PATHS);
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		Font f = g.getFont();
		FontMetrics fm = g.getFontMetrics(f);
		
		int stringWidth = fm.stringWidth(id);
		int stringHeight = fm.getHeight();
		
		int stringX = x + (width/2) - (stringWidth/2);
		int stringY = y + (height/2) + (stringHeight/4);// Since Strings are drawn from baseline (bottom y)
		
		g.drawImage(getBackground(), x, y, null);
		
		g.setColor(Color.WHITE);
		g.drawString(getId(), stringX, stringY);
		
	}
	
}
