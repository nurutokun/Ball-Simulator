package com.rawad.ballsimulator.networking.client;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.networking.client.udp.CPacket02Move;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;

public class NetworkMovementSystem extends GameSystem {
	
	private ClientNetworkManager networkManager;
	
	public NetworkMovementSystem(ClientNetworkManager networkManager) {
		super();
		
		this.networkManager = networkManager;
		
		compatibleComponentTypes.add(NetworkComponent.class);
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(MovementComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		NetworkComponent networkComp = e.getComponent(NetworkComponent.class);
		MovementComponent movementComp = e.getComponent(MovementComponent.class);
		
		CPacket02Move moveRequest = new CPacket02Move(networkComp, movementComp);
		
		networkManager.sendPacket(moveRequest);
		
	}
	
}
