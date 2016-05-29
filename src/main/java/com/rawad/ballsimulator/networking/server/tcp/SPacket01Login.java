package com.rawad.ballsimulator.networking.server.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class SPacket01Login extends TCPPacket {
	
	private static final int ENTITY_ID_INDEX = 1;
	
	private static final int X_INDEX = 2;
	private static final int Y_INDEX = 3;
	
	private static final int WIDTH_INDEX = 4;
	private static final int HEIGHT_INDEX = 5;
	
	private static final int THETA_INDEX = 6;
	
	private static final int TERRAIN_NAME_INDEX = 7;
	
	private static final int LOGIN_INDEX = 8;
	
	public SPacket01Login(int entityId, double x, double y, int width, int height, double theta, String terrainName, 
			boolean login) {
		super(TCPPacketType.LOGIN, Integer.toString(entityId), Double.toString(x), Double.toString(y), 
				Integer.toString(width), Integer.toString(height), Double.toString(theta), terrainName, 
				Boolean.toString(login));
	}
	
	public SPacket01Login(String dataAsString) {
		super(dataAsString);
	}
	
	public int getEntityId() {
		return Integer.parseInt(indexedData[ENTITY_ID_INDEX]);
	}
	
	public double getX() {
		return Double.parseDouble(indexedData[X_INDEX]);
	}
	
	public double getY() {
		return Double.parseDouble(indexedData[Y_INDEX]);
	}
	
	public int getWidth() {
		return Integer.parseInt(indexedData[WIDTH_INDEX]);
	}
	
	public int getHeight() {
		return Integer.parseInt(indexedData[HEIGHT_INDEX]);
	}
	
	public double getTheta() {
		return Double.parseDouble(indexedData[THETA_INDEX]);
	}
	
	public String getTerrainName() {
		return indexedData[TERRAIN_NAME_INDEX];
	}
	
	public boolean canLogin() {
		return Boolean.parseBoolean(indexedData[LOGIN_INDEX]);
	}
	
}
