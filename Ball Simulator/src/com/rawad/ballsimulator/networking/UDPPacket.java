package com.rawad.ballsimulator.networking;

public abstract class UDPPacket extends Packet {
	
	public UDPPacket(UDPPacketType type, String username, String... data) {
		super(type.getId(), username, data);
		
	}
	
	public UDPPacket(UDPPacketType type, byte[] data) {
		super(type.getId(), data);
		
	}
	
}
