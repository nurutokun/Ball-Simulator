package com.rawad.ballsimulator.client.input;

import java.util.HashMap;

public final class InputBindings {
	
	private static final HashMap<Input, InputAction> BINDINGS = new HashMap<Input, InputAction>();
	
	public static InputAction putBinding(Input input, InputAction action) {
		return BINDINGS.put(input, action);
	}
	
	public static InputAction getAction(Input input) {
		return BINDINGS.get(input);
	}
	
}
