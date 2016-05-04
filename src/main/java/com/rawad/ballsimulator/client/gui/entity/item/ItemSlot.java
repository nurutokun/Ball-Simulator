package com.rawad.ballsimulator.client.gui.entity.item;

import java.io.IOException;

import com.rawad.ballsimulator.entity.item.Item;
import com.rawad.gamehelpers.resources.Loader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ItemSlot extends StackPane {
	
	@FXML private Button button;
	@FXML private ImageView imageView;
	
	private Item item;
	
	private int count;
	
	public ItemSlot(Item item, int count) {
		
		this.item = item;
		
		this.count = count;
		
		FXMLLoader loader = new FXMLLoader(Loader.getFxmlLocation(getClass()));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getCount() {
		return count;
	}
	
}
