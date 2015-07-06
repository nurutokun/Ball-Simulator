package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class CPacket02Logout extends TCPPacket {
	
	private static final int ADDRESS_INDEX = 1;
	
	/**
	 * 
	 * @param username
	 * @param address IP Address of the player that is disconnecting.
	 */
	public CPacket02Logout(String username, String address) {
		super(TCPPacketType.LOGOUT, username, address);
	}
	
	public CPacket02Logout(byte[] data) {
		super(TCPPacketType.LOGOUT, data);
	}
	
	public String getAddress() {
		return dataString[ADDRESS_INDEX];
	}
	
}
