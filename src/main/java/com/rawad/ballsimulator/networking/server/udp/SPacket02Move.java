package com.rawad.ballsimulator.networking.server.udp;

import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.gamehelpers.utils.Util;

public class SPacket02Move extends UDPPacket {
	
	private static final int X_INDEX = 2;
	private static final int Y_INDEX = 3;
	
	private static final int VX_INDEX = 4;
	private static final int VY_INDEX = 5;
	
	private static final int AX_INDEX = 6;
	private static final int AY_INDEX = 7;
	
	private static final int THETA_INDEX = 8;
	
	public SPacket02Move(int entityId, double x, double y, double vx, double vy, double ax, double ay, double theta) {
		super(UDPPacketType.MOVE, entityId, Double.toString(x), Double.toString(y), Double.toString(vx), Double.toString(vy),
				Double.toString(ax), Double.toString(ay), Double.toString(theta));
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
