package com.rawad.ballsimulator.gamestates;

import java.awt.Graphics2D;
import java.util.HashMap;

import com.rawad.ballsimulator.log.Logger;

public class StateManager {
	
	private HashMap<StateEnum, State> states;
	
	private State currentState;
	
	public StateManager() {
		
		states = new HashMap<StateEnum, State>();
		
	}
	
	public void update() {
		
		try {
			currentState.update();
			
		} catch(NullPointerException ex) {
			Logger.log(Logger.DEBUG, "current state is null for updating");
		}
		
	}
	
	public void render(Graphics2D g) {
		
		try {
			currentState.render(g);
			
		} catch(NullPointerException ex) {
			Logger.log(Logger.DEBUG, "current state is null for rendering");
		}
		
	}
	
	public void addState(State state) {
		
		states.put(state.getStateType(), state);
		
		currentState = state;
		
	}
	
	public void setState(StateEnum stateType) {
		
		try {
			
			currentState = states.get(stateType);
			
		} catch(Exception ex) {
			
			Logger.log(Logger.WARNING, "The state: " + stateType + " has not been created.");
			
		}
		
	}
	
}
