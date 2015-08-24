package com.rawad.ballsimulator;

import com.rawad.ballsimulator.main.BallSimulator;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.game.GameManager;

public class BallSimulatorStart {
	
	public static void main(String[] args) {
		
//		BallSimulator.instance().init();
		
		GameManager.initGame(new BallSimulator());
		
		DisplayManager.setDisplayMode(DisplayManager.Mode.WINDOWED);
		
	}
	
}
