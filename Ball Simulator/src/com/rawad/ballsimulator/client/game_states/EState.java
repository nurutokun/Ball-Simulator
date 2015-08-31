package com.rawad.ballsimulator.client.game_states;

public enum EState {
	
	GAME("Game State"),
	MENU("Menu State"),
	OPTION("Option State"),
	WORLD_EDITOR("World Editor State"),
	MULTIPLAYER_GAME("Multiplayer Game State");
	
	private final String id;
	
	private EState(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
}
