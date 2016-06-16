package com.rawad.ballsimulator.networking.server.udp;

import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;

public class SPacket03Ping extends UDPPacket {
	
	private static final int PING_INDEX = 2;
	
	private static final int REQUEST_INDEX = 3;
	
	public SPacket03Ping(NetworkComponent networkComp, UserComponent userComp, boolean request) {
		super(UDPPacketType.PING, networkComp.getId(), Integer.toString(userComp.getPing()), Boolean.toString(request));
	}
	
	public SPacket03Ping(String dataAsString) {
		super(dataAsString);
	}
	
	public int getPing() {
		return Integer.parseInt(indexedData[PING_INDEX]);
	}
	
	public boolean isRequest() {
		return Boolean.parseBoolean(indexedData[REQUEST_INDEX]);
	}
	
}
