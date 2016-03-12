package com.rawad.ballsimulator.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.rawad.ballsimulator.world.World;

public abstract class EntityMovingBase extends EntityLivingBase {
	
	// Make all of these relative to single game tick (~200 ms)
	private static final double JERK = 0.7d;// 0.1d
	
	private static final double MAX_ACCEL = JERK * 2;// 2.0d
	private static final double MIN_ACCEL = 0.1d;// 0.01d
	private static final double MIN_VELOCITY = 0.4d;// 0.4d
	
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
	public void update() {
		super.update();
		
		if(up) {
			ay -= JERK;
		} else if(down) {
			ay += JERK;
		} else {
			ay /= 2d;
		}
		
		if(Math.abs(ay) > 2) {
			ay = up? -MAX_ACCEL: down? MAX_ACCEL:ay;
		}
		
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
		
		vx += ax;//Math.sqrt(Math.pow(vx, 2) + (ax));
		vy += ay;//Math.sqrt(Math.pow(vy, 2) + (ay));
		
		double newX = x + vx;
		double newY = y + vy;
		
		int width = (this.width/2);
		int height = (this.height/2);
		
		// X-Component
		Rectangle tempHitbox = new Rectangle((int) (newX - width), (int) (y - height), hitbox.width, hitbox.height);
		
		if(newX - width <= 0) {
			ax /= 2;
			vx = -vx*3/4;
			
			newX = width;
			
		} else if(newX + width >= world.getWidth()) {
			ax /= 2;
			vx = -vx*3/4;
			
			newX = world.getWidth() - width;
			
		}
		
		if(world.mapCollision(tempHitbox)) {
			
			vx = -vx*3/4;
			x += vx;
			
			newX = x;
			
		}
		// end X-Component
		
		// Y-Component
		tempHitbox.setBounds((int) (x - width), (int) (newY - height), hitbox.width, hitbox.height);
		
		if(newY - height <= 0) {
			ay /= 2;
			vy = -vy*3/4;
			
			newY = height;
		} else if(newY + height >= world.getHeight()) {
			ay /= 2;
			vy = -vy*3/4;
			
			newY = world.getHeight() - height;
			
		}
		
		if(world.mapCollision(tempHitbox)) {
			
			vy = -vy*3/4;
			y += vy;
			
			newY = y;
		}
		// end Y-Component
		
		x = newX;
		y = newY;
		
		if(ax == 0) {
			vx /= 1.01d;
			
			if(Math.abs(vx) < MIN_VELOCITY) {
				vx = 0;
			}
			
		}
		
		if(ay == 0) {
			vy /= 1.01d;
			
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
		
		updateHitbox();
		
		// stopMoving(); when using single-frame key testing.
		stopMoving();
		
	}
	
	public void renderVector(Graphics2D g) {
		
		g.setColor(Color.GREEN);
		g.drawLine((int) getX(), (int) getY(), (int) (vx * 3 + getX()), (int) (vy * 3 + getY()));
		
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
	
	public double getAx() {
		return ax;
	}
	
	public void setAx(double ax) {
		this.ax = ax;
	}
	
	public double getAy() {
		return ay;
	}
	
	public void setAy(double ay) {
		this.ay = ay;
	}
	
	public double getVx() {
		return vx;
	}
	
	public void setVx(double vx) {
		this.vx = vx;
	}
	
	public double getVy() {
		return vy;
	}
	
	public void setVy(double vy) {
		this.vy = vy;
	}
	
}
