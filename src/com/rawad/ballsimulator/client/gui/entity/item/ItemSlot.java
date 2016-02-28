package com.rawad.ballsimulator.client.gui.entity.item;

import java.awt.Dimension;
import java.awt.Graphics;

import com.rawad.ballsimulator.entity.item.Item;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.resources.TextureResource;

public class ItemSlot extends Button {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 6554805239312950335L;
	
	public static int WIDTH;
	public static int HEIGHT;
	
	private static int DEFAULT_BACKGROUND;
	private static int DEFAULT_FOREGROUND;
	
	private Item item;
	
	private int count;
	
	public ItemSlot(Item item, int count) {
		super("", "Item Slot");
		
		this.item = item;
		
		this.count = count;
		
		setBackgroundTexture(DEFAULT_BACKGROUND);
		setForegroundTexture(DEFAULT_FOREGROUND);
		setOnclickTexture(DEFAULT_FOREGROUND);
		setDisabledTexture(DEFAULT_BACKGROUND);
		
		setFocusable(false);
		
	}
	
	public static void registerTextures(CustomLoader loader) {
		
		DEFAULT_BACKGROUND = loader.loadGuiTexture("inventory", "item_slot_background");
		DEFAULT_FOREGROUND = loader.loadGuiTexture("inventory", "item_slot_foreground");
		
		final TextureResource defaultBackground = ResourceManager.getTextureObject(DEFAULT_BACKGROUND);
		
		defaultBackground.setOnloadAction(new Runnable() {
			
			@Override
			public void run() {
				
				WIDTH = defaultBackground.getTexture().getWidth();
				HEIGHT = defaultBackground.getTexture().getHeight();
				
			}
			
		});
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Render item stack + count of items in stack.
		g.drawString("Items: " + count, 0, 0);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getCount() {
		return count;
	}
	
}
