package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;

/**
 * Gives {@code Entiti} a position, scale, and a rotation in 2D space. of the {@code World}.
 * 
 * @author Rawad
 *
 */
public class TransformComponent extends Component {
	
	private double x;
	private double y;
	
	private double scale = 1d;
	
	/** In degrees. */
	private double theta;
	
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
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}
	
	/**
	 * @param scale the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
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
