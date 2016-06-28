package com.rawad.ballsimulator.client;

import com.rawad.ballsimulator.game.BallSimulator;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;

import javafx.application.Application;
import javafx.stage.Stage;

public class BallSimulatorStart extends Application {
	
	private static final BallSimulator game = new BallSimulator();
	
	private static final Client client = new Client();
	
	public static void main(String... args) {
		
		ResourceManager.init(Util.parseCommandLineArguments(args));
		
		game.getProxies().put(client);
		
		GameManager.instance().launchGame(game);
		
		Application.launch(args);
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		client.setStage(primaryStage);
		
		client.initGui();
		
	}
	
}
