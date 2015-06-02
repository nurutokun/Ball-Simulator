package com.rawad.ballsimulator.displaymanager;

import java.awt.Color;

import com.rawad.ballsimulator.log.Logger;

public class DisplayManager {
	
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 255);
	
	// vvv All of these should be changeable to what the user desires
	public static int REFRESH_RATE = 60;
	
	private static int SCREEN_WIDTH = 640;// not final; should be changable by user
	private static int SCREEN_HEIGHT = 480;
	
	private static int FPS_SAMPLE_COUNT = 20;
	// ^^^
	
	private static DisplayMode currentDisplayMode;
	
	private static Thread workerThread = new Thread(new WorkerThread());
	
	private static long prevTime;
	private static long timePassed;
	
	private static long averageFrameRate;
	
	private static boolean running;
	
	public static void setDisplayMode(Mode mode) {
		
		if(currentDisplayMode != null) {
			running = false;// Helps (a lot) to avoid null exceptions
			currentDisplayMode.destroy();
		}
		
		currentDisplayMode = mode.getDisplayMode();
		
		currentDisplayMode.create();
		
		running = true;
		
		if(!workerThread.isAlive()) {
			workerThread.start();
		}
		
	}
	
	public static long getFPS() {
		return averageFrameRate;
	}
	
	public static long getDeltaTime() {
		return timePassed;
	}
	
	public static int getWidth() {
		return SCREEN_WIDTH;
	}
	
	public static void setWidth(int width) {
		SCREEN_WIDTH = width;
	}
	
	public static int getHeight() {
		return SCREEN_HEIGHT;
	}
	
	public static void setHeight(int height) {
		SCREEN_HEIGHT = height;
	}
	
	public static void setRunning(boolean running) {
		DisplayManager.running = running;
	}
	
	public static enum Mode {
		
		WINDOWED(new Windowed()),
		FULLSCREEN(new Fullscreen());
		
		private final DisplayMode displayMode;
		
		private Mode(DisplayMode displayMode) {
			this.displayMode = displayMode;
		}
		
		public DisplayMode getDisplayMode() {
			return displayMode;
		}
		
	}
	
	private static class WorkerThread implements Runnable {
		
		public void run() {
			
			int i = 0;
			int totalTime = 0;
			
			while(running) {
				
				long currentTime = System.currentTimeMillis();
				
				long deltaTime = currentTime - prevTime;
				
				timePassed = 1000 / (deltaTime <= 0? 1:deltaTime);
				
				totalTime += timePassed;
				
				i++;
				
				if(i >= FPS_SAMPLE_COUNT) {
					
					averageFrameRate = totalTime/i;
					
					i = 0;
					totalTime = 0;
					
				}
				
				prevTime = currentTime;
				
				currentDisplayMode.repaint();
				
				try {
					Thread.sleep(1000/DisplayManager.REFRESH_RATE);
				} catch(InterruptedException ex) {
					Logger.log(Logger.SEVERE, "Thread was interrupted");
				}
				
			}
			
		}
		
	}
	
}
