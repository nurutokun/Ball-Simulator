package com.rawad.ballsimulator.fileparser;

import java.util.ArrayList;

import com.rawad.ballsimulator.world.terrain.Terrain;
import com.rawad.ballsimulator.world.terrain.TerrainComponent;
import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.utils.Util;

public class TerrainFileParser extends FileParser {
	
	private static final String REGEX = ",";
	
	private static final int INDEX_X = 0;
	private static final int INDEX_Y = 1;
	private static final int INDEX_WIDTH = 2;
	private static final int INDEX_HEIGHT = 3;
	
	private Terrain terrain;
	
	@Override
	protected void parseLine(String line) {
		
		String[] tokens = line.split(REGEX);
		
		double x = Util.parseDouble(tokens[INDEX_X]);
		double y = Util.parseDouble(tokens[INDEX_Y]);
		
		double width = Util.parseDouble(tokens[INDEX_WIDTH]);
		double height = Util.parseDouble(tokens[INDEX_HEIGHT]);
		
		terrain.addTerrainComponent(new TerrainComponent(x, y, width, height));
		
	}
	
	@Override
	protected void start() {
		super.start();
		
		terrain = new Terrain();
		
	}
	
	@Override
	public String getContent() {
		
		ArrayList<TerrainComponent> terrainComponents = terrain.getTerrainComponents();
		
		String[] lines = new String[terrainComponents.size()];
		
		int i = 0;
		
		for(TerrainComponent comp: terrainComponents) {
			
			lines[i] = comp.getX() + REGEX + comp.getY() + REGEX + comp.getWidth() + REGEX + comp.getHeight();
			
			i++;
		}
		
		return Util.getStringFromLines(lines, Util.NL, false);
		
	}
	
	public Terrain getTerrain() {
		return terrain;
	}
	
	public void setTerrain(Terrain terrain) {
		
		this.terrain.setTerrainComponents(terrain.getTerrainComponents());
		
	}
	
}
