package com.rawad.ballsimulator.world.terrain;

import java.awt.Color;
import java.awt.Graphics2D;

public class TerrainComponent {
	
	private double x;
	private double y;
	
	private int width;
	private int height;
	
	public TerrainComponent(double x, double y, int width, int height) {
		
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.RED);
		g.drawRect((int) x, (int) y, width, height);
	}
	
}
