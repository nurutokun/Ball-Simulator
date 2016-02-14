package com.rawad.ballsimulator.main;

import java.awt.image.BufferedImage;

import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.resources.ResourceManager;

public class BallSimulator extends Game {
	
	/**
	 * @Temporary
	 */
	public static final String NAME = "Ball Simulator";
	
	private static int ICON;
	
	private CustomLoader loader;
	
	public BallSimulator() {
		super();
		
	}
	
	@Override
	protected void init(Proxy clientOrServer) {
		super.init(clientOrServer);

		loader = new CustomLoader();
		
		loaders.put(CustomLoader.class, loader);
		
		ICON = loader.loadTexture("", "game_icon");
		
		fileParsers.put(TerrainFileParser.class, new TerrainFileParser());
		fileParsers.put(SettingsFileParser.class, new SettingsFileParser());
		
		clientOrServer.init(this);
		
	}
	
	@Override
	public BufferedImage getIcon() {
		return ResourceManager.getTexture(ICON);
	}
	
	@Override
	public String toString() {
		return NAME;
	}
	
	@Override
	public String getSettingsFileName() {
		return "settings";
	}
	
}
