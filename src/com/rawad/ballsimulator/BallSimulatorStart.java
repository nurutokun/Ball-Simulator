package com.rawad.ballsimulator;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.main.BallSimulator;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.resources.ResourceManager;

public class BallSimulatorStart {
	
	private static BallSimulator game = new BallSimulator();
	
	public static void main(String[] args) {
		
		ResourceManager.init(args);
		
		GameManager gameLauncher = GameManager.instance();
		
		Client client = new Client();
		
		gameLauncher.launchGame(game, client);
		
	}
	
}
