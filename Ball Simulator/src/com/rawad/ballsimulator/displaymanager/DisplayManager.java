package com.rawad.ballsimulator.displaymanager;

import java.awt.Color;
import java.awt.Component;

import com.rawad.ballsimulator.input.MouseInput;
import com.rawad.ballsimulator.log.Logger;

public class DisplayManager {
	
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 255);
	
	private static final int SCREEN_WIDTH = 640;
	private static final int SCREEN_HEIGHT = 480;
	
	// vvv All of these should be changeable to what the user desires
	public static int REFRESH_RATE = 60;
	
	private static int DISPLAY_WIDTH = 640;// not final; should be changable by user
	private static int DISPLAY_HEIGHT = 480;
	
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
	
	/**
	 * Original width of the screen of the game. Used for GAME LOGIC.
	 * 
	 * @return
	 */
	public static int getScreenWidth() {
		return SCREEN_WIDTH;
	}
	
	/**
	 * Original height of the screen of the game. Used for GAME LOGIC.
	 * 
	 * @return
	 */
	public static int getScreenHeight() {
		return SCREEN_HEIGHT;
	}
	
	/**
	 * Display width which is scaled up/down to from the original screen width.
	 * 
	 * @return
	 */
	public static int getDisplayWidth() {
		return DISPLAY_WIDTH;
	}
	
	public static void setDisplayWidth(int width) {
		DISPLAY_WIDTH = width;
	}
	
	/**
	 * Display height which is scaled up/down to from the original screen height.
	 * 
	 * @return
	 */
	public static int getDisplayHeight() {
		return DISPLAY_HEIGHT;
	}
	
	public static void setDisplayHeight(int height) {
		DISPLAY_HEIGHT = height;
	}
	
	public static void setRunning(boolean running) {
		DisplayManager.running = running;
	}
	
	public static Component getCurrentWindowComponent() {
		return currentDisplayMode.getCurrentWindow();
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
				
				MouseInput.update(getCurrentWindowComponent(), getDeltaTime());
				
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
