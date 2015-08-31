package com.rawad.ballsimulator.client.gui;

import java.awt.Graphics2D;

import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.resources.ResourceManager;

public class CloseButton extends Button {
	
	private static final int BACKGROUND_LOCATION;
	private static final int FOREGROUND_LOCATION;
	private static final int ONCLICK_LOCATION;
	private static final int DISABLED_LOCATION;
	
	public CloseButton(int x, int y, int width,
			int height) {
		super("Close", "", x, y, width, height);
		
	}
	
	static {
		
		String base = ResourceManager.getString("Button.base");
		
		BACKGROUND_LOCATION = loadTexture(base, "close_background");
		FOREGROUND_LOCATION = loadTexture(base, "close_foreground");
		ONCLICK_LOCATION = loadTexture(base, "close_onclick");
		DISABLED_LOCATION = ResourceManager.loadTexture("");// TODO: Unknown for now...
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g, BACKGROUND_LOCATION, FOREGROUND_LOCATION, ONCLICK_LOCATION, DISABLED_LOCATION);
	}
	
}
