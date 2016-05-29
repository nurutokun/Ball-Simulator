package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class CPacket02Logout extends TCPPacket {
	
	private static final int ADDRESS_INDEX = 1;
	
	public CPacket02Logout(String address) {
		super(TCPPacketType.LOGOUT, address);
	}
	
	public String getAddress() {
		return indexedData[ADDRESS_INDEX];
	}
	
}
