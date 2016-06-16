package com.rawad.ballsimulator.networking.client.udp.entity;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.client.udp.CPacket02Move;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.gamehelpers.game.entity.Entity;

public class MovementComponentUpdater extends AComponentUpdater {
	
	@Override
	public void tick(ClientNetworkManager networkManager, Entity e, NetworkComponent networkComp) {
		
		MovementComponent movementComp = e.getComponent(MovementComponent.class);
		
		if(movementComp == null) return;
		
		CPacket02Move moveRequest = new CPacket02Move(networkComp, movementComp);
		
		networkManager.sendPacket(moveRequest);
		
	}
	
}
