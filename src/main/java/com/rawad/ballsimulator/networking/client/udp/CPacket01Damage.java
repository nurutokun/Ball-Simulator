package com.rawad.ballsimulator.networking.client.udp;

import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;

public class CPacket01Damage extends UDPPacket {
	
	private static final int DAMAGE_INDEX = 2;
	
	public CPacket01Damage(int entityId, double damage) {
		super(UDPPacketType.DAMAGE, entityId, Double.toString(damage));
	}
	
	public CPacket01Damage(String dataAsString) {
		super(dataAsString);
	}
	
	public double getDamage() {
		return Double.parseDouble(indexedData[DAMAGE_INDEX]);
	}
	
}
