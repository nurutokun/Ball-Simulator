package com.rawad.ballsimulator.game;

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
	
	public BallSimulator() {
		super();
	}
	
	@Override
	public void registerTextures() {
		
		ICON = loaders.get(CustomLoader.class).registerTexture("", "game_icon");
		
	}
	
	@Override
	protected void init() {
		super.init();
		
		loaders.put(CustomLoader.class, new CustomLoader());
		
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
	
}
