package com.rawad.ballsimulator.client.gui.entity.item;

import com.rawad.ballsimulator.entity.item.Item;

public class ItemSlot {
	
	private Item item;
	
	private int count;
	
	public ItemSlot(Item item, int count) {
		
		this.item = item;
		
		this.count = count;
		
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getCount() {
		return count;
	}
	
}
