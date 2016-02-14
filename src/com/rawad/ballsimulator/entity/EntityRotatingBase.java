package com.rawad.ballsimulator.entity;

import com.rawad.ballsimulator.world.World;

public abstract class EntityRotatingBase extends EntityMovingBase {
	
	protected double theta;
	
	public EntityRotatingBase(World world, double maxHealth, double health, double regen, boolean canRegen) {
		super(world, maxHealth, health, regen, canRegen);
		
	
	}
	
	public EntityRotatingBase(World world) {
		super(world);
		
	}
	
	@Override
	public void updateHitbox() {
		super.updateHitbox();
		
		updateTheta();
		
	}
	
	private void updateTheta() {
		
		theta += vx * 0.01d;
		
	}
	
	public void setTheta(double theta) {
		this.theta = theta;
	}
	
	public double getTheta() {
		return theta;
	}
	
}
