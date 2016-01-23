package com.rawad.ballsimulator.client.gui;

import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.ResourceManager;

public class CloseButton extends Button {
	
	public static final String NAME = "Close Button";
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 7931925570895858571L;
	
	private static final int BACKGROUND_LOCATION;
	private static final int FOREGROUND_LOCATION;
	private static final int ONCLICK_LOCATION;
	private static final int DISABLED_LOCATION;
	
	public CloseButton() {
		super("", "Close Button");
		
		setBackgroundTexture(BACKGROUND_LOCATION);
		setForegroundTexture(FOREGROUND_LOCATION);
		setOnclickTexture(ONCLICK_LOCATION);
		setDisabledTexture(DISABLED_LOCATION);
		
	}
	
	static {
		
		String base = ResourceManager.getString("Button.base");
		
		GameHelpersLoader loader = GameManager.instance().getCurrentGame().getLoader(GameHelpersLoader.BASE);
		
		BACKGROUND_LOCATION = loader.loadGuiTexture(base, "close_background");
		FOREGROUND_LOCATION = loader.loadGuiTexture(base, "close_foreground");
		ONCLICK_LOCATION = loader.loadGuiTexture(base, "close_onclick");
		DISABLED_LOCATION = ResourceManager.loadTexture("");// TODO: Unknown for now...
		
	}
	/*/
	@Override
	public void render(Graphics2D g) {
		super.render(g, BACKGROUND_LOCATION, FOREGROUND_LOCATION, ONCLICK_LOCATION, DISABLED_LOCATION);
	}/**/
	
}
