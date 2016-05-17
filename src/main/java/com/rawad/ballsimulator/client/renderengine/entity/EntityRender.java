package com.rawad.ballsimulator.client.renderengine.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.rawad.gamehelpers.client.renderengine.LayerRender;
import com.rawad.gamehelpers.game.GameManager;

public abstract class EntityRender extends LayerRender {
	
	public void renderHitbox(Graphics2D g, Rectangle hitbox) {
		
		if(GameManager.instance().getCurrentGame().isDebug()) {
			g.setColor(Color.BLACK);
			g.draw(hitbox);
		}
		
	}
	
}
