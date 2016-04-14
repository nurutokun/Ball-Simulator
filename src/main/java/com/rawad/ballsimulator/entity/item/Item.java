package com.rawad.ballsimulator.entity.item;

import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.world.World;

import javafx.scene.canvas.GraphicsContext;

public class Item extends Entity {
	
	/** ID for the item, should probably contain name of image in it for use by item icon. */
	private String id;
	
	public Item(World world, String id, int x, int y) {
		super(world);
		
		this.id = id;
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render(GraphicsContext g) {
		
	}
	
	public String getId() {
		return id;
	}
	
}
