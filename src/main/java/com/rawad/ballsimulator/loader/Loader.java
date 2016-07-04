package com.rawad.ballsimulator.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.BallSimulator;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ALoader;
import com.rawad.gamehelpers.resources.ResourceManager;

public class Loader extends ALoader {
	
	public static final String FOLDER_TERRAIN = "terrains";
	
	private static final String FOLDER_MISC = "files";
	private static final String FOLDER_GUI = "gui";
	
	private static final String EXTENSION_LAYOUT_FILE = ".fxml";
	private static final String EXTENSION_STYLESHEET_FILE = ".css";
	
	public Loader() {
		super(BallSimulator.NAME);
	}
	
	public int loadGuiTexture(String subGuiFolder, String guiTextureFile) {
		return registerTexture(ResourceManager.getProperPath(FOLDER_GUI, subGuiFolder), guiTextureFile);
	}
	
	public void loadSettings(SettingsFileParser parser, String settingsFile) {
		
		BufferedReader reader = readFile(FOLDER_MISC, settingsFile);
		
		try {
			parser.parseFile(reader);
		} catch(IOException ex) {
			Logger.log(Logger.WARNING, "Couldn't parse settings file.");
			ex.printStackTrace();
		}
		
	}
	
	public void saveSettings(SettingsFileParser parser, String settingsFile) {
		
		saveFile(parser.getContent(), FOLDER_MISC, settingsFile);
		
	}
	
	public void loadTerrain(TerrainFileParser parser, World world, String terrainName) {
		
		BufferedReader reader = readFile(FOLDER_TERRAIN, terrainName);
		
		parser.setWorld(world);
		
		try {
			parser.parseFile(reader);
		} catch(IOException ex) {
			Logger.log(Logger.WARNING, "Couldn't parse terrain file.");
			ex.printStackTrace();
		}
		
	}
	
	public void saveTerrain(TerrainFileParser parser, String terrainName) {
		
		saveFile(parser.getContent(), FOLDER_TERRAIN, terrainName);
		
	}
	
	/**
	 * Assumes the fxml file is in the same package as the the {@code clazz} given. <b>Note:</b> can be in completely 
	 * different src file.
	 * 
	 * @param clazz
	 * @param fileName
	 * @return
	 */
	public static URL getFxmlLocation(Class<? extends Object> clazz, String fileName) {
		return clazz.getResource(fileName + EXTENSION_LAYOUT_FILE);
	}
	
	/**
	 * Assumes the name of the fxml file is that of the given {@code clazz}.
	 * 
	 * @param clazz
	 * @return
	 */
	public static URL getFxmlLocation(Class<? extends Object> clazz) {
		return getFxmlLocation(clazz, clazz.getSimpleName());
	}
	
	public static String getStyleSheetLocation(Class<? extends Object> clazz, String styleSheetName) {
		return clazz.getResource(styleSheetName + EXTENSION_STYLESHEET_FILE).toExternalForm();
	}
	
}
