package com.rawad.ballsimulator.networking.server.udp;

import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.UDPPacket;

public class SPacket01Damage extends UDPPacket {
	
	private static final int HEALTH_INDEX = 1;
	
	public SPacket01Damage(String username, double health) {
		super(UDPPacketType.DAMAGE, username, Double.toString(HEALTH_INDEX));
		
	}
	
	public SPacket01Damage(byte[] data) {
		super(UDPPacketType.DAMAGE, data);
		
	}
	
	public double getHealth() {
		return Double.parseDouble(dataString[HEALTH_INDEX]);
	}
	
}
