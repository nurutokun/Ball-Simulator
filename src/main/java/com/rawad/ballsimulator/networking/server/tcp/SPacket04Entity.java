package com.rawad.ballsimulator.networking.server.tcp;

import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class SPacket04Entity extends TCPPacket {
	
	private static final int INDEX_ENTITY_NAME = 1;
	
	private static final int INDEX_X = 2;
	private static final int INDEX_Y = 3;
	
	private static final int INDEX_SCALE_X = 4;
	private static final int INDEX_SCALE_Y = 5;
	
	private static final int INDEX_THETA = 6;
	
	private static final int INDEX_LAST = 7;
	
	public SPacket04Entity(String entityName, TransformComponent transformComp, boolean last) {
		super(TCPPacketType.ENTITY, entityName, Double.toString(transformComp.getX()), 
				Double.toString(transformComp.getY()), Double.toString(transformComp.getScaleX()), 
				Double.toString(transformComp.getScaleY()), Double.toString(transformComp.getTheta()), 
				Boolean.toString(last));
	}
	
	public SPacket04Entity(String dataAsString) {
		super(dataAsString);
	}
	
	public String getEntityName() {
		return indexedData[INDEX_ENTITY_NAME];
	}
	
	public double getX() {
		return Double.parseDouble(indexedData[INDEX_X]);
	}
	
	public double getY() {
		return Double.parseDouble(indexedData[INDEX_Y]);
	}
	
	public double getScaleX() {
		return Double.parseDouble(indexedData[INDEX_SCALE_X]);
	}
	
	public double getScaleY() {
		return Double.parseDouble(indexedData[INDEX_SCALE_Y]);
	}
	
	public double getTheta() {
		return Double.parseDouble(indexedData[INDEX_THETA]);
	}
	
	public boolean isLast() {
		return Boolean.parseBoolean(indexedData[INDEX_LAST]);
	}
	
}
