package com.rawad.ballsimulator.input;

import java.util.HashMap;

public class KeyboardInput {
	
	private static HashMap<Integer, Boolean> keyStates;
	
	static {
		
		keyStates = new HashMap<Integer, Boolean>();
		
	}
	
	public static void setKeyDown(int key, boolean value) {
		
		keyStates.put(key, value);
		
	}
	
	public static boolean isKeyDown(int key) {
		
		try {
			
			return keyStates.get(key);
			
		} catch(Exception ex) {
			return false;
		}
		
	}
	
}
