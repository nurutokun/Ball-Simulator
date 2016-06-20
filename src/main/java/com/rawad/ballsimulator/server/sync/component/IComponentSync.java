package com.rawad.ballsimulator.server.sync.component;

import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.server.udp.ServerDatagramManager;
import com.rawad.gamehelpers.game.entity.Entity;

public interface IComponentSync {
	
	public void sync(Entity e, ServerDatagramManager datagramManager, NetworkComponent networkComp);
	
}
