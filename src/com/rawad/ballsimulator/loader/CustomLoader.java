package com.rawad.ballsimulator.loader;

import java.io.BufferedReader;

import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.main.BallSimulator;
import com.rawad.ballsimulator.world.terrain.Terrain;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.resources.ResourceManager;

public class CustomLoader extends Loader {
	
	private static final String MISC_FOLDER = "files";
	private static final String TERRAIN_FOLDER = "terrains";
	
	private static final String GUI_FOLDER = ResourceManager.getString("Gui.base");
	
	public CustomLoader() {
		super(BallSimulator.NAME);
		
	}
	
	public int loadGuiTexture(String subGuiFolder, String guiTextureFile) {
		return registerTexture(ResourceManager.getProperPath(GUI_FOLDER, subGuiFolder), guiTextureFile);
	}
	
	public void loadSettings(SettingsFileParser parser, String settingsFile) {
		
		BufferedReader reader = readFile(MISC_FOLDER, settingsFile);
		
		parser.parseFile(reader);
		
	}
	
	public void saveSettings(SettingsFileParser parser, String settingsFile) {
		
		saveFile(parser.getContent(), MISC_FOLDER, settingsFile);
		
	}
	
	public Terrain loadTerrain(TerrainFileParser parser, String terrainName) {
		
		BufferedReader reader = readFile(TERRAIN_FOLDER, terrainName);
		
		parser.parseFile(reader);
		
		return parser.getTerrain();
		
	}
	
	public void saveTerrain(TerrainFileParser parser, String terrainName) {
		
		saveFile(parser.getContent(), TERRAIN_FOLDER, terrainName);
		
	}
	
}