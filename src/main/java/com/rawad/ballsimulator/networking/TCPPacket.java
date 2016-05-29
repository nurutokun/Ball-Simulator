package com.rawad.ballsimulator.networking;

import com.rawad.gamehelpers.utils.Util;

public abstract class TCPPacket extends APacket {
	
	public TCPPacket(TCPPacketType type, String... data) {
		super(APacket.getFormattedDataString(REGEX, Util.prepend(data, type.getId())));
	}
	
	public TCPPacket(String dataAsString) {
		super(dataAsString);
	}
	
	public static TCPPacketType getPacketTypeFromData(String dataAsString) {
		return TCPPacketType.getPacketTypeById(APacket.getPacketIdFromString(dataAsString));
	}
	
}
