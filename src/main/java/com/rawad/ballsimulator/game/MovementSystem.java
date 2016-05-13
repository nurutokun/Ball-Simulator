package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;

public class MovementSystem extends GameSystem {
	
	private static final double JERK = 0.7d;// 0.1d
	
	private static final double MAX_ACCEL = JERK * 2d;// 2.0d
	private static final double MIN_ACCEL = 0.1d;// 0.01d
	private static final double MIN_VELOCITY = 0.4d;// 0.4d
	
	private static final double FRICTION = 0.1d;
	
	public MovementSystem() {
		super();
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(MovementComponent.class);
		
	}
	
	@Override
	public void tick(Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		MovementComponent movingComp = e.getComponent(MovementComponent.class);
		
		double ay = movingComp.getAy();
		
		boolean up = movingComp.isUp();
		boolean down = movingComp.isDown();
		
		if(up) {
			ay -= JERK;
		} else if(down) {
			ay += JERK;
		} else {
			ay /= 2;
		}
		
		if(Math.abs(ay) > MAX_ACCEL) {
			ay = up? -MAX_ACCEL: down? MAX_ACCEL:ay;
		}
		
		double ax = movingComp.getAx();
		
		boolean right = movingComp.isRight();
		boolean left = movingComp.isLeft();
		
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
		
		double vx = movingComp.getVx();
		double vy = movingComp.getVy();
		
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
		
		CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
		
		if(collisionComp != null && collisionComp.getCollidingWithEntity().get() != null) {
			
			boolean collidingUp = collisionComp.isCollidingUp();
			boolean collidingDown = collisionComp.isCollidingDown();
			boolean collidingRight = collisionComp.isCollidingRight();
			boolean collidingLeft = collisionComp.isCollidingLeft();
			
			if(collidingUp || collidingDown) {
				newX = transformComp.getX() - vx;
				
				ax /= 2;
				vx = -vx*3/4;
				
			}
			
			if(collidingRight || collidingLeft) {
				newY = transformComp.getY() - vy;
				
				ay /= 2;
				vy = -vy*3/4;
				
			}
			
//			if(collidingUp || collidingDown || collidingRight || collidingLeft) 
//				collisionComp.getCollidingWithEntity().set(null);
			
//			collisionComp.setCollidingUp(false);
//			collisionComp.setCollidingDown(false);
//			collisionComp.setCollidingRight(false);
//			collisionComp.setCollidingLeft(false);
			
			collisionComp.getHitbox().setX(newX);
			collisionComp.getHitbox().setY(newY);
			
		}
		
		movingComp.setAy(ay);
		movingComp.setAx(ax);
		
		movingComp.setVx(vx);
		movingComp.setVy(vy);
		
		transformComp.setX(newX);
		transformComp.setY(newY);
		
	}
	
}
