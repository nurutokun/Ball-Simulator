package com.rawad.ballsimulator.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rawad.ballsimulator.input.MouseInput;
import com.rawad.ballsimulator.world.World;

public class EntityPlayer extends EntityMovingBase {
	
	public EntityPlayer(World world) {
		super(world);
		
		x = 200;
		y = 200;
		
		width = 40;
		height = 40;
		
	}
	
	@Override
	public void update(long timePassed) {
		super.update(timePassed);
		
		updateHitbox(x - (width/2), y - (height/2), width, height);
		
		if(intersects(MouseInput.getX(), MouseInput.getY()) && MouseInput.isButtonDown(MouseInput.LEFT_MOUSE_BUTTON)) {
			this.hit(0.2d);
			
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		g.setColor(Color.RED);
		g.fillOval(hitbox.x, hitbox.y, width, height);
		
	}
	
}
