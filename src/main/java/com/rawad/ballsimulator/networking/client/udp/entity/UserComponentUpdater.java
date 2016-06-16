package com.rawad.ballsimulator.networking.client.udp.entity;

import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.client.tcp.CPacket03Ping;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.gamehelpers.game.entity.Entity;

public class UserComponentUpdater extends AComponentUpdater {
	
	private static final int TICKS_PER_UPDATE = 50;
	
	@Override
	public void tick(ClientNetworkManager networkManager, Entity e, NetworkComponent networkComp) {
		
		UserComponent userComp = e.getComponent(UserComponent.class);
		
		if(userComp == null) return;
		
		userComp.setPingUpdateCounter(userComp.getPingUpdateCounter() + 1);
		
		if(userComp.getPingUpdateCounter() >= TICKS_PER_UPDATE) {
			
			CPacket03Ping pingRequest = new CPacket03Ping(System.currentTimeMillis());
			
			networkManager.sendPacket(pingRequest);
			
			userComp.setPingUpdateCounter(0);
			
		}
		
	}
	
}
