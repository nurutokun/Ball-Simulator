package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.gamehelpers.client.input.InputBindings;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;

public class MovementControlSystem extends GameSystem implements EventHandler<KeyEvent> {
	
	private InputBindings inputBindings;
	
	private boolean up;
	private boolean down;
	private boolean right;
	private boolean left;
	
	public MovementControlSystem(InputBindings inputBindings) {
		super();
		
		this.inputBindings = inputBindings;
		
		compatibleComponentTypes.add(UserControlComponent.class);
		compatibleComponentTypes.add(MovementComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		MovementComponent movingComp = e.getComponent(MovementComponent.class);
		
		movingComp.setUp(up);
		movingComp.setDown(down);
		movingComp.setRight(right);
		movingComp.setLeft(left);
		
	}
	
	@Override
	public void handle(KeyEvent event) {
		
		EventType<KeyEvent> eventType = event.getEventType();
		
		InputAction action = (InputAction) inputBindings.get(event.getCode());
		
		if(eventType == KeyEvent.KEY_PRESSED) {
			keyPressed(action);
		} else if(eventType == KeyEvent.KEY_RELEASED) {
			keyReleased(action);
		}
		
	}
	
	private void keyPressed(InputAction action) {
		switch(action) {
		
		case MOVE_UP:
			up = true;
			down = false;
			break;
			
		case MOVE_DOWN:
			down = true;
			up = false;
			break;
			
		case MOVE_RIGHT:
			right = true;
			left = false;
			break;
			
		case MOVE_LEFT:
			left = true;
			right = false;
			break;
			
		default:
			break;
		
		}
	}
	
	private void keyReleased(InputAction action) {
		switch(action) {
		
		case MOVE_UP:
			up = false;
			break;
			
		case MOVE_DOWN:
			down = false;
			break;
			
		case MOVE_RIGHT:
			right = false;
			break;
			
		case MOVE_LEFT:
			left = false;
			break;
			
		default:
			break;
		
		}
	}
	
}
