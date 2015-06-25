package com.rawad.ballsimulator.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rawad.ballsimulator.input.MouseInput;
import com.rawad.ballsimulator.world.World;

public class EntityPlayer extends EntityMovingBase {
	
	public EntityPlayer(World world) {
		super(world);
		
		this.width = 40;
		this.height = 40;
		
	}
	
	@Override
	public void update(long timePassed) {
		super.update(timePassed);
		
		if(intersects(MouseInput.getX(), MouseInput.getY()) && MouseInput.isButtonDown(MouseInput.LEFT_MOUSE_BUTTON)) {
			this.hit(0.2d);
			
		}
		
		updateHitbox(this.x - (this.width/2), this.y - (this.height/2), this.width, this.height);
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		g.setColor(Color.RED);
		g.fillOval((int) hitbox.x, (int) hitbox.y, width, height);
		
	}
	
}
