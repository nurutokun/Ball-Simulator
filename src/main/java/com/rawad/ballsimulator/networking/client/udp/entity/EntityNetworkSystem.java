package com.rawad.ballsimulator.networking.client.udp.entity;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;

public class EntityNetworkSystem extends GameSystem {
	
	private ClientNetworkManager networkManager;
	
	private ArrayList<AComponentUpdater> updaters;
	
	public EntityNetworkSystem(ClientNetworkManager networkManager) {
		super();
		
		this.networkManager = networkManager;
		
		updaters = new ArrayList<AComponentUpdater>();
		
		compatibleComponentTypes.add(NetworkComponent.class);
		compatibleComponentTypes.add(TransformComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		NetworkComponent networkComp = e.getComponent(NetworkComponent.class);
		
		for(AComponentUpdater updater: updaters) {
			updater.tick(networkManager, e, networkComp);
		}
		
	}
	
	public ArrayList<AComponentUpdater> getUpdaters() {
		return updaters;
	}
	
}
