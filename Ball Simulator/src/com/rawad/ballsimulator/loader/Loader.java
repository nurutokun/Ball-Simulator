package com.rawad.ballsimulator.loader;

import java.io.BufferedReader;

import com.rawad.ballsimulator.files.SettingsLoader;
import com.rawad.ballsimulator.files.TerrainLoader;
import com.rawad.ballsimulator.main.BallSimulator;
import com.rawad.ballsimulator.world.terrain.Terrain;
import com.rawad.gamehelpers.files.FileParser;
import com.rawad.gamehelpers.files.FileType;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.resources.ResourceManager;

public class Loader {
	
	public static final String TERRAIN_COMPONENT_ATTRIB_SPLIT = ",";
	
	private static final String RES_0 = BallSimulator.NAME + "/res/";
	
	private static final String TEXTURES_1 = "textures/";// _1 are general directories
	private static final String TERRAIN_1 = "terrains/";
	private static final String FILES_1 = "files/";
	
	public static final String ENTITY_2 = "entity/";// _2 should be used by other classes
	
	private static final String TXT_4 = ".txt";
	private static final String PNG_4 = ".png";
	
	public static int loadTexture(String textureFolder, String textureName) {
		return ResourceManager.loadTexture(RES_0 + TEXTURES_1 + textureFolder + textureName + PNG_4);
	}
	
	public static void saveFile(FileType file, String fileName) {
		
		String filePath = RES_0 + FILES_1 + fileName + TXT_4;
		
		ResourceManager.saveFile(filePath, file.getContent());
		
	}
	
	public static void loadSettings(Game game, String settingsFile) {
		
		String filePath = RES_0 + FILES_1 + settingsFile + TXT_4;
		
		SettingsLoader settings = game.getFile(SettingsLoader.class);
		
		FileParser parser = game.getFileParser();
		
		parser.parseFile(settings, ResourceManager.readFile(filePath));
		
	}
	
	public static void saveTerrain(Game game, Terrain terrain, String terrainName) {
		
		String filePath = RES_0 + TERRAIN_1 + terrainName + TXT_4;
		
		TerrainLoader terrainLoader = game.getFile(TerrainLoader.class);
		
		terrainLoader.setComponents(terrain.getTerrainComponents());
		
		ResourceManager.saveFile(filePath, terrainLoader.getContent());// TODO change dis. filePath
		
	}
	
	public static Terrain loadTerrain(Game game, String terrainName) {
		Terrain re = new Terrain();
		
		String filePath = RES_0 + TERRAIN_1 + terrainName + TXT_4;
		
		BufferedReader reader = ResourceManager.readFile(filePath);
		
		TerrainLoader terrainLoader = game.getFile(TerrainLoader.class);
		FileParser parser = game.getFileParser();
		
		parser.parseFile(terrainLoader, reader);
		
		re.setTerrainComponents(terrainLoader.getComponents());
		
		/*/
		ArrayList<String> lines = new ArrayList<String>();
		
		try {
			
			while(reader.ready()) {
				lines.add(reader.readLine());
			}
			
			for(String line: lines) {
				
				String[] components = line.split(TERRAIN_COMPONENT_ATTRIB_SPLIT);
				
				re.addTerrainComponent(getTerrainComponentFromLine(components));
			}
			
		} catch(IOException ex) {
			Logger.log(Logger.WARNING, "Couldn't read terrain: " + terrainName);
			ex.printStackTrace();
		}/**/
		
		return re;
	}
	
}
