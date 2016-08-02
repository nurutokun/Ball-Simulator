package com.rawad.ballsimulator.client.gui;

import com.rawad.ballsimulator.loader.Loader;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class Background {
	
	private static final String DEFAULT_TEXTURE_FILE = "game_background";
	
	private static double PIXELS_PER_TICK = 2.5;
	
	private static Image DEFAULT_TEXTURE;
	private static Image DEFAULT_FLIPPED_TEXTURE;
	
	private static Background instance;
	
	private Image texture;
	private Image flippedTexture;
	
	private double x;
	private double secondX;
	
	private double maxWidth;
	private double maxHeight;// Solely for rendering.
	
	private Background() {}
	
	public static void loadTextures(Loader loader) {
		
		DEFAULT_TEXTURE = loader.loadTexture("", DEFAULT_TEXTURE_FILE);
		
		DEFAULT_FLIPPED_TEXTURE = getHorizontallyFlippedImage(DEFAULT_TEXTURE);
		
		Background.instance().texture = DEFAULT_TEXTURE;
		Background.instance().flippedTexture = DEFAULT_FLIPPED_TEXTURE;
		
		Background.instance().maxWidth = DEFAULT_TEXTURE.getWidth();
		Background.instance().maxHeight = DEFAULT_TEXTURE.getHeight();
		
		Background.instance().x = 0;
		Background.instance().secondX = -Background.instance().maxWidth;
		
	}
	
	public void tick() {
		
		double delta = PIXELS_PER_TICK;
		
		x += delta;
		secondX += delta;
		
		if(x >= maxWidth) {
			x = 0 - maxWidth;
		}
		
		if(secondX > maxWidth) {
			secondX = x - maxWidth;
		}
		
	}
	
	public double getX() {
		return x;
	}
	
	public double getSecondX() {
		return secondX;
	}
	
	public Image getTexture() {
		return texture;
	}
	
	public Image getFlippedTexture() {
		return flippedTexture;
	}
	
	public double getMaxWidth() {
		return maxWidth;
	}
	
	public double getMaxHeight() {
		return maxHeight;
	}
	
	public static Image getHorizontallyFlippedImage(Image original) {
		
		WritableImage flipped = new WritableImage((int) original.getWidth(), (int) original.getHeight());
		
		for(int i = 0; i < flipped.getWidth(); i++) {
			for(int j = 0; j < flipped.getHeight(); j++) {
				
				flipped.getPixelWriter().setArgb(i, j, original.getPixelReader().getArgb((int) original.getWidth() - i 
						- 1, j));
				
			}
		}
		
		return flipped;
		
	}
	
	public static Background instance() {
		
		if(instance == null) {
			instance = new Background();
		}
		
		return instance;
		
	}
	
}
