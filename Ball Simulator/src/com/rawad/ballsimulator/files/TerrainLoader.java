package com.rawad.ballsimulator.files;

import java.util.ArrayList;

import com.rawad.ballsimulator.world.terrain.TerrainComponent;
import com.rawad.gamehelpers.files.FileType;
import com.rawad.gamehelpers.utils.Util;

public class TerrainLoader extends FileType {
	
	private static final String DATA_SPLIT = ",";
	
	private ArrayList<TerrainComponent> terrainComponents;
	
	public TerrainLoader() {
		terrainComponents = new ArrayList<TerrainComponent>();
	}
	
	@Override
	public void parseData(ArrayList<String> lines) {
		
		terrainComponents.clear();
		
		super.parseData(lines);
	}
	
	@Override
	protected void handleData(String key, String value) {
		
		String[] data = value.split(DATA_SPLIT);
		
		double x = Util.parseDouble(data[0]);
		double y = Util.parseDouble(data[1]);
		
		int width = Util.parseInt(data[2]);
		int height = Util.parseInt(data[3]);
		
		addComponent(new TerrainComponent(x, y, width, height));
		
	}
	
	/**
	 * Overriden so that the components can be added in a specific order.
	 * 
	 */
	@Override
	public String getContent() {
		
		String[] lines = new String[terrainComponents.size()];
		
		for(int i = 0; i < lines.length; i++) {
			lines[i] = i + SPLIT + getLine(terrainComponents.get(i));
		}
		
		return Util.getStringFromLines(lines, Util.NL, false);
	}
	
	public TerrainComponent[] getComponents() {
		return terrainComponents.toArray(new TerrainComponent[terrainComponents.size()]);
	}
	
	public void setComponents(TerrainComponent[] components) {
		terrainComponents.clear();// Don't create new object, and don't convert array to list, addComponent method will.
		
		for(int i = 0; i < components.length; i++) {
			
			TerrainComponent component = components[i];
			
			addComponent(component);
			
		}
		
	}
	
	public void addComponent(TerrainComponent component) {
		
		String line = getLine(component);
		
		terrainComponents.add(component);
		
		data.put(terrainComponents.size() + "", line);
		
	}
	
	private String getLine(TerrainComponent component) {
		return component.getX() + DATA_SPLIT + component.getY() + DATA_SPLIT + component.getWidth() +
				DATA_SPLIT + component.getHeight();
	}
	
}
