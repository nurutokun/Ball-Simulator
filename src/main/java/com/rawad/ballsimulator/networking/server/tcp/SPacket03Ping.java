package com.rawad.ballsimulator.networking.server.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;


public class SPacket03Ping extends TCPPacket {
	
	private static final int INDEX_PING = 1;
	
	public SPacket03Ping(String username, int ping) {
		super(TCPPacketType.PING, username, Integer.toString(ping));
		
	}
	
	public SPacket03Ping(byte[] data) {
		super(TCPPacketType.PING, data);
		
	}
	
	public int getPing() {
		return Integer.parseInt(dataString[INDEX_PING]);
	}
	
}
