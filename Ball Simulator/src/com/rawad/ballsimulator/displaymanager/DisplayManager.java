package com.rawad.ballsimulator.displaymanager;


public class DisplayManager {
	
	public static final int REFRESH_RATE = 60;
	
	private static DisplayMode currentDisplayMode;
	
	public static void setDisplayMode(Mode mode) {
		
		if(currentDisplayMode != null) {
			currentDisplayMode.destroy();
		}
		
		currentDisplayMode = mode.getDisplayMode();
		
		currentDisplayMode.create();
		
	}
	
	public static int getWidth() {
		return currentDisplayMode.getWidth();
	}
	
	public static int getHeight() {
		return currentDisplayMode.getHeight();
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
