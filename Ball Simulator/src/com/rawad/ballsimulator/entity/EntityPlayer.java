package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.resources.ResourceManager;

public class EntityPlayer extends EntityRotatingBase {
	
	private static final int DEFAULT_TEXTURE;
	
	private final int textureLoc; 
	
	public EntityPlayer(World world) {
		super(world);
		
		x = 20;
		y = 20;
		
		width = 40;
		height = 40;
		
		textureLoc = DEFAULT_TEXTURE;
		
	}
	
	static {
		
		Game game = GameManager.instance().getCurrentGame();
		
		DEFAULT_TEXTURE = game.getLoader(game.toString()).loadTexture("entity", "player");
		
	}
	
	public void update(long timePassed, MouseEvent e) {
		super.update(timePassed);
		
		if(intersects(e.getX(), e.getY()) && e.isLeftButtonDown()) {
			this.hit(0.2d);
			
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		BufferedImage image = ResourceManager.getTexture(textureLoc);
		
		int x = (int) (getX() - (width/2));
		int y = (int) (getY() - (height/2));
		
		g.rotate(theta, getX(), getY());
		
		g.drawImage(image.getScaledInstance(width, height, BufferedImage.SCALE_FAST), x, y, null);
		
		g.rotate(-theta, getX(), getY());
		
		super.renderHealthBar(g);
		
		if(GameManager.instance().getCurrentGame().isDebug()) {
			super.renderHitbox(g);
			super.renderVector(g);
		}
		
	}
	
}
