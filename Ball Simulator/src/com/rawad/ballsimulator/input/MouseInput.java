package com.rawad.ballsimulator.input;

import java.util.HashMap;

import com.rawad.ballsimulator.log.Logger;

public class MouseInput {
	
	public static final int LEFT_MOUSE_BUTTON = 0;
	public static final int RIGHT_MOUSE_BUTTON = 1;
	public static final int MIDDLE_MOUSE_BUTTON = 2;
	
	private static final int DOWN_INDEX = 0;
	private static final int CLICK_INDEX = 1;// Prevents click, drag-on-to-surface, release to count as a click
	
	private static HashMap<Integer, Boolean[]> mouseStates;
	
	private static int x;
	private static int y;
	
	private static int mouseWheelPosition;
	
	static {
		
		mouseStates = new HashMap<Integer, Boolean[]>();
		
		mouseStates.put(LEFT_MOUSE_BUTTON, new Boolean[]{false, false});
		mouseStates.put(RIGHT_MOUSE_BUTTON, new Boolean[]{false, false});
		mouseStates.put(MIDDLE_MOUSE_BUTTON, new Boolean[]{false, false});
		
	}
	
	public static boolean isButtonDown(int key) {
		
		try {
			
			return mouseStates.get(key)[DOWN_INDEX];
			
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; tried to check for button that wasn't a button");
			return false;
		}
		
	}
	
	public static void setButtonDown(int key, boolean value) {
		
		try {
			
			setIndexedValue(key, DOWN_INDEX, value);
			
		} catch(Exception ex) {
			// key isn't valid
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; tried to set button that wasn't a button");
		}
		
	}
	
	public static boolean isButtonClicked(int key) {
		
		try {
			
			return mouseStates.get(key)[CLICK_INDEX];
			
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; tried to check for button that wasn't a button");
			return false;
		}
		
	}
	
	public static void setButtonClicked(int key, boolean value) {
		
		try {
			
			setIndexedValue(key, CLICK_INDEX, value);
			
		} catch(Exception ex) {
			// key isn't valid
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; tried to set button that wasn't a button");
		}
		
	}
	
	private static void setIndexedValue(int mouseType, int index, boolean value) {
		
		Boolean[] currentValues = mouseStates.get(mouseType);
		
		currentValues[index] = value;
		
		mouseStates.put(mouseType, currentValues);
		
	}
	
	public static int getMouseWheelPosition() {
		return mouseWheelPosition;
	}
	
	public static void setMouseWheelPosition(int mouseWheelPosition) {
		
		MouseInput.mouseWheelPosition = mouseWheelPosition;
		
	}
	
	public static int getX() {
		return x;
	}
	
	public static void setX(int x) {
		MouseInput.x = x;
	}
	
	public static int getY() {
		return y;
	}
	
	public static void setY(int y) {
		MouseInput.y = y;
	}
	
}
