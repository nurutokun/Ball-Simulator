package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.geometry.Point;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Gives {@code Entiti} a position, scale, and a rotation in 2D space. of the {@code World}.
 * 
 * @author Rawad
 *
 */
public class TransformComponent extends Component {
	
	private double x = 0;
	private double y = 0;
	
	private SimpleDoubleProperty scaleX;
	private SimpleDoubleProperty scaleY;
	
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
	
	public SimpleDoubleProperty scaleXProperty() {
		if(scaleX == null) scaleX = new SimpleDoubleProperty(1);
		return scaleX;
	}
	
	/**
	 * @return the scaleX
	 */
	public double getScaleX() {
		return scaleXProperty().get();
	}
	
	/**
	 * @param scaleX the scaleX to set
	 */
	public void setScaleX(double scaleX) {
		scaleXProperty().set(scaleX);
	}
	
	public SimpleDoubleProperty scaleYProperty() {
		if(scaleY == null) scaleY = new SimpleDoubleProperty(1);
		return scaleY;
	}
	
	/**
	 * @return the scaleY
	 */
	public double getScaleY() {
		return scaleYProperty().get();
	}
	
	/**
	 * @param scaleY the scaleY to set
	 */
	public void setScaleY(double scaleY) {
		scaleYProperty().set(scaleY);
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
	
	/**
	 * 
	 * Converts the given {@code x} and {@code y} coordinates for a point on the screen and converts it to a point in this
	 * {@code Transform}'s space.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Point transformFromScreen(double x, double y) {
		
		Point pointInWorld = new Point(x, y);
		
		try {
			pointInWorld.setX(x / getScaleX() + getX());
			pointInWorld.setY(y /  getScaleY() + getY());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return pointInWorld;
		
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof TransformComponent) {
			
			TransformComponent transformComp = (TransformComponent) comp;
			
			
			transformComp.setX(getX());
			transformComp.setY(getY());
			
			transformComp.setScaleX(getScaleX());
			transformComp.setScaleY(getScaleY());
			
			transformComp.setMaxScaleX(getMaxScaleX());
			transformComp.setMaxScaleY(getMaxScaleY());
			
			transformComp.setTheta(getTheta());
			
			return transformComp;
			
		}
		
		return comp;
		
	}
	
}
