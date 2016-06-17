package com.rawad.ballsimulator.server.gui;

import com.rawad.ballsimulator.client.gui.entity.player.PlayerList;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.server.udp.ServerDatagramManager;
import com.rawad.ballsimulator.server.IServerSync;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.application.Platform;

public class GuiSync implements IServerSync {
	
	private PlayerList playerList;
	
	public GuiSync(PlayerList playerList) {
		this.playerList = playerList;
	}
	
	@Override
	public void sync(Entity e, NetworkComponent networkComp, ServerDatagramManager datagramManager) {
		Platform.runLater(() -> {
			playerList.refresh();// Did get an exception without runLater wrapper.
		});
	}
	
}
