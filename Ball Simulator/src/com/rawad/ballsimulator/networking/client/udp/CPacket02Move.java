package com.rawad.ballsimulator.networking.client.udp;

import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;

public class CPacket02Move extends UDPPacket {
	
	private static final int UP_INDEX = 1;
	private static final int DOWN_INDEX = 2;
	private static final int RIGHT_INDEX = 3;
	private static final int LEFT_INDEX = 4;
	
	public CPacket02Move(String username, boolean up, boolean down, boolean right, boolean left) {
		super(UDPPacketType.MOVE, username, Boolean.toString(up), Boolean.toString(down), Boolean.toString(right), Boolean.toString(left));
	}
	
	public CPacket02Move(byte[] data) {
		super(UDPPacketType.MOVE, data);
	}
	
	public boolean isUp() {
		return Boolean.parseBoolean(dataString[UP_INDEX]);
	}
	
	public boolean isDown() {
		return Boolean.parseBoolean(dataString[DOWN_INDEX]);
	}
	
	public boolean isRight() {
		return Boolean.parseBoolean(dataString[RIGHT_INDEX]);
	}
	
	public boolean isLeft() {
		return Boolean.parseBoolean(dataString[LEFT_INDEX]);
	}
	
}
