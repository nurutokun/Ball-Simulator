package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class CPacket05Entity extends TCPPacket {
	
	public CPacket05Entity() {
		super(TCPPacketType.ENTITY);
	}
	
	public CPacket05Entity(String dataAsString) {
		super(dataAsString);
	}
	
}
