package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.resources.ResourceManager;

public class EntityPlayer extends EntityRotatingBase {
	
	private final int textureLoc; 
	
	public EntityPlayer(World world) {
		super(world);
		
		x = 20;
		y = 20;
		
		width = 40;
		height = 40;
		
		textureLoc = ResourceManager.loadTexture("res/entity_textures/player.png");
		
	}
	
	public void update(long timePassed, MouseEvent e) {
		super.update(timePassed);
		
		if(intersects(e.getX(), e.getY()) && e.isButtonDown()) {
			this.hit(0.2d);
			
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		BufferedImage image = ResourceManager.getTexture(textureLoc);
		
		int x = (int) (getX() - (width/2));
		int y = (int) (getY() - (height/2));
		
		g.drawImage(image.getScaledInstance(width, height, BufferedImage.SCALE_FAST), x, y, null);
		
	}
	
}
