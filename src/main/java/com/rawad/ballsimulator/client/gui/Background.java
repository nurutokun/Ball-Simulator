package com.rawad.ballsimulator.client.gui;

import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.resources.TextureResource;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class Background {
	
	private static final String DEFAULT_TEXTURE_FILE = "game_background";
	
	private static double PIXELS_PER_TICK = 2.5;
	
	private static int DEFAULT_TEXTURE;
	private static int DEFAULT_FLIPPED_TEXTURE;
	
	private static Background instance;
	
	private int texture;
	private int flippedTexture;
	
	private double x;
	private double secondX;
	
	private double maxWidth;
	private double maxHeight;// Solely for rendering.
	
	private Background() {}
	
	public static void registerTextures(CustomLoader loader) {
		
		DEFAULT_TEXTURE = loader.registerTexture("", DEFAULT_TEXTURE_FILE);
		
		DEFAULT_FLIPPED_TEXTURE = loader.registerTexture("", DEFAULT_TEXTURE_FILE + " (flipped)");
		
		Background.instance().texture = DEFAULT_TEXTURE;
		Background.instance().flippedTexture = DEFAULT_FLIPPED_TEXTURE;
		
		final TextureResource flippedTexture = ResourceManager.getTextureObject(DEFAULT_FLIPPED_TEXTURE);
		
		ResourceManager.getTextureObject(DEFAULT_TEXTURE).setOnloadAction(textureResource -> {// In case they're not 
			// loaded in the order they're registered (which they are).
			
				flippedTexture.setTexture(getHorizontallyFlippedImage(textureResource.getTexture()));
				
				Background.instance().maxWidth = textureResource.getTexture().getWidth();
				Background.instance().maxHeight = textureResource.getTexture().getHeight();
				
				Background.instance().x = 0;
				Background.instance().secondX = -Background.instance().maxWidth;
			
		});
		
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
	
	public int getTexture() {
		return texture;
	}
	
	public int getFlippedTexture() {
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
				
				flipped.getPixelWriter().setArgb(i, j, original.getPixelReader().getArgb((int) original.getWidth() - i - 1, j));
				
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
