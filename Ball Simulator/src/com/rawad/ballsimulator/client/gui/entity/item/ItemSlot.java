package com.rawad.ballsimulator.client.gui.entity.item;

import java.awt.Graphics2D;

import com.rawad.ballsimulator.entity.item.Item;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.resources.ResourceManager;

public class ItemSlot extends Button {
	
	public static final int WIDTH;
	public static final int HEIGHT;
	
	private static final int DEFAULT_BACKGROUND;
	private static final int DEFAULT_FOREGROUND;
	
	private Item item;
	
	private int count;
	
	public ItemSlot(Item item, int count, int texture, int x, int y) {
		super("Item Slot", "", x, y, 0, 0);
//		super("Item Slot", x, y);
		
		this.item = item;
		
		this.count = count;
		
		this.texture = texture;
		
		this.width = ResourceManager.getTexture(texture).getWidth();
		this.height = ResourceManager.getTexture(texture).getHeight();
		
		updateHitbox();
		
	}
	
	public ItemSlot(Item item, int count, int x, int y) {
		this(item, count, DEFAULT_BACKGROUND, x, y);
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
	public void render(Graphics2D g) {
		super.render(g, texture, DEFAULT_FOREGROUND, texture, texture);
		
		// Render Item Stack
		
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getCount() {
		return count;
	}
	
}
