package com.rawad.ballsimulator.networking.server.tcp;

import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class SPacket05Terrain extends TCPPacket {
	
	private static final int ENTITY_NAME_INDEX = 1;
	
	private static final int X_INDEX = 2;
	private static final int Y_INDEX = 3;
	
	private static final int SCALE_X_INDEX = 4;
	private static final int SCALE_Y_INDEX = 5;
	
	private static final int LAST_INDEX = 6;
	
	public SPacket05Terrain(String entityName, TransformComponent transformComp, boolean last) {
		super(TCPPacketType.TERRAIN, entityName, Double.toString(transformComp.getX()), 
				Double.toString(transformComp.getY()), Double.toString(transformComp.getScaleX()), 
				Double.toString(transformComp.getScaleY()), Boolean.toString(last));
	}
	
	public SPacket05Terrain(String dataAsString) {
		super(dataAsString);
	}
	
	public String getEntityName() {
		return indexedData[ENTITY_NAME_INDEX];
	}
	
	public double getX() {
		return Double.parseDouble(indexedData[X_INDEX]);
	}
	
	public double getY() {
		return Double.parseDouble(indexedData[Y_INDEX]);
	}
	
	public double getScaleX() {
		return Double.parseDouble(indexedData[SCALE_X_INDEX]);
	}
	
	public double getScaleY() {
		return Double.parseDouble(indexedData[SCALE_Y_INDEX]);
	}
	
	public boolean isLast() {
		return Boolean.parseBoolean(indexedData[LAST_INDEX]);
	}
	
}
