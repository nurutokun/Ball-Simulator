package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;

public class MovingComponent extends Component {
	
	private double ax;
	private double ay;
	
	private double vx;
	private double vy;
	
	private boolean up;
	private boolean down;
	private boolean right;
	private boolean left;
	
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
	
	/**
	 * @return the up
	 */
	public boolean isUp() {
		return up;
	}
	
	/**
	 * @param up the up to set
	 */
	public void setUp(boolean up) {
		this.up = up;
	}
	
	/**
	 * @return the down
	 */
	public boolean isDown() {
		return down;
	}
	
	/**
	 * @param down the down to set
	 */
	public void setDown(boolean down) {
		this.down = down;
	}
	
	/**
	 * @return the right
	 */
	public boolean isRight() {
		return right;
	}
	
	/**
	 * @param right the right to set
	 */
	public void setRight(boolean right) {
		this.right = right;
	}
	
	/**
	 * @return the left
	 */
	public boolean isLeft() {
		return left;
	}
	
	/**
	 * @param left the left to set
	 */
	public void setLeft(boolean left) {
		this.left = left;
	}
	
}
