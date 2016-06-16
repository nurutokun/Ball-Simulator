package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class CPacket04Entity extends TCPPacket {
	
	public CPacket04Entity() {
		super(TCPPacketType.ENTITY);
	}
	
	public CPacket04Entity(String dataAsString) {
		super(dataAsString);
	}
	
}
