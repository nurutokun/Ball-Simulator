package com.rawad.ballsimulator.networking.server.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class SPacket01Login extends TCPPacket {
	
	private static final int X_INDEX = 1;
	private static final int Y_INDEX = 2;
	
	private static final int WIDTH_INDEX = 3;
	private static final int HEIGHT_INDEX = 4;
	
	private static final int THETA_INDEX = 5;
	
	private static final int TERRAIN_NAME_INDEX = 6;
	
	private static final int LOGIN_INDEX = 7;
	
	public SPacket01Login(String username, double x, double y, int width, int height, double theta, String terrainName, boolean login) {
		super(TCPPacketType.LOGIN, username, Double.toString(x), Double.toString(y), Integer.toString(width), Integer.toString(height), 
				Double.toString(theta), terrainName, Boolean.toString(login));
		
	}
	
	public SPacket01Login(byte[] data) {
		super(TCPPacketType.LOGIN, data);
	}
	
	public double getX() {
		return Double.parseDouble(dataString[X_INDEX]);
	}
	
	public double getY() {
		return Double.parseDouble(dataString[Y_INDEX]);
	}
	
	public int getWidth() {
		return Integer.parseInt(dataString[WIDTH_INDEX]);
	}
	
	public int getHeight() {
		return Integer.parseInt(dataString[HEIGHT_INDEX]);
	}
	
	public double getTheta() {
		return Double.parseDouble(dataString[THETA_INDEX]);
	}
	
	public String getTerrainName() {
		return dataString[TERRAIN_NAME_INDEX];
	}
	
	public boolean canLogin() {
		return Boolean.parseBoolean(dataString[LOGIN_INDEX]);
	}
	
	/*
	private TerrainComponent[] toTerrainComponents(String dataString) {
		
		String[] componentsAsString = dataString.split(COMPONENT_REGEX);
		
		TerrainComponent[] components = new TerrainComponent[componentsAsString.length];
		
		for(int i = 0; i < componentsAsString.length; i++) {
			String[] attribValues = componentsAsString[i].split(Loader.TERRAIN_COMPONENT_ATTRIB_SPLIT);// For consistancy.
			
			TerrainComponent component = Loader.getTerrainComponentFromLine(attribValues);
			
			components[i] = component;			
		}
		
		return components;
		
	}*/
	
}
