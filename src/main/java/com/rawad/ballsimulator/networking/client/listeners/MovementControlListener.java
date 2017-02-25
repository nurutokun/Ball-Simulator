package com.rawad.ballsimulator.networking.client.listeners;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.client.udp.CPacket02Move;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;
import com.rawad.gamehelpers.game.event.Listener;

public class MovementControlListener implements Listener {
	
	private ClientNetworkManager networkManager;
	
	public MovementControlListener(ClientNetworkManager networkManager) {
		this.networkManager = networkManager;
	}
	
	@Override
	public void onEvent(Event ev) {
		
		Entity e = ev.getEntity();
		
		MovementComponent movementComp = e.getComponent(MovementComponent.class);
		NetworkComponent networkComp = e.getComponent(NetworkComponent.class);
		
		if(networkComp != null) networkManager.sendPacket(new CPacket02Move(networkComp, movementComp));
		
	}
	
}
