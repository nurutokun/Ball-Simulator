package com.rawad.ballsimulator.server;

import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.server.udp.ServerDatagramManager;
import com.rawad.gamehelpers.game.entity.Entity;

public interface IServerSync {
	
	public void sync(Entity e, NetworkComponent networkComp, ServerDatagramManager datagramManager);
	
}
