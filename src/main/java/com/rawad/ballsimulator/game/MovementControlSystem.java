package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MovementControlSystem extends GameSystem implements EventHandler<KeyEvent> {
	
	private boolean up;
	private boolean down;
	private boolean right;
	private boolean left;
	
	public MovementControlSystem() {
		super();
		
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
		
		if(eventType == KeyEvent.KEY_PRESSED) {
			keyPressed(event.getCode());
		} else if(eventType == KeyEvent.KEY_RELEASED) {
			keyReleased(event.getCode());
		}
		
	}
	
	private void keyPressed(KeyCode keyCode) {
		switch(keyCode) {
		
		case UP:
		case W:
			up = true;
			down = false;
			break;
			
		case DOWN:
		case S:
			down = true;
			up = false;
			break;
			
		case RIGHT:
		case D:
			right = true;
			left = false;
			break;
			
		case LEFT:
		case A:
			left = true;
			right = false;
			break;
			
		default:
			break;
		
		}
	}
	
	private void keyReleased(KeyCode keyCode) {
		switch(keyCode) {
		
		case UP:
		case W:
			up = false;
			break;
			
		case DOWN:
		case S:
			down = false;
			break;
			
		case RIGHT:
		case D:
			right = false;
			break;
			
		case LEFT:
		case A:
			left = false;
			break;
			
		default:
			break;
		
		}
	}
	
}
