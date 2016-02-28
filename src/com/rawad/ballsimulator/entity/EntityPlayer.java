package com.rawad.ballsimulator.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.resources.TextureResource;

public class EntityPlayer extends EntityRotatingBase {
	
	private static int DEFAULT_TEXTURE;
	
	private int texture;
	
	public EntityPlayer(World world) {
		super(world);
		
		x = 0;
		y = 0;
		
		texture = DEFAULT_TEXTURE;
		
		TextureResource tex = ResourceManager.getTextureObject(texture);
		
		width = tex.getTexture().getWidth();
		height = tex.getTexture().getHeight();
		
	}
	
	public static void registerTextures(CustomLoader loader) {
		
		DEFAULT_TEXTURE = loader.registerTexture("entity", "player");
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		BufferedImage image = ResourceManager.getTexture(texture);
		
		double x = getX() - ((double) width/2d);
		double y = getY() - ((double) height/2d);
		
		g.rotate(theta, getX(), getY());
		
		g.translate(x, y);
		
		g.drawImage(image, 0, 0, width, height, null);
		
		g.translate(-x, -y);
		
		g.rotate(-theta, getX(), getY());
		
		super.renderHealthBar(g);
		
		if(GameManager.instance().getCurrentGame().isDebug()) {
			super.renderHitbox(g);
			super.renderVector(g);
		}
		
	}
	
}
