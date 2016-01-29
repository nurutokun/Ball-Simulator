package com.rawad.ballsimulator.networking.server.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class SPacket02Logout extends TCPPacket {
	
	public SPacket02Logout(String username) {
		super(TCPPacketType.LOGOUT, username);
	}
	
	public SPacket02Logout(byte[] data) {
		super(TCPPacketType.LOGOUT, data);
	}
	
}
