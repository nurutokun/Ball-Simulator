package com.rawad.ballsimulator.displaymanager;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import com.rawad.ballsimulator.input.MouseInput;
import com.rawad.ballsimulator.log.Logger;

public class DisplayManager {
	
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(202, 212, 227);
	
	private static final int SCREEN_WIDTH = 640;
	private static final int SCREEN_HEIGHT = 480;
	
	// vvv All of these should be changeable to what the user desires
	public static int REFRESH_RATE = 60;
	
	private static int DISPLAY_WIDTH = 640;// not final; changes with the resizing of the window
	private static int DISPLAY_HEIGHT = 480;
	
	private static int FULLSCREEN_WIDTH = 640;
	private static int FULLSCREEN_HEIGHT = 480;
	
	private static int FPS_SAMPLE_COUNT = 25;
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
	
	
	public static void destroyWindow() {
		
		running = false;
		
		currentDisplayMode.destroy();
		
		workerThread = null;
		currentDisplayMode = null;
		
		System.exit(0);
		
	}
	
	public static String[] getCompatibleDisplayModeResolutions() {
		
		java.awt.DisplayMode[] modes = Fullscreen.getCompatibleDisplayModes();
		
		ArrayList<String> prevRes = new ArrayList<String>();
		
		boolean matchFound = false;;
		
		for(int i = 0; i < modes.length; i++) {
			
			String currentRes = modes[i].getWidth() + "x" + modes[i].getHeight();
			
			for(int j = 0; j < prevRes.size(); j++) {
				
				if(prevRes.get(j).equals(currentRes)) {
					matchFound = true;
					break;
				}
				
			}
			
			if(matchFound) {
				break;
			} else {
				prevRes.add(currentRes);
				
			}
			
		}
		
		String[] resolutions = new String[prevRes.size()];
		
		for(int i = 0; i < resolutions.length; i++) {
			resolutions[i] = prevRes.get(i);
		}
		
		return resolutions;
		
	}
	
	public static void changeFullScreenResolution(String resolution) {
		
		int width = Integer.parseInt(resolution.split("x")[0]);
		int height = Integer.parseInt(resolution.split("x")[1]);
		
		FULLSCREEN_WIDTH = width;
		FULLSCREEN_HEIGHT = height;
		
	}
	
	public static String getFullScreenResolution() {
		return getFullScreenWidth() + "x" + getFullScreenHeight();
	}
	
	public static int getFullScreenWidth() {
		return FULLSCREEN_WIDTH;
	}
	
	public static int getFullScreenHeight() {
		return FULLSCREEN_HEIGHT;
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
			
			prevTime = System.currentTimeMillis();// To keep the initial value limited to zero, just in case.
			
			while(running) {
				
				long currentTime = System.currentTimeMillis();
				
				long deltaTime = currentTime - prevTime;
				
				timePassed = (deltaTime <= 0? 1:deltaTime);
				
				totalTime += (1000 / timePassed);
				
//				averageFrameRate = (timePassed);
				
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
