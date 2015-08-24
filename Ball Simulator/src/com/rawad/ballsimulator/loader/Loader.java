package com.rawad.ballsimulator.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.rawad.ballsimulator.world.terrain.Terrain;
import com.rawad.ballsimulator.world.terrain.TerrainComponent;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;

public class Loader {
	
	public static final String TERRAIN_COMPONENT_ATTRIB_SPLIT = ",";
	
	private static final String RES_0 = "res/";
	
//	private static final String TEXTURES_1 = "textures/";
	
	private static final String TERRAIN_1 = "terrains/";
	
	private static final String TXT_3 = ".txt";
	
	public static void saveTerrain(Terrain terrain, String terrainName) {
		
		String filePath = RES_0 + TERRAIN_1 + terrainName + TXT_3;
		
		File f = new File(filePath);
		
		try (	PrintWriter writer = new PrintWriter(new FileWriter(f));
				) {
			
			f.createNewFile();
			
			TerrainComponent[] components = terrain.getTerrainComponents();
			
			for(TerrainComponent component: components) {
				
				/*
				double x = component.getX();
				double y = component.getY();
				
				int width = component.getWidth();
				int height = component.getHeight();
				
				writer.write(x + TERRAIN_COMPONENT_ATTRIB_SPLIT);
				writer.write(y + TERRAIN_COMPONENT_ATTRIB_SPLIT);
				writer.write(width + TERRAIN_COMPONENT_ATTRIB_SPLIT);
				writer.write(height + "");
				writer.println();*/
				
				writer.write(component.toString());
				writer.println();
				
			}
			
		} catch(Exception ex) {
			Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; couldn't open terrain file, \"" + filePath + "\", to save to.");
		}
		
	}
	
	public static Terrain loadTerrain(String terrainName) {
		Terrain re = new Terrain();
		
		ResourceManager.loadTexture("");
		
		String filePath = RES_0 + TERRAIN_1 + terrainName + TXT_3;
		
		File f = new File(filePath);
		
		try {
			f.createNewFile();
		} catch(Exception ex) {
			// Ignore for now
		}
		
		try (	BufferedReader reader = new BufferedReader(new FileReader(f));
				) {
			
			ArrayList<String> lines = new ArrayList<String>();
			
			while(reader.ready()) {
				lines.add(reader.readLine());
			}
			
			for(String line: lines) {
				
				String[] components = line.split(TERRAIN_COMPONENT_ATTRIB_SPLIT);
				
				re.addTerrainComponent(getTerrainComponentFromLine(components));
			}
			
		} catch(Exception ex) {
			Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; couldn't load terrain file \"" + filePath + "\".");
		}
		
		return re;
	}
	
	public static TerrainComponent getTerrainComponentFromLine(String[] lineComponents) {
		
		double x = parseDouble(lineComponents[0]);
		double y = parseDouble(lineComponents[1]);
		
		int width = parseInt(lineComponents[2]);
		int height = parseInt(lineComponents[3]);
		
		return new TerrainComponent(x, y, width, height);
		
	}
	
	private static int parseInt(String potentialInt) {
		
		try {
			return Integer.parseInt(potentialInt);
		} catch(Exception ex) {
			return 0;
		}
		
	}
	
	private static double parseDouble(String potentialDouble) {
		
		try {
			return Double.parseDouble(potentialDouble);
		} catch(Exception ex) {
			return 0.0d;
		}
		
	}
	
}
