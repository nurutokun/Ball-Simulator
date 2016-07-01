package com.rawad.ballsimulator.networking.server.udp;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;

public class SPacket02Move extends UDPPacket {
	
	private static final int INDEX_X = 2;
	private static final int INDEX_Y = 3;
	
	private static final int INDEX_VX = 4;
	private static final int INDEX_VY = 5;
	
	private static final int INDEX_AX = 6;
	private static final int INDEX_AY = 7;
	
	private static final int INDEX_THETA = 8;
	
	public SPacket02Move(NetworkComponent networkComp, TransformComponent transformComp, MovementComponent movementComp) {
		super(UDPPacketType.MOVE, networkComp.getId(), Double.toString(transformComp.getX()), 
				Double.toString(transformComp.getY()), Double.toString(movementComp.getVx()), 
				Double.toString(movementComp.getVy()), Double.toString(movementComp.getAx()), 
				Double.toString(movementComp.getAy()), Double.toString(transformComp.getTheta()));
	}
	
	public SPacket02Move(String dataAsString) {
		super(dataAsString);
	}
	
	public double getX() {
		return Double.parseDouble(indexedData[INDEX_X]);
	}
	
	public double getY() {
		return Double.parseDouble(indexedData[INDEX_Y]);
	}
	
	public double getVx() {
		return Double.parseDouble(indexedData[INDEX_VX]);
	}
	
	public double getVy() {
		return Double.parseDouble(indexedData[INDEX_VY]);
	}
	
	public double getAx() {
		return Double.parseDouble(indexedData[INDEX_AX]);
	}
	
	public double getAy() {
		return Double.parseDouble(indexedData[INDEX_AY]);
	}
	
	public double getTheta() {
		return Double.parseDouble(indexedData[INDEX_THETA]);
	}
	
}
