package com.rawad.ballsimulator.main;

import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.game.Game;

public class BallSimulator extends Game {
	
	/**
	 * @Temporary
	 */
	public static final String NAME = "Ball Simulator";
	
	private static int ICON;
	
	private CustomLoader loader;
	
	public BallSimulator() {
		super();
		
		loader = new CustomLoader();
		
		loaders.put(CustomLoader.class, loader);
		
	}
	
	@Override
	public void registerTextures() {
		
		ICON = loader.registerTexture("", "game_icon");
		
	}
	
	@Override
	protected void init() {
		super.init();
		
		fileParsers.put(TerrainFileParser.class, new TerrainFileParser());
		fileParsers.put(SettingsFileParser.class, new SettingsFileParser());
		
	}
	
	@Override
	public int getIconLocation() {
		return ICON;
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
