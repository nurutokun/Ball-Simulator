package com.rawad.ballsimulator.entity;

import com.rawad.ballsimulator.world.World;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class EntityLivingBase extends Entity {
	
	protected double maxHealth;
	protected double health;
	
	protected double regen;
	
	protected boolean canRegen;
	
	public EntityLivingBase(World world, double maxHealth, double health, double regen, boolean canRegen) {
		super(world);
		
		this.maxHealth = maxHealth;
		this.health = health;
		
		this.regen = regen;
		
		this.canRegen = canRegen;
		
	}
	
	public EntityLivingBase(World world) {
		this(world, 20d, 20d, 0.1d, true);
	}
	
	@Override
	public void update() {
		
		if(canRegen && this.health < maxHealth) {
			double newHealth = this.health + regen;
			
			if(newHealth <= maxHealth) {
				this.health = newHealth;
				
			}
			
		}
		
	}
	
	public void renderHealthBar(GraphicsContext g) {
		
		double width = this.width;
		double height = 10;
		
		double x = this.x - (width/2);
		double y = this.y - (this.height/2) - height;
		
		g.setFill(Color.BLACK);
		g.fillRect((int) x, (int) y, (int) width, (int) height);
		
		width = (health/maxHealth) * this.width;
		
		int offset = 6;
		
		g.setFill(Color.RED);
		g.fillRect((int) x + (offset/2), (int) y + (offset/2), (int) width - (offset), (int) height - (offset));
		
	}
	
	public boolean isAlive() {
		return health <= 0;
	}
	
	public double getMaxHealth() {
		return maxHealth;
	}
	
	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public double getHealth() {
		return health;
	}
	
	public void setHealth(double health) {
		this.health = health;
	}
	
	public double getRegen() {
		return regen;
	}
	
	public void setRegen(double regen) {
		this.regen = regen;
	}
	
	public boolean isCanRegen() {
		return canRegen;
	}
	
	public void setCanRegen(boolean canRegen) {
		this.canRegen = canRegen;
	}
	
	public void hit(double damage) {
		if(this.health > 0) {
			this.health -= damage;
		}
	}
	
}
