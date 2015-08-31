package com.rawad.ballsimulator.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.rawad.ballsimulator.main.BallSimulator;
import com.rawad.ballsimulator.world.terrain.Terrain;
import com.rawad.ballsimulator.world.terrain.TerrainComponent;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;

public class Loader {
	
	public static final String TERRAIN_COMPONENT_ATTRIB_SPLIT = ",";
	
	private static final String RES_0 = BallSimulator.NAME + "/res/";
	
	private static final String TEXTURES_1 = "textures/";// _1 are general directories
	private static final String TERRAIN_1 = "terrains/";
	private static final String FILES_1 = "files/";
	
	public static final String ENTITY_2 = "entity/";// _2 should be used by other classes
	
	private static final String TXT_4 = ".txt";
	private static final String PNG_4 = ".png";
	
	public static BufferedReader readFile(String fileName) {
		return ResourceManager.readFile(RES_0 + FILES_1 + fileName + TXT_4);
	}
	
	public static void saveFile(String fileName, String content) {
		ResourceManager.saveFile(RES_0 + FILES_1 + fileName + TXT_4, content);
	}
	
	public static int loadTexture(String textureFolder, String textureName) {
		return ResourceManager.loadTexture(RES_0 + TEXTURES_1 + textureFolder + textureName + PNG_4);
	}
	
	public static void saveTerrain(Terrain terrain, String terrainName) {// TODO: Make this into a class that is a subclass of FileType
		
		String filePath = RES_0 + TERRAIN_1 + terrainName + TXT_4;
		
		TerrainComponent[] components = terrain.getTerrainComponents();
		String[] componentLines = new String[components.length];
		
		for(int i = 0; i < componentLines.length; i++) {
			componentLines[i] = components[i].toString();
		}
		
		String content = Util.getStringFromLines(componentLines, Util.NL, false);
		
		ResourceManager.saveFile(filePath, content);
		
	}
	
	public static Terrain loadTerrain(String terrainName) {
		Terrain re = new Terrain();
		
		String filePath = RES_0 + TERRAIN_1 + terrainName + TXT_4;
		
		BufferedReader reader = ResourceManager.readFile(filePath);
		
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
		}
		
		return re;
	}
	
	public static TerrainComponent getTerrainComponentFromLine(String[] lineComponents) {
		
		double x = Util.parseDouble(lineComponents[0]);
		double y = Util.parseDouble(lineComponents[1]);
		
		int width = Util.parseInt(lineComponents[2]);
		int height = Util.parseInt(lineComponents[3]);
		
		return new TerrainComponent(x, y, width, height);
		
	}
	
}
