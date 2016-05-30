package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class CPacket01Login extends TCPPacket {
	
	private static final int ENTITY_ID_INDEX = 1;
	
	public CPacket01Login(int entityId) {
		super(TCPPacketType.LOGIN, Integer.toString(entityId));
	}
	
	public CPacket01Login(String dataAsString) {
		super(dataAsString);
	}
	
	public int getEntityId() {
		return Integer.parseInt(indexedData[ENTITY_ID_INDEX]);
	}
	
}
