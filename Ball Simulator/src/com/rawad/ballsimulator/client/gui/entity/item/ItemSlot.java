package com.rawad.ballsimulator.client.gui.entity.item;

import java.awt.Dimension;
import java.awt.Graphics;

import com.rawad.ballsimulator.entity.item.Item;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.resources.ResourceManager;

public class ItemSlot extends Button {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 6554805239312950335L;
	
	public static final int WIDTH;
	public static final int HEIGHT;
	
	private static final int DEFAULT_BACKGROUND;
	private static final int DEFAULT_FOREGROUND;
	
	private Item item;
	
	private int count;
	
	private int width;
	private int height;
	
	public ItemSlot(Item item, int count) {
		super("Item Slot", "");
		
		this.item = item;
		
		this.count = count;
		
		this.width = ResourceManager.getTexture(DEFAULT_BACKGROUND).getWidth();
		this.height = ResourceManager.getTexture(DEFAULT_BACKGROUND).getHeight();
		
		setRolloverIcon(ResourceManager.getImageIcon(DEFAULT_FOREGROUND));
		setRolloverSelectedIcon(ResourceManager.getImageIcon(DEFAULT_FOREGROUND));
		
	}
	
	static {
		
		Game game = GameManager.instance().getCurrentGame();
		
		CustomLoader loader = game.getLoader(game.toString());
		
		DEFAULT_BACKGROUND = loader.loadTexture("gui/inventory", "item_slot_background");
		DEFAULT_FOREGROUND = loader.loadTexture("gui/inventory", "item_slot_foreground");
		
		WIDTH = ResourceManager.getTexture(DEFAULT_BACKGROUND).getWidth();
		HEIGHT = ResourceManager.getTexture(DEFAULT_BACKGROUND).getHeight();
		
	}
	@Override
	public void paintComponents(Graphics g) {
		super.paintComponents(g);
		
		// Render item stack + count of items in stack.
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getCount() {
		return count;
	}
	
}
