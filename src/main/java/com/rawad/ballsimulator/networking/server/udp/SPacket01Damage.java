package com.rawad.ballsimulator.networking.server.udp;

import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.UDPPacket;

public class SPacket01Damage extends UDPPacket {
	
	private static final int HEALTH_INDEX = 2;
	
	public SPacket01Damage(int entityId, double health) {
		super(UDPPacketType.DAMAGE, entityId, Double.toString(HEALTH_INDEX));
	}
	
	public SPacket01Damage(String dataAsString) {
		super(dataAsString);
	}
	
	public double getHealth() {
		return Double.parseDouble(indexedData[HEALTH_INDEX]);
	}
	
}
