package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;

public class CPacket01Login extends TCPPacket {
	
	private static final int INDEX_ENTITY_ID = 1;
	
	private static final int INDEX_USERNAME = 2;
	private static final int INDEX_IP = 3;
	
	public CPacket01Login(NetworkComponent networkComp, UserComponent userComp) {
		super(TCPPacketType.LOGIN, Integer.toString(networkComp.getId()), userComp.getUsername(), userComp.getIp());
	}
	
	public CPacket01Login(String dataAsString) {
		super(dataAsString);
	}
	
	public int getEntityId() {
		return Integer.parseInt(indexedData[INDEX_ENTITY_ID]);
	}
	
	public String getUsername() {
		return indexedData[INDEX_USERNAME];
	}
	
	public String getIp() {
		return indexedData[INDEX_IP];
	}
	
}
