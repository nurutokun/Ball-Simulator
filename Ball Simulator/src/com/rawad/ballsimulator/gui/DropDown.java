package com.rawad.ballsimulator.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.ballsimulator.input.MouseEvent;

public class DropDown extends Button {
	
	private static final BufferedImage[] DEFAULT_TEXTURES;
	
	private static final String[] TEXTURE_PATHS = {"default_background", "default_foreground", "default_onclick"};
	
	private static final String SKIN_PATH = "dropdown_skins/";
	
	private static final int BACKGROUND_INDEX = 0;
	private static final int FOREGROUND_INDEX = 1;
	private static final int ONCLICK_INDEX = 2;
	
	private Menu menu;
	
	private String[] options;
	
	private String currentSelected;
	
	private int widthSection;
	
	private boolean menuDown;
	
	public DropDown(String id, BufferedImage background, BufferedImage foreground, BufferedImage onclick,
			int x, int y, String defaultOption, String... options) {
		super(id, background, foreground, onclick, x, y);
		
		this.options = options;
		
		widthSection = width/3;// Use a third of the total bar as the arrow clicking spot
		
		hitbox.setBounds(this.x + (2*widthSection), this.y, widthSection, height);
		
		this.menu = new Menu(this.options, this.x + 5, this.y + height, widthSection * 2);
		currentSelected = defaultOption;
		
	}
	
	public DropDown(String id, BufferedImage background,BufferedImage foreground, BufferedImage onclick,
			int x, int y, int width, int height, String defaultOption, String... options) {
		this(id, getScaledImage(background, width, height), getScaledImage(foreground, width, height), 
				getScaledImage(onclick, width, height), x, y, defaultOption, options);
		
	}
	
	public DropDown(String id, BufferedImage background,
			BufferedImage foreground, BufferedImage onclick, int x, int y, int width, int height, String... options) {
		this(id, background, foreground, onclick, x, y, width, height, options[0], options);
		
	}
	
	public DropDown(String id, int x, int y, String... options) {
		this(id, DEFAULT_TEXTURES[BACKGROUND_INDEX], DEFAULT_TEXTURES[FOREGROUND_INDEX],
				DEFAULT_TEXTURES[ONCLICK_INDEX], x, y, options[0], options);
		
	}
	
	public DropDown(String id, int x, int y, int width, int height, String defaultOption, String... options) {
		this(id, DEFAULT_TEXTURES[BACKGROUND_INDEX], DEFAULT_TEXTURES[FOREGROUND_INDEX],
				DEFAULT_TEXTURES[ONCLICK_INDEX], x, y, width, height, defaultOption, options);
	}
	
	public DropDown(String id, int x, int y, int width, int height, String... options) {
		this(id, x, y, width, height, options[0], options);
	}
	
	static {
		
		DEFAULT_TEXTURES = loadTextures(SKIN_PATH, TEXTURE_PATHS);
		
	}
	
	@Override
	public void update(MouseEvent e) {
		super.update(e);
		
		int x = e.getX();
		int y = e.getY();
		
		boolean mouseButtonDown = e.isButtonDown();
		
		if(isMenuDown()) {
			hitbox.setBounds(menu.getX(), menu.getY(), menu.getWidth(), menu.getHeight());
			
			if(highlighted) {
				menu.mouseHover(y);
			}
			
			// Or could set highlighted false.
			
		} else {
			hitbox.setBounds(this.x + (2*widthSection), this.y, widthSection, height);
		}
		
		if(!intersects(x, y) && !mouseDragged && mouseButtonDown) {
			setMenuDown(false);
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		if(isMenuDown()) {
			menu.render(g);
		}
		
	}
	
	@Override
	protected void drawText(Graphics2D g, int stringX, int stringY) {
		
		Font f = g.getFont();
		FontMetrics fm = g.getFontMetrics(f);
		
		String text = id + ":";
		
		int stringWidth = fm.stringWidth(text);
		
		int widthSection = 2*(width/3);// Use only two thirds of the bar; .
		
		stringX = x + ((widthSection - stringWidth)/2);
		
		g.setColor(Color.WHITE);
		g.drawString(text, stringX, stringY - fm.getHeight());
		
		stringWidth = fm.stringWidth(currentSelected);
		
		stringX = x + ((widthSection - stringWidth)/2);
		
		g.drawString(currentSelected, stringX, stringY);
		
	}
	
	@Override
	public void mouseExited() {
		super.mouseExited();
		
		menu.mouseExited();
		
	}
	
	public void calculateSelectedItem(int y) {
		
		currentSelected = menu.getItemAt(y);
		
	}
	
	public String getCurrentSelectedItem() {
		return currentSelected;
	}
	
	public void setMenuDown(boolean menuDown) {
		this.menuDown = menuDown;
	}
	
	public boolean isMenuDown() {
		return menuDown;
	}
	
	private class Menu {
		
		private static final int STRING_HEIGHT = 20;
		
		private String[] items;
		
		private int x;
		private int y;
		
		private int width;
		private int height;
		
		private int highlighted;
		
		private boolean hovered;
		
		public Menu(String[] items, int x, int y, int width) {
			this.items = items;
			
			this.x = x;
			this.y = y - (STRING_HEIGHT);
			
			this.width = width;
			this.height = items.length * STRING_HEIGHT;// each item is 20 pixels
			
		}
		
		public void render(Graphics2D g) {
			
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x + 1, y + 1, width - 1, height - 1);
			
			g.setColor(Color.GRAY);
			
			if(hovered) {
				g.fillRect(x + 1, y + highlighted * STRING_HEIGHT + 1, width - 1, STRING_HEIGHT - 1);
			}
			
			g.setColor(Color.BLACK);
			g.drawRect(x, y, width, height);
			
			for(int i = 0; i < items.length; i++) {
				g.drawString(items[i], x + 5, y + (height/items.length * i) + STRING_HEIGHT - 5);
			}
			
		}
		
		public void mouseHover(int y) {
			
			highlighted = (y - this.y)/STRING_HEIGHT;
			hovered = true;
			
		}
		
		public void mouseExited() {
			
			hovered = false;
			
		}
		
		public String getItemAt(int y) {
			
			int yIndex = y - this.y;
			
			return items[yIndex/STRING_HEIGHT];
			
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public int getWidth() {
			return width;
		}
		
		public int getHeight() {
			return height;
		}
		
	}
	
}
