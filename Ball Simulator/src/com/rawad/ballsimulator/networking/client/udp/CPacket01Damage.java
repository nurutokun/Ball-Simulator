package com.rawad.ballsimulator.networking.client.udp;

import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;

public class CPacket01Damage extends UDPPacket {
	
	private static final int DAMAGE_INDEX = 1;
	
	public CPacket01Damage(String username, double damage) {
		super(UDPPacketType.DAMAGE, username, Double.toString(damage));
	}
	
	public CPacket01Damage(byte[] data) {
		super(UDPPacketType.DAMAGE, data);
		
	}
	
	public double getDamage() {
		return Double.parseDouble(dataString[DAMAGE_INDEX]);
	}
	
}
