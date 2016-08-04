package com.rawad.ballsimulator.client.input;

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
			
			if(input.type == type && (input.key == key || input.button == button)) return true;
			
		}
		
		return false;
		
	}
	
	private static enum Type {
		
		KEY,
		MOUSE_BUTTON;
		
	}
	
}
