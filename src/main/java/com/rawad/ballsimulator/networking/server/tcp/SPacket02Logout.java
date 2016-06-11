package com.rawad.ballsimulator.networking.server.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class SPacket02Logout extends TCPPacket {
	
	private static final int ENTITY_ID_INDEX = 1;
	
	public SPacket02Logout(int entityId) {
		super(TCPPacketType.LOGOUT, Integer.toString(entityId));
	}
	
	public SPacket02Logout(String dataAsString) {
		super(dataAsString);
	}
	
	public int getEntityId() {
		return Integer.parseInt(indexedData[ENTITY_ID_INDEX]);
	}
	
}
