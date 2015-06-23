package com.rawad.ballsimulator.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.world.World;

public class Player extends Entity {
	
	private double ax;
	private double ay;
	
	private double vx;
	private double vy;
	
	private boolean up;
	private boolean down;
	private boolean right;
	private boolean left;
	
	public Player(World world) {
		super(world);
		
	}
	
	@Override
	public void update(long timePassed) {
		
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
			ay /= 2d;
		}
		
		if(Math.abs(ax) > 2) {
			ax = left? -2: right? 2:ax;
		}
		
		double finalAccelX = ax * delta;
		double finalAccelY = ay * delta;
		
		vx += finalAccelX;//Math.sqrt(Math.pow(vx, 2) + (ax));
		vy += finalAccelY;//Math.sqrt(Math.pow(vy, 2) + (ay));
		
		x += vx;
		y += vy;
		
//		double decrement = 0.1;
		
		if(ax != 0) {
			ax /= 2d;
//			ax = ax < 0? ax + decrement:ax - decrement;
		}
		
		if(ay != 0) {
			ay /= 2d;
//			ay = ay < 0? ay + decrement: ay - decrement;
		}
		
		if(Math.abs(ax) < 0.05) {
			ax = 0;
		}
		
		if(Math.abs(ay) < 0.05) {
			ay = 0;
		}
		
		if(Math.abs(vx) < 0.05) {
			vx = 0;
		}
		
		if(Math.abs(vy) < 0.05) {
			vy = 0;
		}
		
		if(x < 0) {
			ax /= 2;
			x = 0;
			vx = -vx/2;
		} else if(x > DisplayManager.getScreenWidth()) {
			ax /= 2;
			x = DisplayManager.getScreenWidth();
			vx = -vx/2;
		}
		
		if(y < 0) {
			ay /= 2;
			y = 0;
			vy = -vy;
		} else if(y > DisplayManager.getScreenHeight()) {
			ay /= 2;
			y = DisplayManager.getScreenHeight();
			vy = -vy;
		}
		
		up = false;
		down = false;
		right = false;
		left = false;
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		g.setColor(Color.RED);
		g.fillOval((int) x - 20, (int) y - 20, 40, 40);
		
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
	
}
