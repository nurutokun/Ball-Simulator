package com.rawad.ballsimulator.server.gui;

import com.rawad.ballsimulator.client.gui.entity.player.PlayerList;
import com.rawad.ballsimulator.server.Server;
import com.rawad.ballsimulator.server.sync.IServerSync;

import javafx.application.Platform;

public class GuiSync implements IServerSync {
	
	private PlayerList playerList;
	
	public GuiSync(PlayerList playerList) {
		this.playerList = playerList;
	}
	
	@Override
	public void sync(Server server) {
		Platform.runLater(() -> {
			playerList.refresh();// Did get an exception without runLater wrapper.
		});
	}
	
}
