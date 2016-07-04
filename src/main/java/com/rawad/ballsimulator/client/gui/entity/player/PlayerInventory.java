package com.rawad.ballsimulator.client.gui.entity.player;

import com.rawad.ballsimulator.client.gui.entity.item.ItemSlot;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.client.gui.IHideable;

import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Control;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class PlayerInventory extends GridPane implements IHideable {
	
	// 5 rows. 10 columns default
	private static final int ROWS = 5;
	private static final int COLUMNS = 10;
	
	private static final RowConstraints ROW_CONSTRAINT = new RowConstraints(Control.USE_COMPUTED_SIZE, 
			Control.USE_COMPUTED_SIZE, Double.MAX_VALUE, Priority.NEVER, VPos.CENTER, true);
	private static final ColumnConstraints COL_CONSTRAINT = new ColumnConstraints(Control.USE_COMPUTED_SIZE, 
			Control.USE_COMPUTED_SIZE, Double.MAX_VALUE, Priority.NEVER, HPos.CENTER, true);
	
	private int rows;
	private int columns;
	
	public PlayerInventory(int rows, int columns) {
		
		this.rows = rows;
		this.columns = columns;
		
		FXMLLoader loader = new FXMLLoader(Loader.getFxmlLocation(getClass()));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		hgapProperty().bind(widthProperty().divide(columns * 10));
		vgapProperty().bind(heightProperty().divide(rows * 10));
		
		visibleProperty().addListener((e, prevVisible, currentlyVisible) -> {
			
			if(!currentlyVisible) getParent().requestFocus();
			
		});
		
		generateSlots();
		
	}
	
	public PlayerInventory() {
		this(ROWS, COLUMNS);
	}
	
	private void generateSlots() {
		
		int startRow = getRowConstraints().size() / 2;
		int startCol = getColumnConstraints().size() / 2;
		
		for(int row = startRow; row < rows + startRow; row++) {
			
			getRowConstraints().add(row, ROW_CONSTRAINT);// Add rows
			
			for(int col = startCol; col < columns + startCol; col++) {
				
				if(row == startRow) getColumnConstraints().add(col, COL_CONSTRAINT);// Add cols.
				
				ItemSlot inventorySlot = new ItemSlot(null, 0);
				inventorySlot.setPrefSize(40, 40);
				
				add(inventorySlot, col, row);
				
			}
			
		}
		
	}
	
	@Override
	public void show() {
		setVisible(true);
	}
	
	@Override
	public void hide() {
		setVisible(false);
	}
	
	@Override
	public boolean isShowing() {
		return isVisible();
	}
	
}
