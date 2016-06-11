package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class CPacket02Logout extends TCPPacket {
	
	public CPacket02Logout() {
		super(TCPPacketType.LOGOUT);
	}
	
	public CPacket02Logout(String dataAsString) {
		super(dataAsString);
	}
	
}
