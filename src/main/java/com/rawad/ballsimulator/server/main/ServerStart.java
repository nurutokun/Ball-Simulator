package com.rawad.ballsimulator.server.main;

import java.util.HashMap;

import com.rawad.ballsimulator.game.BallSimulator;
import com.rawad.ballsimulator.server.Server;
import com.rawad.ballsimulator.server.gui.ServerGui;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;

import javafx.application.Application;

public class ServerStart {
	
	public static final Server server = new Server();
	
	private static BallSimulator game = new BallSimulator();
	
	public static void main(String[] args) {
		
		HashMap<String, String> commands = Util.parseCommandLineArguments(args);
		
		boolean useGui = false;
		
		try {
			useGui = Boolean.parseBoolean(commands.get("useGui"));
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, "Didn't specify whether or not gui should be used so it won't be.");
		}
		
		GameManager.instance().launchGame(game, server);
		
		if(useGui) {
			ResourceManager.init(commands);
			
			Application.launch(ServerGui.class);
		}
		
	}
	
}
