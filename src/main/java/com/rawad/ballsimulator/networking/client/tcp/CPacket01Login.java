package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;

public class CPacket01Login extends TCPPacket {
	
	private static final int ENTITY_ID_INDEX = 1;
	
	private static final int USERNAME_INDEX = 2;
	private static final int IP_INDEX = 3;
	
	public CPacket01Login(NetworkComponent networkComp, UserComponent userComp) {
		super(TCPPacketType.LOGIN, Integer.toString(networkComp.getId()), userComp.getUsername(), userComp.getIp());
	}
	
	public CPacket01Login(String dataAsString) {
		super(dataAsString);
	}
	
	public int getEntityId() {
		return Integer.parseInt(indexedData[ENTITY_ID_INDEX]);
	}
	
	public String getUsername() {
		return indexedData[USERNAME_INDEX];
	}
	
	public String getIp() {
		return indexedData[IP_INDEX];
	}
	
}
