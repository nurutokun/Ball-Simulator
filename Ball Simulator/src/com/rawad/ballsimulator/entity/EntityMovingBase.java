package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.rawad.ballsimulator.world.World;

public abstract class EntityMovingBase extends EntityLivingBase {
	
	protected double ax;
	protected double ay;
	
	protected double vx;
	protected double vy;
	
	private boolean up;
	private boolean down;
	private boolean right;
	private boolean left;
	
	public EntityMovingBase(World world, double maxHealth, double health, double regen, boolean canRegen) {
		super(world, maxHealth, health, regen, canRegen);
		
	}
	
	public EntityMovingBase(World world) {
		super(world);
	}
	
	@Override
	public void update(long timePassed) {
		super.update(timePassed);
		
		double delta = timePassed/100d;
		
		if(up) {
			ay -= 0.1d;
		} else if(down) {
			ay += 0.1d;
		} else {
			ay /= 2d;
		}
		
		if(Math.abs(ay) > 2) {
			ay = up? -2: down? 2:ay;
		}
		
		if(right) {
			ax += 0.1d;
		} else if(left) {
			ax -= 0.1d;
		} else {
			ax /= 2d;
		}
		
		if(Math.abs(ax) > 2) {
			ax = left? -2: right? 2:ax;
		}
		
		double finalAccelX = ax * delta;
		double finalAccelY = ay * delta;
		
		vx += finalAccelX;//Math.sqrt(Math.pow(vx, 2) + (ax));
		vy += finalAccelY;//Math.sqrt(Math.pow(vy, 2) + (ay));
		
		double newX = x + vx;
		double newY = y + vy;
		
		int width = (this.width/2);
		int height = (this.height/2);
		
		Rectangle tempHitbox = new Rectangle((int) newX - width, (int) y - height, hitbox.width, hitbox.height);
		
		if(newX - width <= 0) {
			ax /= 2;
			newX = 0 + width;
			vx = -vx/2;
		} else if(newX + width >= world.getWidth()) {
			ax /= 2;
			newX = world.getWidth() - width;
			vx = -vx/2;
		}
		
		if(world.mapCollision(tempHitbox)) {
			newX = x;
			vx = -vx/2;
		}
		
		tempHitbox.setBounds((int) x - width, (int) newY - height, hitbox.width, hitbox.height);
		
		if(newY - height <= 0) {
			ay /= 2;
			newY = 0 + (height);
			vy = -vy/2;
		} else if(newY + height >= world.getHeight()) {
			ay /= 2;
			newY = world.getHeight() - height;
			vy = -vy/2;
		}
		
		if(world.mapCollision(tempHitbox)) {
			newY = y;
			vy = -vy/2;
		}
		
		x = newX;
		y = newY;
		
//		double decrement = 0.1;
		
		final double minVelocity = 0.4;
		
		if(ax == 0) {
//			ax /= 2d;
			vx /= 1.01d;
//			ax = ax < 0? ax + decrement:ax - decrement;
			
			if(Math.abs(vx) < minVelocity) {
				vx = 0;
			}
			
		}
		
		if(ay == 0) {
//			ay /= 2d;
			vy /= 1.01d;
//			ay = ay < 0? ay + decrement: ay - decrement;
			
			if(Math.abs(vy) < minVelocity) {
				vy = 0;
			}
			
		}
		
		final double minAccel = 0.01;
		
		if(Math.abs(ax) < minAccel) {
			ax = 0;
		}
		
		if(Math.abs(ay) < minAccel) {
			ay = 0;
		}
		
		updateHitbox();
		
		// stopMoving(); when using single-frame key testing.
		stopMoving();
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
	}
	
	public void stopMoving() {
		
		up = false;
		down = false;
		right = false;
		left = false;
		
	}
	
	public void moveUp() {
		up = true;
		down = false;
	}
	
	public void moveDown() {
		down = true;
		up = false;
	}
	
	public void moveRight() {
		right = true;
		left = false;
	}
	
	public void moveLeft() {
		left = true;
		right = false;
	}
	
	protected double getAx() {
		return ax;
	}
	
	protected void setAx(double ax) {
		this.ax = ax;
	}
	
	protected double getAy() {
		return ay;
	}
	
	protected void setAy(double ay) {
		this.ay = ay;
	}
	
	protected double getVx() {
		return vx;
	}
	
	protected void setVx(double vx) {
		this.vx = vx;
	}
	
	protected double getVy() {
		return vy;
	}
	
	protected void setVy(double vy) {
		this.vy = vy;
	}
	
}
