package com.rawad.ballsimulator.server.sync.component;

import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.ballsimulator.networking.server.udp.SPacket03Ping;
import com.rawad.ballsimulator.networking.server.udp.ServerDatagramManager;
import com.rawad.gamehelpers.game.entity.Entity;

public class PingSync implements IComponentSync {
	
	@Override
	public void sync(Entity e, ServerDatagramManager datagramManager, NetworkComponent networkComp) {
		
		UserComponent userComp = e.getComponent(UserComponent.class);
		
		if(userComp != null)
			datagramManager.sendPacketToAllClients(new SPacket03Ping(networkComp, userComp, System.nanoTime(), true));
		
	}
	
}
