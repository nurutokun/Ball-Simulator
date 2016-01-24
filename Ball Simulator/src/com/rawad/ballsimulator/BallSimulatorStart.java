package com.rawad.ballsimulator;

import com.rawad.ballsimulator.main.BallSimulator;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;

public class BallSimulatorStart {
	
	private static BallSimulator game = new BallSimulator();
	
	public static void main(String[] args) {
		
		try {
			
			boolean devEnv = Boolean.getBoolean(args[0]);
			
			ResourceManager.setBasePath(devEnv);
			
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, "Can ignore or add command line argument(s).");
		}
		
		GameManager gameLauncher = GameManager.instance();
		
		gameLauncher.launchGame(game);
		
	}
	
}
