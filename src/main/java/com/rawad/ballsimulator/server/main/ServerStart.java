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
	
	private static final String TERRAIN_NAME = "terrain";
	
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
		
		game.getProxies().put(server);
		
		if(useGui) {
			
			ResourceManager.init(commands);
			
			serverGui = new ServerGui(server);
			
			game.getProxies().put(serverGui);

			Thread guiThread = new Thread(() -> Application.launch(args), "Gui Thread");
			guiThread.setDaemon(true);
			guiThread.start();
			
		} else {
			
			GameManager.instance().launchGame(game);
			
		}
		
		CustomLoader loader = game.getLoaders().get(CustomLoader.class);
		
		TerrainFileParser parser = game.getFileParsers().get(TerrainFileParser.class);
		
		game.addTask(new Task<Integer>() {
			
			@Override
			protected Integer call() throws Exception {
				
				Logger.log(Logger.DEBUG, "Loading terrain...");
				loader.loadTerrain(parser, game.getWorld(), TERRAIN_NAME);
				Logger.log(Logger.DEBUG, "Terrain loaded successfully.");
				
				Logger.log(Logger.DEBUG, "Initializing network manager...");
				server.getNetworkManager().init();// Allows for world to be initialized before clients can connect.
				Logger.log(Logger.DEBUG, "Network manager initialized.");
				
				return 0;
				
			}
		});
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		serverGui.initGui(primaryStage);
		
		GameManager.instance().launchGame(game);
		
	}
	
}
