package com.rawad.ballsimulator.networking.server.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class SPacket03Message extends TCPPacket {
	
//	private static final int MESSAGE_INDEX = 1;
	
	public SPacket03Message(String username, String message) {
		super(TCPPacketType.MESSAGE, username, message);	
	}
	
	public SPacket03Message(byte[] data) {
		super(TCPPacketType.MESSAGE, data);
	}
	
	public String getMessage() {
		
//		try {
//			return dataString[MESSAGE_INDEX];
//		} catch(ArrayIndexOutOfBoundsException ex) {
		return rawData.substring(getUsername().length() + REGEX.length());
//		}
		
	}
	
}
