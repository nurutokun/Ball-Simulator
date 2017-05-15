package com.rawad.ballsimulator.networking.client.listeners;

import com.rawad.ballsimulator.game.event.MovementEvent;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.client.udp.CPacket02Move;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;
import com.rawad.gamehelpers.game.event.Listener;

public class ClientMovementRequestHandler implements Listener {
	
	private ClientNetworkManager networkManager;
	
	public ClientMovementRequestHandler(ClientNetworkManager networkManager) {
		super();
		
		this.networkManager = networkManager;
		
	}
	
	@Override
	public void onEvent(Event ev) {
		
		MovementEvent movementEvent = (MovementEvent) ev;
		
		Entity e = movementEvent.getEntityToMove();
		
		NetworkComponent networkComp = e.getComponent(NetworkComponent.class);
		
		if(networkComp != null) networkManager.sendPacket(new CPacket02Move(networkComp, movementEvent.getMovementRequest()));
		
	}
	
}
