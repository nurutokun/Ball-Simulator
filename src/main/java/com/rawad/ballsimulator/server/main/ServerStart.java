package com.rawad.ballsimulator.server.main;

import java.util.HashMap;

import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.BallSimulator;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.server.Server;
import com.rawad.ballsimulator.server.gui.ServerGui;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public class ServerStart extends Application {
	
	private static final Server server = new Server();
	
	private static final BallSimulator game = new BallSimulator();
	
	private static ServerGui serverGui;
	
	public static void main(String[] args) {
		
		HashMap<String, String> commands = Util.parseCommandLineArguments(args);
		
		boolean useGui = false;
		
		try {
			useGui = Boolean.parseBoolean(commands.get("useGui"));
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, "Didn't specify whether or not gui should be used so it won't be.");
		}
		
		GameManager.instance().launchGame(game, server);
		
		synchronized(game) {
			try {
				game.wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		if(useGui) {
			
			ResourceManager.init(commands);
			
			serverGui = new ServerGui(server);
			
			serverGui.init(game);
			
		}
		
		CustomLoader loader = game.getLoader(CustomLoader.class);
		
		TerrainFileParser parser = game.getFileParser(TerrainFileParser.class);
		
		server.addTask(new Task<Integer>() {
			
			@Override
			protected Integer call() throws Exception {
				
				Logger.log(Logger.DEBUG, "Loading terrain...");
				loader.loadTerrain(parser, game.getWorld(), Server.TERRAIN_NAME);
				Logger.log(Logger.DEBUG, "Terrain loaded successfully.");
				
				return 0;
				
			}
		});
		
		if(useGui) Application.launch(args);
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		serverGui.initGui(primaryStage);
		
	}
	
}
