package com.rawad.ballsimulator.client.renderengine.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rawad.ballsimulator.entity.EntityLivingBase;

public abstract class LivingBaseRender extends EntityRender {
	
	public LivingBaseRender() {
		
	}
	
	public void renderHealthBar(Graphics2D g, EntityLivingBase entity) {
		
		double width = entity.getWidth();
		double height = 10;
		
		double x = entity.getX() - (width/2);
		double y = entity.getY() - (entity.getHeight()/2) - height;
		
		g.setColor(Color.BLACK);
		g.draw(entity.getHitbox());
		
		g.fillRect((int) x, (int) y, (int) width, (int) height);
		
		width = (entity.getHealth()/entity.getMaxHealth()) * entity.getWidth();
		
		int offset = 6;
		
		g.setColor(Color.RED);
		g.fillRect((int) x + (offset/2), (int) y + (offset/2), (int) width - (offset), (int) height - (offset));
		
	}
	
}
