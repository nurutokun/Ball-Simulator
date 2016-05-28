package com.rawad.ballsimulator.networking;

public abstract class TCPPacket extends APacket {
	
	public TCPPacket(TCPPacketType type, String username, String... data) {
		super(type.getId(), username, data);
		
	}
	
	public TCPPacket(TCPPacketType type, byte[] data) {
		super(type.getId(), data);
		
	}
	
}
