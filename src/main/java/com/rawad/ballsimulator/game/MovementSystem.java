package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.game.event.EntityCollisionEvent;
import com.rawad.ballsimulator.game.event.EventType;
import com.rawad.ballsimulator.game.event.MovementEvent;
import com.rawad.ballsimulator.game.event.WallCollisionEvent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;
import com.rawad.gamehelpers.game.event.EventManager;
import com.rawad.gamehelpers.game.event.Listener;

public class MovementSystem extends GameSystem implements Listener {
	
	private static final double JERK = 0.7d;// 0.1d
	
	private static final double MAX_ACCEL = JERK * 2d;// 2.0d
	private static final double MIN_ACCEL = 0.1d;// 0.01d
	private static final double MIN_VELOCITY = 0.4d;// 0.4d
	
	private static final double FRICTION = 0.9d;
	
	public MovementSystem(EventManager eventManager) {
		super();
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(MovementComponent.class);
		
		eventManager.registerListener(EventType.COLLISION_ENTITY, this);
		eventManager.registerListener(EventType.COLLISION_WALL, this);
		eventManager.registerListener(EventType.MOVEMENT, this);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		MovementComponent movementComp = e.getComponent(MovementComponent.class);
		
		double ay = movementComp.getAy();
		
		if(Math.abs(ay) > MAX_ACCEL) {
			ay = Math.signum(ay) * MAX_ACCEL;
		}
		
		double ax = movementComp.getAx();
		
		if(Math.abs(ax) > MAX_ACCEL) {
			ax = Math.signum(ax) * MAX_ACCEL;
		}
		
		double vx = movementComp.getVx();
		double vy = movementComp.getVy();
		
		vx += ax;//Math.sqrt(Math.pow(vx, 2) + (ax));
		vy += ay;//Math.sqrt(Math.pow(vy, 2) + (ay));
		
		if(Math.abs(ax) < MIN_ACCEL) {
			ax = 0;
		}
		
		if(Math.abs(ay) < MIN_ACCEL) {
			ay = 0;
		}
		
		if(ax == 0) {
			vx *= FRICTION;
			
			if(Math.abs(vx) < MIN_VELOCITY) {
				vx = 0;
			}
			
		}
		
		if(ay == 0) {
			vy *= FRICTION;
			
			if(Math.abs(vy) < MIN_VELOCITY) {
				vy = 0;
			}
			
		}
		
		movementComp.setAy(ay);
		movementComp.setAx(ax);
		
		movementComp.setVx(vx);
		movementComp.setVy(vy);
		
		transformComp.setX(transformComp.getX() + vx);
		transformComp.setY(transformComp.getY() + vy);
		
	}
	
	@Override
	public void onEvent(Event ev) {
		
		switch((EventType) ev.getEventType()) {
		
		case COLLISION_ENTITY:
			handleEntityCollision((EntityCollisionEvent) ev);
			break;
			
		case COLLISION_WALL:
			handleWallCollision((WallCollisionEvent) ev);
			break;
			
		case MOVEMENT:
			handleMovement((MovementEvent) ev);
			break;
			
			default:
				break;
		
		}
		
	}
	
	private void handleWallCollision(WallCollisionEvent ev) {
		handleCollision(ev.getEntity(), ev.isCollideX(), ev.isCollideY());
	}
	
	private void handleEntityCollision(EntityCollisionEvent ev) {
		handleCollision(ev.getEntity(), ev.getCollidingWithX() != null, ev.getCollidingWithY() != null);
	}
	
	// TODO: Collision is now broken. Doesn't bounce back enough for some reason. Collision event is now queued.
	// This is just temporary for now, should make separate collision for entity vs. wall (maybe depending on type of entity).
	private void handleCollision(Entity e, boolean collideX, boolean collideY) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		MovementComponent movementComp = e.getComponent(MovementComponent.class);
		
		double ax = movementComp.getAx();
		double ay = movementComp.getAy();
		
		double vx = movementComp.getVx();
		double vy = movementComp.getVy();
		
		double newX = transformComp.getX();
		double newY = transformComp.getY();
		
		if(collideX) {
			newX -= vx * 2;
			
			ax = -ax / 2d;// ax /= 2d
			vx = -vx * 3d/4d;// 3/4
			
		}
		
		if(collideY) {
			newY -= vy * 2;
			
			ay = -ay / 2d;
			vy = -vy * 3d/4d;
			
		}
		
		movementComp.setAy(ay);
		movementComp.setAx(ax);
		
		movementComp.setVx(vx);
		movementComp.setVy(vy);
		
		transformComp.setX(newX);
		transformComp.setY(newY);
		
	}
	
	private void handleMovement(MovementEvent ev) {
		
		Entity entityToMove = ev.getEntityToMove();
		MovementRequest movementRequest = ev.getMovementRequest();
		
		MovementComponent movementComp = entityToMove.getComponent(MovementComponent.class);
		
		double xDir = movementRequest.isRight()? 1d: (movementRequest.isLeft()? -1d:0d);
		double yDir = movementRequest.isDown()? 1d: (movementRequest.isUp()? -1d:0d);
		
		// Apply friction when not moving.
		double ax = /*((xDir == 0? 1d/2d:1d) */ movementComp.getAx();
		double ay = /*(yDir == 0? 1d/2d:1d) */ movementComp.getAy();
		
		movementComp.setAx(ax + (xDir * JERK));
		movementComp.setAy(ay + (yDir * JERK));
		
	}
	
}
