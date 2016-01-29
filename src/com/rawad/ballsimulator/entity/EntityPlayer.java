package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.input.Mouse;
import com.rawad.gamehelpers.resources.ResourceManager;

public class EntityPlayer extends EntityRotatingBase {
	
	private static final int DEFAULT_TEXTURE;
	
	private int texture; 
	
	public EntityPlayer(World world) {
		super(world);
		
		x = 0;
		y = 0;
		
		setTexture(DEFAULT_TEXTURE);
	}
	
	static {
		
		Game game = GameManager.instance().getCurrentGame();
		
		DEFAULT_TEXTURE = game.getLoader(game.toString()).loadTexture("entity", "player");
		
	}
	
	public void update(long timePassed, int x, int y) {
		super.update(timePassed);
		
		if(intersects(x, y) && Mouse.isButtonDown(Mouse.LEFT_MOUSE_BUTTON)) {
			this.hit(0.2d);
			
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		BufferedImage image = ResourceManager.getTexture(texture);
		
		int x = (int) (getX() - (width/2d));
		int y = (int) (getY() - (height/2d));
		
		g.rotate(theta, getX(), getY());
		
		g.drawImage(image, x, y, width, height, null);
		
		g.rotate(-theta, getX(), getY());
		
		super.renderHealthBar(g);
		
		if(GameManager.instance().getCurrentGame().isDebug()) {
			super.renderHitbox(g);
			super.renderVector(g);
		}
		
	}
	
	public void setTexture(int texture) {
		this.texture = texture;
		
		BufferedImage tex = ResourceManager.getTexture(texture);
		
		width = tex.getWidth();
		height = tex.getHeight();
		
	}
	
}
