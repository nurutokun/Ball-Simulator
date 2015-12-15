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
	
	public TerrainFileParser() {
		
	}

	@Override
	protected void parseLine(String line) {
		
		String[] tokens = line.split(REGEX);
		
		double x = Util.parseDouble(tokens[INDEX_X]);
		double y = Util.parseDouble(tokens[INDEX_Y]);
		
		int width = Util.parseInt(tokens[INDEX_WIDTH]);
		int height = Util.parseInt(tokens[INDEX_HEIGHT]);
		
		terrain.addTerrainComponent(x, y, width, height);
		
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
	
//	@Override
//	protected void handleData(String key, String value) {
//		
//		String[] data = value.split(DATA_SPLIT);
//		
//		double x = Util.parseDouble(data[0]);
//		double y = Util.parseDouble(data[1]);
//		
//		int width = Util.parseInt(data[2]);
//		int height = Util.parseInt(data[3]);
//		
//		addComponent(new TerrainComponent(x, y, width, height));
//		
//	}
	
//	/**
//	 * Overriden so that the components can be added in a specific order.
//	 * 
//	 */
//	@Override
//	public String getContent() {
//		
//		String[] lines = new String[terrainComponents.size()];
//		
//		for(int i = 0; i < lines.length; i++) {
//			lines[i] = i + SPLIT + getLine(terrainComponents.get(i));
//		}
//		
//		return Util.getStringFromLines(lines, Util.NL, false);
//	}
//	
//	public TerrainComponent[] getComponents() {
//		return terrainComponents.toArray(new TerrainComponent[terrainComponents.size()]);
//	}
//	
//	public void setComponents(TerrainComponent[] components) {
//		terrainComponents.clear();// Don't create new object, and don't convert array to list, addComponent method will.
//		
//		for(int i = 0; i < components.length; i++) {
//			
//			TerrainComponent component = components[i];
//			
//			addComponent(component);
//			
//		}
//		
//	}
//	
//	public void addComponent(TerrainComponent component) {
//		
//		String line = getLine(component);
//		
//		terrainComponents.add(component);
//		
//		data.put(terrainComponents.size() + "", line);
//		
//	}
//	
//	private String getLine(TerrainComponent component) {
//		return component.getX() + DATA_SPLIT + component.getY() + DATA_SPLIT + component.getWidth() +
//				DATA_SPLIT + component.getHeight();
//	}
	
}
