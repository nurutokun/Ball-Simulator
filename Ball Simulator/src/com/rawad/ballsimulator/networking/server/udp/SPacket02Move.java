package com.rawad.ballsimulator.networking.server.udp;

import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;

public class SPacket02Move extends UDPPacket {
	
	private static final int X_INDEX = 1;
	private static final int Y_INDEX = 2;
	
	private static final int VX_INDEX = 3;
	private static final int VY_INDEX = 4;
	
	private static final int AX_INDEX = 5;
	private static final int AY_INDEX = 6;
	
	public SPacket02Move(String username, double x, double y, double vx, double vy, double ax, double ay) {
		super(UDPPacketType.MOVE, username, Double.toString(x), Double.toString(y), Double.toString(vx), Double.toString(vy), 
				Double.toString(ax), Double.toString(ay));
		
	}
	
	public SPacket02Move(byte[] data) {
		super(UDPPacketType.MOVE, data);
		
	}
	
	public double getX() {
		return Double.parseDouble(dataString[X_INDEX]);
	}
	
	public double getY() {
		return Double.parseDouble(dataString[Y_INDEX]);
	}
	
	public double getVx() {
		return Double.parseDouble(dataString[VX_INDEX]);
	}
	
	public double getVy() {
		return Double.parseDouble(dataString[VY_INDEX]);
	}
	
	public double getAx() {
		return Double.parseDouble(dataString[AX_INDEX]);
	}
	
	public double getAy() {
		return Double.parseDouble(dataString[AY_INDEX]);
	}
	
}
