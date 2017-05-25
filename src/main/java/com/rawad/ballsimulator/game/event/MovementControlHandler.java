package com.rawad.ballsimulator.game.event;

import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.game.MovementRequest;
import com.rawad.gamehelpers.game.GameEngine;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.EventManager;
import com.rawad.jfxengine.client.input.InputBindings;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;

/**
 * @author Rawad
 *
 */
public class MovementControlHandler implements EventHandler<KeyEvent> {
	
	private final EventManager eventManager;
	
	private final InputBindings inputBindings;
	
	private Entity entityToControl;
	
	private boolean up = false;
	private boolean down = false;
	private boolean right = false;
	private boolean left = false;
	
	/**
	 * @param gameEngine
	 * @param inputBindings
	 * @param entityToControl
	 */
	public MovementControlHandler(GameEngine gameEngine, InputBindings inputBindings, Entity entityToControl) {
		super();
		
		this.inputBindings = inputBindings;
		this.entityToControl = entityToControl;
		
		this.eventManager = gameEngine.getEventManager();
		
	}
	
	@Override
	public void handle(KeyEvent event) {
		
		EventType<KeyEvent> eventType = event.getEventType();
		
		Object actionObject = inputBindings.get(event.getCode());
		
		if(!(actionObject instanceof InputAction)) return;
		
		InputAction action = (InputAction) actionObject;
		
		if(eventType == KeyEvent.KEY_PRESSED) {
			
			updateMovement(action, true);
			
		} else if(eventType == KeyEvent.KEY_RELEASED) {
			
			updateMovement(action, false);
			
		}
		
		eventManager.submitEvent(new MovementEvent(entityToControl, new MovementRequest(up, down, right, left)));
		
	}
	
	private void updateMovement(InputAction action, boolean keyPressed) {
		
		switch(action) {
		
		case MOVE_UP:
			if(keyPressed) {
				up = true;
				down = false;
			} else {
				up = false;
			}
			break;
			
		case MOVE_DOWN:
			if(keyPressed) {
				down = true;
				up = false;
			} else {
				down = false;
			}
			break;
			
		case MOVE_RIGHT:
			if(keyPressed) {
				right = true;
				left = false;
			} else {
				right = false;
			}
			break;
			
		case MOVE_LEFT:
			if(keyPressed) {
				left = true;
				right = false;
			} else {
				left = false;
			}
			break;
			
		default:
			break;
		
		}
		
	}
	
}
