package com.rawad.ballsimulator.networking;

public class Packet00Invalid extends Packet {
	
	public Packet00Invalid() {
		super(TCPPacketType.INVALID.getId(), "INVALID", "INVALID");
	}
	
}
