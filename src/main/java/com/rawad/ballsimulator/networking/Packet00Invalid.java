package com.rawad.ballsimulator.networking;

public class Packet00Invalid extends APacket {
	
	public Packet00Invalid() {
		super(TCPPacketType.INVALID.getId(), "INVALID", "INVALID");
	}
	
}
