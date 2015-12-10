package com.rawad.ballsimulator.client.renderengine.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.renderengine.LayeredRender;

public abstract class EntityRender extends LayeredRender {
	
	public void renderHitbox(Graphics2D g, Rectangle hitbox) {
		
		if(GameManager.instance().getCurrentGame().isDebug()) {
			g.setColor(Color.BLACK);
			g.draw(hitbox);
		}
		
	}
	
}
