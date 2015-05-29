package com.rawad.ballsimulator.displaymanager;

import com.rawad.ballsimulator.log.Logger;


public class DisplayManager {
	
	public static final int REFRESH_RATE = 60;
	
	private static int SCREEN_WIDTH = 500;// not final; should be changable by user
	private static int SCREEN_HEIGHT = 500;
	
	private static DisplayMode currentDisplayMode;
	
	private static long prevTime;
	private static long timePassed;
	
	public static void setDisplayMode(Mode mode) {
		
		if(currentDisplayMode != null) {
			currentDisplayMode.destroy();
		}
		
		currentDisplayMode = mode.getDisplayMode();
		
		currentDisplayMode.create();
		
		new Thread(new Runnable(){
			
			public void run() {
				
				while(true) {
					
					long currentTime = System.currentTimeMillis();
					
					timePassed = 1000 / (currentTime - prevTime);
					
					prevTime = currentTime;
					
					DisplayManager.repaint();
					
					try {
						Thread.sleep(1000/DisplayManager.REFRESH_RATE);
					} catch(InterruptedException ex) {
						Logger.log(Logger.SEVERE, "Thread was interrupted");
					}
					
				}
				
			}
			
		}).start();
	}
	
	public static void repaint() {
		
		currentDisplayMode.repaint();
		
	}
	
	public static long getDeltaTime() {
		return timePassed;
	}
	
	public static int getWidth() {
		return SCREEN_WIDTH;
	}
	
	public static int getHeight() {
		return SCREEN_HEIGHT;
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
	
}
