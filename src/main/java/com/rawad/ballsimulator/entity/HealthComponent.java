package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;

public class HealthComponent extends Component {
	
	private double health = 1d;
	private double maxHealth = 1d;
	
	private double regenRate = 0d;
	
	private boolean regen = false;
	
	/**
	 * @return the health
	 */
	public double getHealth() {
		return health;
	}
	
	/**
	 * @param health the health to set
	 */
	public void setHealth(double health) {
		this.health = health;
	}
	
	/**
	 * @return the maxHealth
	 */
	public double getMaxHealth() {
		return maxHealth;
	}
	
	/**
	 * @param maxHealth the maxHealth to set
	 */
	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	/**
	 * @return the regenRate
	 */
	public double getRegenRate() {
		return regenRate;
	}
	
	/**
	 * @param regenRate the regenRate to set
	 */
	public void setRegenRate(double regenRate) {
		this.regenRate = regenRate;
	}
	
	/**
	 * @return the regen
	 */
	public boolean isRegen() {
		return regen;
	}
	
	/**
	 * @param regen the regen to set
	 */
	public void setRegen(boolean regen) {
		this.regen = regen;
	}
	
	/*/bad -->public boolean isAlive() {
		if(health <= 0) return false;
		return true;
	}/**/
	
}
