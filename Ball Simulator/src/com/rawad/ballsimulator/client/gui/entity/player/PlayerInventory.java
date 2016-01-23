package com.rawad.ballsimulator.client.gui.entity.player;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import com.rawad.ballsimulator.client.gui.entity.item.ItemSlot;
import com.rawad.gamehelpers.gui.overlay.Overlay;

public class PlayerInventory extends Overlay {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -335256592412816329L;
	
	private static final int ROWS = 5;
	private static final int COLUMNS = 10;
	
	private static final int PADDING = 10;
	
	public static final int SLOTS = ROWS * COLUMNS;
	
	private ArrayList<ItemSlot> itemSlots;
	
	private int width;
	private int height;
	
	public PlayerInventory() {
		super("Player Inventory", Color.GRAY);
		
		itemSlots = new ArrayList<ItemSlot>(SLOTS);
		
		generateSlots();
		
		setVisible(false);// Hide by default.
	}
	
	private void generateSlots() {
		
		int x = getWidth()/2 - (COLUMNS/2 * ItemSlot.WIDTH) - (COLUMNS/2 * PADDING) + getX();
		int y = getHeight()/2 - (ROWS/2 * ItemSlot.HEIGHT) - (ROWS/2 * PADDING) + getY();
		
		width = COLUMNS * ItemSlot.WIDTH + ((COLUMNS+1) * PADDING);
		height = ROWS * ItemSlot.HEIGHT + ((ROWS+1) * PADDING);
		
		for(int col = 0; col < COLUMNS; col++) {
			
			for(int row = 0; row < ROWS; row++) {
				
				ItemSlot curSlot = new ItemSlot(null, 0);
				
				setBounds(x + (col * ItemSlot.WIDTH) + (col * PADDING), y + (row * ItemSlot.HEIGHT) + (row * PADDING),
						ItemSlot.WIDTH, ItemSlot.HEIGHT);
				
				itemSlots.add(curSlot);
				
			}
			
		}
		
	}
	
	public ArrayList<ItemSlot> getSlots() {
		return itemSlots;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	
	/**
	 * Toggles between showing and hiding inventory.
	 */
	public void toggle() {
		setVisible(!isVisible());
	}

}
