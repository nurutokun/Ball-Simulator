package com.rawad.ballsimulator.client.input;

import com.rawad.gamehelpers.client.input.AInput;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class Input extends AInput {
	
	private final KeyCode key;
	private final MouseButton button;
	
	private Type type;
	private String name = "";
	
	private Input(KeyCode key, MouseButton button) {
		super();
		
		this.key = key;
		this.button = button;
		
	}
	
	public Input(KeyCode key) {
		this(key, null);
		
		name = key.getName();
		type = Type.KEY;
		
	}
	
	public Input(MouseButton button) {
		this(null, button);
		
		name = button.name();
		type = Type.MOUSE;
	}
	
	public KeyCode getKey() {
		return key;
	}
	
	public MouseButton getButton() {
		return button;
	}
	
	public Type getType() {
		return type;
	}
	
	@Override
	protected String getName() {
		return name;
	}
	
	@Override
	protected boolean equals(AInput otherAInput) {
		
		if(otherAInput instanceof Input) {
			
			Input otherInput = (Input) otherAInput;
			
			return type == otherInput.getType() && (key == otherInput.getKey() || button == otherInput.getButton());
			
		}
		
		return false;
		
	}
	
	public static enum Type {
		
		KEY,
		MOUSE
		
	}
	
}
