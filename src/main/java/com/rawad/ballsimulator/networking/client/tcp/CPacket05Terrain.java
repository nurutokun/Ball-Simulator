package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class CPacket05Terrain extends TCPPacket {
	
	public CPacket05Terrain() {
		super(TCPPacketType.TERRAIN);
	}
	
	public CPacket05Terrain(String dataAsString) {
		super(dataAsString);
	}
	
}
