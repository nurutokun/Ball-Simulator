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
		
		Rectangle tempHitbox = new Rectangle((int) newX, (int) y, getHitbox().width, getHitbox().height);
		
		if(world.mapCollision(tempHitbox)) {
			newX = x;
		}
		
		tempHitbox.setBounds((int) x, (int) newY, getHitbox().width, getHitbox().height);
		
		if(world.mapCollision(tempHitbox)) {
			newY = y;
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
			
		if(x < 0) {
			ax /= 2;
			x = 0;
			vx = -vx/2;
		} else if(x > world.getWidth()) {
			ax /= 2;
			x = world.getWidth();
			vx = -vx/2;
		}
		
		if(y < 0) {
			ay /= 2;
			y = 0;
			vy = -vy/2;
		} else if(y > world.getHeight()) {
			ay /= 2;
			y = world.getHeight();
			vy = -vy/2;
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
