package com.rawad.ballsimulator.client.input;

import java.util.HashMap;

import com.rawad.gamehelpers.utils.Util;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public final class InputBindings {
	
	private final HashMap<Input, InputAction> bindings = new HashMap<Input, InputAction>();
	
	public InputAction put(InputAction action, KeyCode input) {
		return bindings.put(new Input(input), action);
	}
	
	public InputAction get(KeyCode input) {
		return Util.getNullSafe(bindings.get(new Input(input)), InputAction.DEFAULT);
	}
	
	public InputAction put(InputAction action, MouseButton input) {
		return bindings.put(new Input(input), action);
	}
	
	public InputAction get(MouseButton input) {
		return Util.getNullSafe(bindings.get(new Input(input)), InputAction.DEFAULT);
	}
	
	public HashMap<Input, InputAction> getBindings() {
		return bindings;
	}
	
}
