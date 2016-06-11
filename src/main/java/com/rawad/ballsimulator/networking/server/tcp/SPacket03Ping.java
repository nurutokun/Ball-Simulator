package com.rawad.ballsimulator.networking.server.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;


public class SPacket03Ping extends TCPPacket {
	
	private static final int INDEX_PING = 1;
	
	public SPacket03Ping(long timeStamp) {
		super(TCPPacketType.PING, Long.toString(timeStamp));
	}
	
	public SPacket03Ping(String dataAsString) {
		super(dataAsString);
	}
	
	public long getTimeStamp() {
		return Long.parseLong(indexedData[INDEX_PING]);
	}
	
}
