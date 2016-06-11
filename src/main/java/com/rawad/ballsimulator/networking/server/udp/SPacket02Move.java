package com.rawad.ballsimulator.networking.server.udp;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.server.entity.NetworkComponent;
import com.rawad.gamehelpers.utils.Util;

public class SPacket02Move extends UDPPacket {
	
	private static final int X_INDEX = 2;
	private static final int Y_INDEX = 3;
	
	private static final int VX_INDEX = 4;
	private static final int VY_INDEX = 5;
	
	private static final int AX_INDEX = 6;
	private static final int AY_INDEX = 7;
	
	private static final int THETA_INDEX = 8;
	
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
		return Util.parseDouble(indexedData[X_INDEX]);
	}
	
	public double getY() {
		return Util.parseDouble(indexedData[Y_INDEX]);
	}
	
	public double getVx() {
		return Util.parseDouble(indexedData[VX_INDEX]);
	}
	
	public double getVy() {
		return Util.parseDouble(indexedData[VY_INDEX]);
	}
	
	public double getAx() {
		return Util.parseDouble(indexedData[AX_INDEX]);
	}
	
	public double getAy() {
		return Util.parseDouble(indexedData[AY_INDEX]);
	}
	
	public double getTheta() {
		return Util.parseDouble(indexedData[THETA_INDEX]);
	}
	
}
