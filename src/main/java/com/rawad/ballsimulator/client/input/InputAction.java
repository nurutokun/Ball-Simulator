package com.rawad.ballsimulator.client.input;

public enum InputAction {
	
	// Keyboard
	MOVE_UP("Move Up"),
	MOVE_DOWN("Move Down"),
	MOVE_RIGHT("Move Right"),
	MOVE_LEFT("Move Left"),
	
	TILT_RIGHT("Tilt Right"),
	TILT_LEFT("Tilt Left"),
	TILT_RESET("Tile Reset"),
	
	GEN_POS("Generate Position"),
	
	PAUSE("Pause"),
	INVENTORY("Inventory"),
	
	SHOW_WORLD("Show World"),
	
	SEND("Send"),
	CHAT("Chat"),
	
	DEBUG("Debug"),
	REFRESH("Refresh"),
	FULLSCREEN("Fullscreen"),
	
	CLAMP("Clamp Mouse"),
	SAVE("Save"),
	
	PLAYER_LIST("Player Lit"),
	
	// Mouse.
	PLACE("Place"),
	EXTRACT("Extract"),
	REMOVE("Remove"),
	
	// To avoid null.
	DEFAULT("UNKNOWN");
	
	private final String name;
	
	private InputAction(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static final InputAction getByName(String name) {
		
		for(InputAction action: InputAction.values()) {
			if(action.getName().equals(name)) return action;
		}
		
		return DEFAULT;
		
	}
	
}
