package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.input.MouseEvent;
import com.rawad.gamehelpers.resources.ResourceManager;

public class EntityPlayer extends EntityRotatingBase {
	
	private final int textureLoc; 
	
	public EntityPlayer(World world) {
		super(world);
		
		x = 200;
		y = 200;
		
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
		
//		g.setColor(Color.RED);
//		g.fillRect(hitbox.x, hitbox.y, width, height);
		
//		g.drawImage(texture, hitbox.x, hitbox.y, null);
		
		BufferedImage image = ResourceManager.getTexture(textureLoc);
		
		g.drawImage(image.getScaledInstance(width, height, BufferedImage.SCALE_FAST), hitbox.x, hitbox.y, null);
		
	}
	
}
