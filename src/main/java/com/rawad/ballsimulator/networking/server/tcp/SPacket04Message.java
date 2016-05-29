package com.rawad.ballsimulator.networking.server.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class SPacket04Message extends TCPPacket {
	
	private static final int SENDER_INDEX = 1;
	
	public SPacket04Message(String sender, String message) {
		super(TCPPacketType.MESSAGE, sender, message);	
	}
	
	public SPacket04Message(String dataAsString) {
		super(dataAsString);
	}
	
	public String getSender() {
		return indexedData[SENDER_INDEX];
	}
	
	public String getMessage() {
		String dataWithoutTypeId = dataAsString.substring(dataAsString.indexOf(REGEX));
		String dataWithoutSender = dataWithoutTypeId.substring(dataWithoutTypeId.indexOf(REGEX));
		
		return dataWithoutSender;
	}
	
}
