package com.rawad.ballsimulator.client.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public final class InputBindings {
	
	private final HashMap<Input, InputAction> bindings = new HashMap<Input, InputAction>();
	
	public InputAction put(InputAction action, KeyCode input) {
		return bindings.put(new Input(input), action);
	}
	
	public InputAction get(KeyCode input) {
		return bindings.getOrDefault(new Input(input), InputAction.DEFAULT);
	}
	
	public InputAction put(InputAction action, MouseButton input) {
		return bindings.put(new Input(input), action);
	}
	
	public InputAction get(MouseButton input) {
		return bindings.getOrDefault(new Input(input), InputAction.DEFAULT);
	}
	
	/**
	 * Returns the {@code Input} that represents the key to the given {@code action} only if there is a 1:1 correspondance.
	 * 
	 * @param action
	 * @return
	 */
	public Input get(InputAction action) {
		
		ArrayList<Input> matchingKeys = new ArrayList<Input>();
		
		for(Entry<Input, InputAction> entry: bindings.entrySet()) {
			if(action.equals(entry.getValue())) matchingKeys.add(entry.getKey());
		}
		
		if(matchingKeys.isEmpty() || matchingKeys.size() > 1) return null;
		
		return matchingKeys.get(0);
		
	}
	
	public HashMap<Input, InputAction> getBindings() {
		return bindings;
	}
	
}
