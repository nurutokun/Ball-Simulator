package com.rawad.ballsimulator.entity.item;

import java.awt.Graphics2D;

import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.world.World;

public class Item extends Entity{
	
	/** ID for the item, should probably contain name of image in it for use by item icon. */
	private String id;
	
	public Item(World world, String id, int x, int y) {
		super(world);
		
		this.id = id;
		
	}

	@Override
	public void update(long timePassed) {
		
	}

	@Override
	public void render(Graphics2D g) {
		
	}
	
	public String getId() {
		return id;
	}
	
}
