	package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.client.input.InputBindings;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.entity.event.Event;

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
		
		MovementComponent movementComp = e.getComponent(MovementComponent.class);
		
		if(movementComp.isUp() != up || movementComp.isDown() != down || movementComp.isRight() != right || 
				movementComp.isLeft() != left) {
			
			movementComp.setUp(up);
			movementComp.setDown(down);
			movementComp.setRight(right);
			movementComp.setLeft(left);
			
			gameEngine.submitEvent(new Event(e, MovementComponent.class));
			
		}
		
	}
	
	@Override
	public void handle(KeyEvent event) {
		
		EventType<KeyEvent> eventType = event.getEventType();
		
		InputAction action = inputBindings.get(event.getCode());
		
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
