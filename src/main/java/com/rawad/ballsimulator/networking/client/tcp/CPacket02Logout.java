package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;
import com.rawad.ballsimulator.server.entity.NetworkComponent;

public class CPacket02Logout extends TCPPacket {
	
	private static final int ENTITY_ID_INDEX = 1;
	
	public CPacket02Logout(NetworkComponent networkComp) {
		super(TCPPacketType.LOGOUT, Integer.toString(networkComp.getId()));
	}
	
	public CPacket02Logout(String dataAsString) {
		super(dataAsString);
	}
	
	public int getEntityId() {
		return Integer.parseInt(indexedData[ENTITY_ID_INDEX]);
	}
	
}
