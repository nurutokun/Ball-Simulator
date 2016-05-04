package com.rawad.ballsimulator.entity;

import java.awt.Rectangle;

import com.rawad.ballsimulator.world.World;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class EntityMovingBase extends EntityLivingBase {
	
	public EntityMovingBase(World world, double maxHealth, double health, double regen, boolean canRegen) {
		super(world, maxHealth, health, regen, canRegen);
		
	}
	
	public void renderVector(GraphicsContext g) {
		
		g.setStroke(Color.GREEN);
		g.strokeLine(getX(), getY(), vx * 3 + getX(), vy * 3 + getY());
		
	}
	
}
