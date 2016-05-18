package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;

/**
 * Gives {@code Entiti} a position, scale, and a rotation in 2D space. of the {@code World}.
 * 
 * @author Rawad
 *
 */
public class TransformComponent extends Component {
	
	private double x = 0;
	private double y = 0;
	
	private double scaleX = 1;
	private double scaleY = 1;
	
	/** Maximum an {@code Entity} can be scaled (i.e. made smaller) in the x-direction. */
	private double maxScaleX = Double.MAX_VALUE;
	/** Maximum an {@code Entity} can be scaled (i.e. made smaller) in the y-direction. */
	private double maxScaleY = Double.MAX_VALUE;
	
	/** In degrees. */
	private double theta = 0;
	
	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}	
	
	/**
	 * @return the scaleX
	 */
	public double getScaleX() {
		return scaleX;
	}
	
	/**
	 * @param scaleX the scaleX to set
	 */
	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}
	
	/**
	 * @return the scaleY
	 */
	public double getScaleY() {
		return scaleY;
	}
	
	/**
	 * @param scaleY the scaleY to set
	 */
	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}	
	
	/**
	 * @return the maxScaleX
	 */
	public double getMaxScaleX() {
		return maxScaleX;
	}
	
	/**
	 * @param maxScaleX the maxScaleX to set
	 */
	public void setMaxScaleX(double maxScaleX) {
		this.maxScaleX = maxScaleX;
	}
	
	/**
	 * @return the maxScaleY
	 */
	public double getMaxScaleY() {
		return maxScaleY;
	}
	
	/**
	 * @param maxScaleY the maxScaleY to set
	 */
	public void setMaxScaleY(double maxScaleY) {
		this.maxScaleY = maxScaleY;
	}
	
	/**
	 * @return the theta
	 */
	public double getTheta() {
		return theta;
	}
	
	/**
	 * @param theta the theta to set
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}
	
}
