package com.rawad.ballsimulator.entity;

import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.resources.TextureResource;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class EntityPlayer extends EntityRotatingBase {
	
	private static int DEFAULT_TEXTURE;
	
	private static double width;
	private static double height;
	
	public EntityPlayer(World world) {
		super(world);
		
		x = 0;
		y = 0;
		
	}
	
	public static void registerTextures(CustomLoader loader) {
		
		DEFAULT_TEXTURE = loader.registerTexture("entity", "player");
		TextureResource tex = ResourceManager.getTextureObject(DEFAULT_TEXTURE);
		
		tex.setOnloadAction(() -> {
			width = tex.getTexture().getWidth();
			height = tex.getTexture().getHeight();
		});
		
	}
	
	@Override
	public void updateHitbox() {
		setWidth((int) width);
		setHeight((int) height);
		
		super.updateHitbox();
	}
	
	@Override
	public void render(GraphicsContext g) {
		
		Image image = ResourceManager.getTexture(DEFAULT_TEXTURE);
		
		g.translate(getX(), getY());
		
		g.rotate(Math.toDegrees(theta));
		
		g.drawImage(image, -width/2, -height/2, width, height);
		
		g.rotate(Math.toDegrees(-theta));
		
		g.translate(-getX(), -getY());
		
		super.renderHealthBar(g);
		
		if(GameManager.instance().getCurrentGame().isDebug()) {
			super.renderHitbox(g);
			super.renderVector(g);
		}
		
	}
	
}
