package com.rawad.ballsimulator.client.gui.entity.player;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.rawad.ballsimulator.client.gui.entity.item.ItemSlot;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.gui.overlay.Overlay;

public class PlayerInventory extends Overlay {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -335256592412816329L;
	
	private static final int ROWS = 5;
	private static final int COLUMNS = 10;
	
	public static final int SLOTS = ROWS * COLUMNS;
	
	private ArrayList<ItemSlot> itemSlots;
	
	private JPanel slotContainer;
	
	public PlayerInventory() {
		super("Player Inventory");
		
		itemSlots = new ArrayList<ItemSlot>(SLOTS);
		
		generateSlots();
		
	}
	
	private void generateSlots() {
		
		slotContainer = new JPanel();
		
		slotContainer.setLayout(new GridLayout(ROWS, COLUMNS, Game.SCREEN_WIDTH/(COLUMNS - 1)/10, 
				Game.SCREEN_HEIGHT/(ROWS - 1)/10));
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("center:default:grow"),
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("center:default:grow"),},
			new RowSpec[] {
				RowSpec.decode("pref:grow"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		for(int col = 0; col < COLUMNS; col++) {
			
			for(int row = 0; row < ROWS; row++) {
				
				ItemSlot curSlot = new ItemSlot(null, 0);
				
				itemSlots.add(curSlot);
				slotContainer.add(curSlot);
				
			}
			
		}
		
		this.add(slotContainer, "2, 2, fill, fill");
		
	}
	
	public ArrayList<ItemSlot> getSlots() {
		return itemSlots;
	}
	
}
