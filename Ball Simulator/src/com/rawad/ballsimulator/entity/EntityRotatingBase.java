package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;

import com.rawad.ballsimulator.world.World;

public class EntityRotatingBase extends EntityMovingBase {
	
	protected double theta;
	
	public EntityRotatingBase(World world, double maxHealth, double health, double regen, boolean canRegen) {
		super(world, maxHealth, health, regen, canRegen);
		
	
	}
	
	public EntityRotatingBase(World world) {
		super(world);
		
	}
	
	@Override
	public void update(long timePassed) {
		super.update(timePassed);
		
		theta += vx * 0.01d;
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		g.rotate(theta, getX(), getY());
		
	}
	
}
