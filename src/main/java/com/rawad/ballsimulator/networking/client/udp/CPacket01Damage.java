package com.rawad.ballsimulator.networking.client.udp;

import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.server.entity.NetworkComponent;

public class CPacket01Damage extends UDPPacket {
	
	private static final int DAMAGE_INDEX = 2;
	
	public CPacket01Damage(NetworkComponent networkComp, double damage) {
		super(UDPPacketType.DAMAGE, networkComp.getId(), Double.toString(damage));
	}
	
	public CPacket01Damage(String dataAsString) {
		super(dataAsString);
	}
	
	public double getDamage() {
		return Double.parseDouble(indexedData[DAMAGE_INDEX]);
	}
	
}
