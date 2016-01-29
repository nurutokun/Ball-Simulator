package com.rawad.ballsimulator.client;


/**
 * DisplayManager-specific or Ball Simulator-specific?
 * 
 * @author Rawad
 */
public class Camera {
	
	private double x;
	private double y;
	
	// For zooming
	private double xScale;
	private double yScale;
	
	private double theta;
	
	public Camera() {
		
		x = 0;
		y = 0;
		
		xScale = 1d;
		yScale = 1d;
		
		theta = 0d;
		
	}
	
	// TODO: Figure out all of these methods... Make them work for world editor too (for that, should make limit
	// equal to one screen's width (maxWidth) on every side and corner).
	public void update(double x, double y, int worldWidth, int worldHeight, 
			int minX, int minY, int viewWidth, int viewHeight) {
		
		viewWidth /= xScale;
		viewHeight /= yScale;
		
		minX /= xScale;
		minY /= yScale;
		
		x -= (viewWidth/2d);
		y -= (viewHeight/2d);
		
		worldWidth -= viewWidth;
		worldHeight -= viewHeight;
		
		setX(x, minX, worldWidth);
		setY(y, minY, worldHeight);
		
	}
	
	private void setX(double x, int minX, int worldWidth) {
		
		if(x < minX) {
			x = minX;
			
		} else if(x > worldWidth) {
			x = worldWidth;
			
		}
		
		this.x = x;
		
	}
	
	private void setY(double y, int minY, int worldHeight) {
		
		if(y < minY) {
			y = minY;
			
		} else if(y > worldHeight) {
			y = worldHeight;
			
		}
		
		this.y = y;
		
	}
	public void increaseRotation(double delta) {
		theta += delta;
	}
	
	public void setTheta(double theta) {
		this.theta = theta;
	}
	
	public double getTheta() {
		return theta;
	}
	
	public void setScale(double xScale, double yScale) {
		this.xScale = xScale;
		this.yScale = yScale;
	}
	
	public double getXScale() {
		return xScale;
	}
	
	public double getYScale() {
		return yScale;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
}
