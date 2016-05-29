package com.rawad.ballsimulator.networking.client.udp;

import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;

public class CPacket02Move extends UDPPacket {
	
	private static final int UP_INDEX = 2;
	private static final int DOWN_INDEX = 3;
	private static final int RIGHT_INDEX = 4;
	private static final int LEFT_INDEX = 5;
	
	public CPacket02Move(int entityId, boolean up, boolean down, boolean right, boolean left) {
		super(UDPPacketType.MOVE, entityId, Boolean.toString(up), Boolean.toString(down), Boolean.toString(right), 
				Boolean.toString(left));
	}
	
	public CPacket02Move(String dataAsString) {
		super(dataAsString);
	}
	
	public boolean isUp() {
		return Boolean.parseBoolean(indexedData[UP_INDEX]);
	}
	
	public boolean isDown() {
		return Boolean.parseBoolean(indexedData[DOWN_INDEX]);
	}
	
	public boolean isRight() {
		return Boolean.parseBoolean(indexedData[RIGHT_INDEX]);
	}
	
	public boolean isLeft() {
		return Boolean.parseBoolean(indexedData[LEFT_INDEX]);
	}
	
}
