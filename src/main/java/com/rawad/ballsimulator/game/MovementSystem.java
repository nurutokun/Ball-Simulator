package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.entity.Listener;

public class MovementSystem extends GameSystem implements Listener<CollisionComponent> {
	
	private static final double JERK = 0.7d;// 0.1d
	
	private static final double MAX_ACCEL = JERK * 2d;// 2.0d
	private static final double MIN_ACCEL = 0.1d;// 0.01d
	private static final double MIN_VELOCITY = 0.4d;// 0.4d
	
	private static final double FRICTION = 0.9d;
	
	public MovementSystem() {
		super();
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(MovementComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		MovementComponent movementComp = e.getComponent(MovementComponent.class);
		
		double ay = movementComp.getAy();
		
		boolean up = movementComp.isUp();
		boolean down = movementComp.isDown();
		
		if(up) {
			ay -= JERK;
		} else if(down) {
			ay += JERK;
		} else {
			ay /= 2d;
		}
		
		if(Math.abs(ay) > MAX_ACCEL) {
			ay = up? -MAX_ACCEL: down? MAX_ACCEL:ay;
		}
		
		double ax = movementComp.getAx();
		
		boolean right = movementComp.isRight();
		boolean left = movementComp.isLeft();
		
		if(right) {
			ax += JERK;
		} else if(left) {
			ax -= JERK;
		} else {
			ax /= 2d;
		}
		
		if(Math.abs(ax) > MAX_ACCEL) {
			ax = left? -MAX_ACCEL: right? MAX_ACCEL:ax;
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
		
		double newX = transformComp.getX() + vx;
		double newY = transformComp.getY() + vy;
		
		movementComp.setAy(ay);
		movementComp.setAx(ax);
		
		movementComp.setVx(vx);
		movementComp.setVy(vy);
		
		transformComp.setX(newX);
		transformComp.setY(newY);
		
	}
	
	@Override
	public void onEvent(Entity e, CollisionComponent collisionComp) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		MovementComponent movementComp = e.getComponent(MovementComponent.class);
		
		double ax = movementComp.getAx();
		double ay = movementComp.getAy();
		
		double vx = movementComp.getVx();
		double vy = movementComp.getVy();
		
		double newX = transformComp.getX();
		double newY = transformComp.getY();
		
		if(collisionComp.isCollideX()) {
			
			newX -= vx;
			
			ax /= 2d;
			vx = -vx*3/4;
			
		}
		
		if(collisionComp.isCollideY()) {
			
			newY -= vy;
			
			ay /= 2d;
			vy = -vy*3d/4d;
			
		}
		
		movementComp.setAy(ay);
		movementComp.setAx(ax);
		
		movementComp.setVx(vx);
		movementComp.setVy(vy);
		
		transformComp.setX(newX);
		transformComp.setY(newY);
		
	}
	
}
