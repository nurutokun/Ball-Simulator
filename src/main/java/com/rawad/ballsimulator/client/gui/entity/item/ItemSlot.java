package com.rawad.ballsimulator.client.gui.entity.item;

import java.io.IOException;

import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.jfxengine.loader.GuiLoader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ItemSlot extends StackPane {
	
	@FXML private Button button;
	@FXML private ImageView imageView;
	
	private Entity item;
	
	private int count;
	
	public ItemSlot(Entity item, int count) {
		
		this.item = item;
		
		this.count = count;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load(GuiLoader.streamLayoutFile(getClass()));
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public Entity getItem() {
		return item;
	}
	
	public int getCount() {
		return count;
	}
	
}
