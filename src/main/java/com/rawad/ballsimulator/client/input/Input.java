package com.rawad.ballsimulator.client.input;

import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public final class Input {
	
	private final KeyCode key;
	private final MouseButton button;
	
	private final Type type;
	
	private Input(KeyCode key, MouseButton button, Type type) {
		super();
		
		this.key = key;
		this.button = button;
		
		this.type = type;
		
	}
	
	public Input(KeyCode key) {
		this(key, null, Type.KEY);
	}
	
	public Input(MouseButton button) {
		this(null, button, Type.MOUSE_BUTTON);
	}
	
	public String getName() {
		
		switch(type) {
		
		case KEY:
			return key.getName();
			
		case MOUSE_BUTTON:
			return button.name();
			
			default:
				return "NO NAME FOUND";
		
		}
		
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof Input) {
			
			Input input = (Input) obj;
			
			if(input.type == type) {
				switch(type) {
				
				case KEY:
					return Util.equals(input.key, key);
				
				case MOUSE_BUTTON:
					return Util.equals(input.button, button);
					
				}
			}
			
		}
		
		return false;
		
	}
	
	public static Input getByName(String inputName) {
		
		KeyCode key = KeyCode.getKeyCode(inputName);
		
		if(key != null) return new Input(key);
		
		try {
			MouseButton button = MouseButton.valueOf(inputName);
			
			if(button != null) return new Input(button);
			
		} catch(Exception ex) {}
		
		Logger.log(Logger.WARNING, "Input name \"" + inputName + "\" does not match any input.");
		
		return null;
		
	}
	
	private static enum Type {
		
		KEY,
		MOUSE_BUTTON;
		
		
		
	}
	
}
