package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class CPacket04Message extends TCPPacket {
	
//	private static final int MESSAGE_INDEX = 1;
	
	public CPacket04Message(String username, String message) {
		super(TCPPacketType.MESSAGE, username, message);
	}
	
	public CPacket04Message(byte[] data) {
		super(TCPPacketType.MESSAGE, data);
	}
	
	public String getMessage() {
		
//		try {
//			return getUsername() + "> " + dataString[MESSAGE_INDEX];
//		} catch(ArrayIndexOutOfBoundsException ex) {
		return rawData.substring(getUsername().length() + REGEX.length());
//		}
		
	}
	
}
