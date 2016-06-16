package com.rawad.ballsimulator.networking.client.udp;

import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;

public class CPacket03Ping extends UDPPacket {
	
	private static final int PING_INDEX = 2;
	
	public CPacket03Ping(NetworkComponent networkComp, UserComponent userComp) {
		super(UDPPacketType.PING, networkComp.getId(), Integer.toString(userComp.getPing()));
	}
	
	public CPacket03Ping(String dataAsString) {
		super(dataAsString);
	}
	
	public int getPing() {
		return Integer.parseInt(indexedData[PING_INDEX]);
	}
	
}
