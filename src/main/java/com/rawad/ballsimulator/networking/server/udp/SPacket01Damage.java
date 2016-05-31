package com.rawad.ballsimulator.networking.server.udp;

import com.rawad.ballsimulator.entity.HealthComponent;
import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;

public class SPacket01Damage extends UDPPacket {
	
	private static final int HEALTH_INDEX = 2;
	
	public SPacket01Damage(int entityId, HealthComponent healthComp) {
		super(UDPPacketType.DAMAGE, entityId, Double.toString(healthComp.getHealth()));
	}
	
	public SPacket01Damage(String dataAsString) {
		super(dataAsString);
	}
	
	public double getHealth() {
		return Double.parseDouble(indexedData[HEALTH_INDEX]);
	}
	
}
