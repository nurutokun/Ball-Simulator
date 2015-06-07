package com.rawad.ballsimulator.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class DropDown extends GuiComponent {
	
	private static final BufferedImage[] DEFAULT_TEXTURES;
	
	private static final String[] TEXTURE_PATHS = {"default_background"};
	
	private static final String SKIN_PATH = "dropdown_skins/";
	
	private static final int BACKGROUND_INDEX = 0;
	
	public DropDown(String id, BufferedImage background, BufferedImage foreground, int x, int y) {
		super(id, background, foreground, x, y);
		
	}
	
	static {
		
		DEFAULT_TEXTURES = loadTextures(SKIN_PATH, TEXTURE_PATHS);
		
	}

	public DropDown(String id, Color backgroundColor, Color foregroundColor,
			int x, int y, int width, int height) {
		super(id, backgroundColor, foregroundColor, x, y, width, height);
	}

	public DropDown(String id, BufferedImage background,
			BufferedImage foreground, int x, int y, int width, int height) {
		super(id, background, foreground, x, y, width, height);
	}
	
	public DropDown(String id, int x, int y) {
		super(id, DEFAULT_TEXTURES[BACKGROUND_INDEX], getMonotoneImage(new Color(0xFFFFFF),
				DEFAULT_TEXTURES[BACKGROUND_INDEX].getWidth(), DEFAULT_TEXTURES[BACKGROUND_INDEX].getHeight()), x, y);
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		g.drawImage(getBackground(), x, y, null);
		
		
	}
	
}
