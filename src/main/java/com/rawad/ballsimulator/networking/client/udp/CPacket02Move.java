package com.rawad.ballsimulator.networking.client.udp;

import com.rawad.ballsimulator.game.MovementRequest;
import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;

public class CPacket02Move extends UDPPacket {
	
	private static final int INDEX_UP = 2;
	private static final int INDEX_DOWN = 3;
	private static final int INDEX_RIGHT = 4;
	private static final int INDEX_LEFT = 5;
	
	public CPacket02Move(NetworkComponent networkComp, MovementRequest movementRequesst) {
		super(UDPPacketType.MOVE, networkComp.getId(), Boolean.toString(movementRequesst.isUp()), 
				Boolean.toString(movementRequesst.isDown()), Boolean.toString(movementRequesst.isRight()), 
				Boolean.toString(movementRequesst.isLeft()));
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
