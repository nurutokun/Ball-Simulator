package com.rawad.ballsimulator.networking.client.udp.entity;

import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.gamehelpers.game.entity.Entity;

public abstract class AComponentUpdater {
	
	public abstract void tick(ClientNetworkManager networkManager, Entity e, NetworkComponent networkComp);
	
}
