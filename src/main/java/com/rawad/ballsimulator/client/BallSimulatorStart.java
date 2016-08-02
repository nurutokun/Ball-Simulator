package com.rawad.ballsimulator.client;

import com.rawad.ballsimulator.game.BallSimulator;
import com.rawad.gamehelpers.game.GameManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class BallSimulatorStart extends Application {
	
	private static final BallSimulator game = new BallSimulator();
	
	private static final Client client = new Client();
	
	public static void main(String... args) {
		
		game.getProxies().put(client);
		
		Application.launch(args);
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		client.setStage(primaryStage);
		
		GameManager.launchGame(game);
		
	}
	
}
