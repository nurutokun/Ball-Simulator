package com.rawad.ballsimulator.client.input;

import java.util.HashMap;

import com.rawad.gamehelpers.utils.Util;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public final class InputBindings {
	
	private final HashMap<KeyCode, InputAction> keyBindings = new HashMap<KeyCode, InputAction>();
	private final HashMap<MouseButton, InputAction> mouseBindings = new HashMap<MouseButton, InputAction>();
	
	public InputAction put(InputAction action, KeyCode input) {
		return keyBindings.put(input, action);
	}
	
	public InputAction get(KeyCode input) {
		return Util.getNullSafe(keyBindings.get(input), InputAction.DEFAULT);
	}
	
	public HashMap<KeyCode, InputAction> getKeyBindings() {
		return keyBindings;
	}
	
	public InputAction put(InputAction action, MouseButton input) {
		return mouseBindings.put(input, action);
	}
	
	public InputAction get(MouseButton input) {
		return Util.getNullSafe(mouseBindings.get(input), InputAction.DEFAULT);
	}
	
	public HashMap<MouseButton, InputAction> getMouseBindings() {
		return mouseBindings;
	}
	
}
