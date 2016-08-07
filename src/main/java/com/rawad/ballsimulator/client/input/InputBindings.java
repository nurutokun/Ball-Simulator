package com.rawad.ballsimulator.client.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public final class InputBindings {
	
	private final HashMap<InputAction, Input> bindings = new HashMap<InputAction, Input>();
	
	public Input put(InputAction action, KeyCode input) {
		return put(action, new Input(input));
	}
	
	public InputAction get(KeyCode input) {
		return get(new Input(input));
	}
	
	public Input put(InputAction action, MouseButton input) {
		return put(action, new Input(input));
	}
	
	public InputAction get(MouseButton input) {
		return get(new Input(input));
	}
	
	public Input put(InputAction action, Input input) {
		return bindings.put(action, input);
	}
	
	/**
	 * Returns the {@code InputAction} that represents the key to the given {@code input} only if there is a 1:1 
	 * correspondance.
	 * 
	 * @param input
	 * @return
	 */
	public InputAction get(Input input) {
		
		ArrayList<InputAction> matchingKeys = new ArrayList<InputAction>();
		
		for(Entry<InputAction, Input> entry: bindings.entrySet()) {
			if(input.equals(entry.getValue())) matchingKeys.add(entry.getKey());
		}
		
		if(matchingKeys.isEmpty() || matchingKeys.size() > 1) return InputAction.DEFAULT;
		
		return matchingKeys.get(0);
		
	}
	
	public Input get(InputAction action) {
		return bindings.get(action);
	}
	
	public HashMap<InputAction, Input> getBindings() {
		return bindings;
	}
	
}
