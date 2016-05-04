package com.rawad.ballsimulator.game;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.MovingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

public class PhysicsSystem extends GameSystem {
	
	private static final double JERK = 0.7d;// 0.1d
	
	private static final double MAX_ACCEL = JERK * 2d;// 2.0d
	private static final double MIN_ACCEL = 0.1d;// 0.01d
	private static final double MIN_VELOCITY = 0.4d;// 0.4d
	
	private static final double FRICTION = 0.99d;
	
	private double maxWidth;
	private double maxHeight;
	
	public PhysicsSystem(double maxWidth, double maxHeight) {
		super();
		
		compatibleComponentTypes.add(TransformComponent.class);
		compatibleComponentTypes.add(MovingComponent.class);
		compatibleComponentTypes.add(CollisionComponent.class);
		
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		
	}
	
	@Override
	public void tick(Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		MovingComponent movingComp = e.getComponent(MovingComponent.class);
		CollisionComponent collisionComp = e.getComponent(CollisionComponent.class);
		
		move(movingComp);
		
		// Test for collision...
		checkCollision(e, transformComp, movingComp, collisionComp);
		
		if(collisionComp.getCollidingWithEntity().get() == null) {// No collision.
		} else {
			stopMoving(movingComp);
		}
		
		transformComp.setX(transformComp.getX() + movingComp.getVx());
		transformComp.setY(transformComp.getY() + movingComp.getVy());
		
	}
	
	private void move(MovingComponent movingComp) {
		
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
		
		if(Math.abs(ay) > 2) {
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
		
		if(Math.abs(ax) > 2) {
			ax = left? -MAX_ACCEL: right? MAX_ACCEL:ax;
		}
		
		double vx = movingComp.getVx();
		double vy = movingComp.getVy();
		
		vx += ax;//Math.sqrt(Math.pow(vx, 2) + (ax));
		vy += ay;//Math.sqrt(Math.pow(vy, 2) + (ay));
		
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
		
		if(Math.abs(ax) < MIN_ACCEL) {
			ax = 0;
		}
		
		if(Math.abs(ay) < MIN_ACCEL) {
			ay = 0;
		}
		
		movingComp.setAy(ay);
		movingComp.setAx(ax);
		
		movingComp.setVx(vx);
		movingComp.setVy(vy);
		
	}
	
	private void stopMoving(MovingComponent movingComp) {
		
	}
	
	private void checkCollision(Entity currentEntity, TransformComponent transformComp, MovingComponent movingComp, 
			CollisionComponent collisionComp) {
		
		double x = transformComp.getX();
		double y = transformComp.getY();
		
		double ax = movingComp.getAx();
		double ay = movingComp.getAy();
		
		double vx = movingComp.getVx();
		double vy = movingComp.getVy();
		
		double newX = x + vx;
		double newY = y + vy;
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		double widthOffset = (hitbox.getWidth()/2);
		double heightOffset = (hitbox.getHeight()/2);
		
		// X-Component
		Rectangle tempHitbox = new Rectangle(newX - widthOffset, y - heightOffset, hitbox.getWidth(), hitbox.getHeight());
		
		if(newX - widthOffset <= 0) {
			ax /= 2;
			vx = -vx*3/4;
			
			newX = widthOffset;
			
		} else if(newX + widthOffset >= maxWidth) {
			ax /= 2;
			vx = -vx*3/4;
			
			newX = maxWidth - widthOffset;
			
		}
		
		if(isColliding(compatibleEntities, currentEntity, collisionComp)) {
			vx = -vx*3/4;
			x += vx;
			
			newX = x;
		}
		
		// end X-Component
		
		// Y-Component
		tempHitbox.setX(x - widthOffset);
		tempHitbox.setY(newY - heightOffset);
		
		if(newY - heightOffset <= 0) {
			ay /= 2;
			vy = -vy*3/4;
			
			newY = heightOffset;
		} else if(newY + heightOffset >= maxHeight) {
			ay /= 2;
			vy = -vy*3/4;
			
			newY = maxHeight - heightOffset;
			
		}
		
		if(isColliding(compatibleEntities, currentEntity, collisionComp)) {
			vy = -vy*3/4;
			y += vy;
			
			newY = y;
		}
		
		transformComp.setX(newX);
		transformComp.setY(newY);
		
		movingComp.setAx(ax);
		movingComp.setAy(ay);
		movingComp.setVx(vx);
		movingComp.setVy(vy);
		
	}
	
	public static boolean isColliding(ArrayList<Entity> compatibleEntities, Entity currentEntity, 
			CollisionComponent collisionComp) {
		
		Rectangle hitbox = collisionComp.getHitbox();
		
		for(Entity e: compatibleEntities) {
			
			if(!currentEntity.equals(e) && hitbox.intersects(e.getComponent(CollisionComponent.class).getHitbox())) {
				return true;
			}
			
		}
		
		return false;
		
	}
	
}
