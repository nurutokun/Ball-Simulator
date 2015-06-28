package com.rawad.ballsimulator.client;

import java.awt.Rectangle;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.entity.EntityPlayer;

/**
 * DisplayManager-specific or Ball Simulator-specific?
 * 
 * @author Rawad
 */
public class Camera {
	
	private EntityPlayer player;
	
	private int x;
	private int y;
	
	public Camera(EntityPlayer player) {
		this.player = player;
	}
	
	public void update() {
		
		Rectangle playerHitbox = player.getHitbox();
		
		this.x = playerHitbox.x - (DisplayManager.getScreenWidth()/2) + (playerHitbox.width/2);
		this.y = playerHitbox.y - (DisplayManager.getScreenWidth()/2) + (playerHitbox.height*5/2);
		
	}
	
	public void setPlayer(EntityPlayer player) {
		this.player = player;
	}
	
	public EntityPlayer getPlayer() {
		return this.player;
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
