package com.rawad.ballsimulator;

import com.rawad.ballsimulator.main.BallSimulator;
import com.rawad.gamehelpers.gamemanager.GameManager;

public class BallSimulatorStart {
	
	private static BallSimulator game = new BallSimulator();
	
	public static void main(String[] args) {
		
		GameManager gameLauncher = GameManager.instance();
		
		gameLauncher.launchGame(game);
		
	}
	
}
