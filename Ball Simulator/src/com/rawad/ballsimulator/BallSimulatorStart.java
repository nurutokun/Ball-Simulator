package com.rawad.ballsimulator;

import com.rawad.ballsimulator.main.BallSimulator;
import com.rawad.gamehelpers.displaymanager.DisplayManager;
import com.rawad.gamehelpers.game.GameManager;

public class BallSimulatorStart {
	
	public static void main(String[] args) {
		
//		BallSimulator.instance().init();
		
		// Complete Free Hacking Courses: Go From Beginner to Exprt Hacker Today!
		// 1:00:00
		
		GameManager.initGame(new BallSimulator());
		
		DisplayManager.setDisplayMode(DisplayManager.Mode.WINDOWED);
		
	}
	
}
