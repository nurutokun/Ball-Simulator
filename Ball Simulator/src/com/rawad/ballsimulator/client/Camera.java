package com.rawad.ballsimulator.client;

import com.rawad.ballsimulator.entity.Entity;
import com.rawad.gamehelpers.display.DisplayManager;

/**
 * DisplayManager-specific or Ball Simulator-specific?
 * 
 * @author Rawad
 */
public class Camera {
	
	private Entity trackedEntity;
	
	private double x;
	private double y;
	
	// For zooming
	private double xScale;
	private double yScale;
	
	private double theta;
	
	public Camera(Entity trackedEntity) {
		this.trackedEntity = trackedEntity;
		
		x = 0;
		y = 0;
		
		xScale = 1d;
		yScale = 1d;
		
		theta = 0d;
		
	}
	
	public void update(double x, double y, int viewportWidth, int viewportHeight) {
		
		this.x = x - (viewportWidth/2);
		this.y = y - (viewportHeight/2);
		
	}
	
	public void update() {
		update(trackedEntity.getX(), trackedEntity.getY(), DisplayManager.getScreenWidth(), 
				DisplayManager.getScreenHeight());
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
	
	public void setTrackedEntity(Entity trackedEntity) {
		this.trackedEntity = trackedEntity;
	}
	
	public Entity getTrackedEntity() {
		return this.trackedEntity;
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
