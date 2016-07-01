package com.rawad.ballsimulator.networking.client.udp;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;

public class CPacket02Move extends UDPPacket {
	
	private static final int INDEX_UP = 2;
	private static final int INDEX_DOWN = 3;
	private static final int INDEX_RIGHT = 4;
	private static final int INDEX_LEFT = 5;
	
	public CPacket02Move(NetworkComponent networkComp, MovementComponent movementComp) {
		super(UDPPacketType.MOVE, networkComp.getId(), Boolean.toString(movementComp.isUp()), 
				Boolean.toString(movementComp.isDown()), Boolean.toString(movementComp.isRight()), 
				Boolean.toString(movementComp.isLeft()));
	}
	
	public CPacket02Move(String dataAsString) {
		super(dataAsString);
	}
	
	public boolean isUp() {
		return Boolean.parseBoolean(indexedData[INDEX_UP]);
	}
	
	public boolean isDown() {
		return Boolean.parseBoolean(indexedData[INDEX_DOWN]);
	}
	
	public boolean isRight() {
		return Boolean.parseBoolean(indexedData[INDEX_RIGHT]);
	}
	
	public boolean isLeft() {
		return Boolean.parseBoolean(indexedData[INDEX_LEFT]);
	}
	
}
