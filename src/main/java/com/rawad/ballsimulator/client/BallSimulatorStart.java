package com.rawad.ballsimulator.client;

import com.rawad.ballsimulator.game.BallSimulator;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;

import javafx.application.Application;
import javafx.stage.Stage;

public class BallSimulatorStart extends Application {
	
	private static BallSimulator game = new BallSimulator();
	
	private static Client client = new Client();
	
	public static void main(String... args) {
		
		ResourceManager.init(Util.parseCommandLineArguments(args));
		
		game.getProxies().put(client);
		
		Application.launch(args);
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		GameManager gameLauncher = GameManager.instance();
		
		client.initGui(primaryStage);
		
		gameLauncher.launchGame(game);
		
	}
	
}
