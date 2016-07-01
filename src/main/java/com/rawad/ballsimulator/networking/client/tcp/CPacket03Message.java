package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class CPacket03Message extends TCPPacket {
	
	private static final int INDEX_SENDER = 1;
	
	public CPacket03Message(String sender, String message) {
		super(TCPPacketType.MESSAGE, sender, message);
	}
	
	public CPacket03Message(String dataAsString) {
		super(dataAsString);
	}
	
	public String getSender() {
		return indexedData[INDEX_SENDER];
	}
	
	public String getMessage() {
		String dataWithoutTypeId = dataAsString.substring(dataAsString.indexOf(REGEX) + REGEX.length());
		String dataWithoutSender = dataWithoutTypeId.substring(dataWithoutTypeId.indexOf(REGEX) + REGEX.length());
		
		return dataWithoutSender;
	}
	
}
