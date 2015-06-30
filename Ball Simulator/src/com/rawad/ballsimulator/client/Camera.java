package com.rawad.ballsimulator.client;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.entity.Entity;

/**
 * DisplayManager-specific or Ball Simulator-specific?
 * 
 * @author Rawad
 */
public class Camera {
	
	private Entity trackedEntity;
	
	private int x;
	private int y;
	
	// For zooming
	private double xScale;
	private double yScale;
	
	public Camera(Entity trackedEntity) {
		this.trackedEntity = trackedEntity;
		
		xScale = 1d;
		yScale = 1d;
		
	}
	
	public void update(int x, int y, int width, int height) {
		
		this.x = x - (width/2); //+ (playerHitbox.width/2);
		this.y = y - (height/2); //+ (playerHitbox.height*5/2);
		
	}
	
	public void update() {
		update((int) trackedEntity.getX(), (int) trackedEntity.getY(), DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
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
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
}
