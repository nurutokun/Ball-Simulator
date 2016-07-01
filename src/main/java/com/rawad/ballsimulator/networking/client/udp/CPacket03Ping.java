package com.rawad.ballsimulator.networking.client.udp;

import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.ballsimulator.networking.server.udp.SPacket03Ping;

public class CPacket03Ping extends UDPPacket {
	
	private static final int INDEX_PING = 2;
	
	private static final int INDEX_TIME_STAMP = 3;
	
	/**
	 * 
	 * @param networkComp
	 * @param userComp
	 * @param timeStamp 
	 * 			Value from {@link SPacket03Ping#getTimeStamp()} or 0 if {@link SPacket03Ping#isRequest()} is {@code false}.
	 */
	public CPacket03Ping(NetworkComponent networkComp, UserComponent userComp, long timeStamp) {
		super(UDPPacketType.PING, networkComp.getId(), Integer.toString(userComp.getPing()), Long.toString(timeStamp));
	}
	
	public CPacket03Ping(String dataAsString) {
		super(dataAsString);
	}
	
	public int getPing() {
		return Integer.parseInt(indexedData[INDEX_PING]);
	}
	
	public long getTimeStamp() {
		return Long.parseLong(indexedData[INDEX_TIME_STAMP]);
	}
	
}
