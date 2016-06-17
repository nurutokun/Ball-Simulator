package com.rawad.ballsimulator.server;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.server.udp.SPacket02Move;
import com.rawad.ballsimulator.networking.server.udp.ServerDatagramManager;
import com.rawad.gamehelpers.game.entity.Entity;

public class MovementSync implements IServerSync {
	
	@Override
	public void sync(Entity e, NetworkComponent networkComp, ServerDatagramManager datagramManager) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		MovementComponent movementComp = e.getComponent(MovementComponent.class);
		
		if(movementComp != null && transformComp != null)
			datagramManager.sendPacketToAllClients(new SPacket02Move(networkComp, transformComp, movementComp));
		
		
	}
	
}
