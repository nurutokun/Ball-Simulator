package com.rawad.ballsimulator.networking;

public class Packet00Invalid extends APacket {
	
	public Packet00Invalid() {
		super(APacket.getFormattedDataString(TCPPacketType.INVALID.getId()));
	}
	
}
