package com.rawad.ballsimulator.client.gui.entity.player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.rawad.ballsimulator.client.gui.entity.item.ItemSlot;
import com.rawad.gamehelpers.gui.GuiComponent;
import com.rawad.gamehelpers.gui.GuiManager;

public class PlayerInventory extends GuiComponent {
	
	private static final int ROWS = 5;
	private static final int COLUMNS = 10;
	
	private static final int PADDING = 10;
	
	public static final int SLOTS = ROWS * COLUMNS;
	
	private ArrayList<ItemSlot> itemSlots;
	
	private boolean show;
	
	public PlayerInventory(String id, int x, int y) {
		super(id, x, y, 0, 0);
		
		itemSlots = new ArrayList<ItemSlot>(SLOTS);
		
		generateSlots();
		
		updateHitbox();
		
		hide();// Doesn't update/render by default.
		
		setUpdate(true);
		setRender(true);
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		g.setColor(Color.WHITE);
		g.fill(hitbox);
		
	}
	
	private void generateSlots() {
		
		int x = this.width/2 - (COLUMNS/2 * ItemSlot.WIDTH) - (COLUMNS/2 * PADDING) + this.x;
		int y = this.height/2 - (ROWS/2 * ItemSlot.HEIGHT) - (ROWS/2 * PADDING) + this.y;
		
		this.x = x - PADDING;
		this.y = y - PADDING;
		
		this.width = COLUMNS * ItemSlot.WIDTH + ((COLUMNS+1) * PADDING);
		this.height = ROWS * ItemSlot.HEIGHT + ((ROWS+1) * PADDING);
		
		for(int col = 0; col < COLUMNS; col++) {
			
			for(int row = 0; row < ROWS; row++) {
				
				ItemSlot curSlot = new ItemSlot(null, 0, x + (col * ItemSlot.WIDTH) + (col * PADDING), 
						y + (row * ItemSlot.HEIGHT) + (row * PADDING));
				
				itemSlots.add(curSlot);
				
			}
			
		}
		
	}
	
	@Override
	public void onAdd(GuiManager manager) {
		super.onAdd(manager);
		
		manager.addComponents(itemSlots);
		
	}
	
	public ArrayList<ItemSlot> getSlots() {
		return itemSlots;
	}
	
	/**
	 * Called when inventory needs to be shown.
	 */
	public void show() {
		
		show = true;
		
		setUpdate(true);
		setRender(true);
		
		for(ItemSlot slot: itemSlots) {
			
			slot.setUpdate(true);
			slot.setRender(true);
			
		}
		
	}
	
	/**
	 * Called when inventory doesn't need to be shown.
	 */
	public void hide() {
		
		show = false;
		
		setUpdate(false);
		setRender(false);
		
		for(ItemSlot slot: itemSlots) {
			
			slot.setUpdate(false);
			slot.setRender(false);
			
		}
		
	}
	
	/**
	 * Toggles between showing and hiding inventory.
	 */
	public void toggle() {
		
		if(show) {
			hide();
		} else {
			show();
		}
		
	}
	
	public boolean isShowing() {
		return show;
	}
	
}
