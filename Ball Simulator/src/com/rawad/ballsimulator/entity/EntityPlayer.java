package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.rawad.ballsimulator.input.MouseInput;
import com.rawad.ballsimulator.world.World;

public class EntityPlayer extends EntityRotatingBase {
	
	private BufferedImage texture;
	
	public EntityPlayer(World world) {
		super(world);
		
		x = 200;
		y = 200;
		
		width = 40;
		height = 40;
		
		try {
			texture = ImageIO.read(new File("res/entity_textures/player.png"));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void update(long timePassed, int pointX, int pointY) {
		super.update(timePassed);
		
		if(intersects(pointX, pointY) && MouseInput.isButtonDown(MouseInput.LEFT_MOUSE_BUTTON)) {
			this.hit(0.2d);
			
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
//		g.setColor(Color.RED);
//		g.fillOval(hitbox.x, hitbox.y, width, height);
		
		g.drawImage(texture, hitbox.x, hitbox.y, null);
		
	}
	
}
