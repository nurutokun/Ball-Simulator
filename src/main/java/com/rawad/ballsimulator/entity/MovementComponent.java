package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;

public class MovementComponent extends Component {
	
	private double ax;
	private double ay;
	
	private double vx;
	private double vy;
	
	/**
	 * @return the ax
	 */
	public double getAx() {
		return ax;
	}
	
	/**
	 * @param ax the ax to set
	 */
	public void setAx(double ax) {
		this.ax = ax;
	}
	
	/**
	 * @return the ay
	 */
	public double getAy() {
		return ay;
	}
	
	/**
	 * @param ay the ay to set
	 */
	public void setAy(double ay) {
		this.ay = ay;
	}
	
	/**
	 * @return the vx
	 */
	public double getVx() {
		return vx;
	}
	
	/**
	 * @param vx the vx to set
	 */
	public void setVx(double vx) {
		this.vx = vx;
	}
	
	/**
	 * @return the vy
	 */
	public double getVy() {
		return vy;
	}
	
	/**
	 * @param vy the vy to set
	 */
	public void setVy(double vy) {
		this.vy = vy;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof MovementComponent) {
			
			MovementComponent movementComp = (MovementComponent) comp;
			
			movementComp.setAx(getAx());
			movementComp.setAy(getAy());
			
			movementComp.setVx(getVx());
			movementComp.setVy(getVy());
			
		}
		
		return comp;
	}
	
}
