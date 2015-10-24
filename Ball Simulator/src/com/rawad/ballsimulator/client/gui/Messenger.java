package com.rawad.ballsimulator.client.gui;

import java.awt.Graphics2D;

import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.GuiComponent;
import com.rawad.gamehelpers.gui.GuiManager;
import com.rawad.gamehelpers.gui.TextEdit;
import com.rawad.gamehelpers.gui.TextLabel;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.resources.ResourceManager;

public class Messenger extends GuiComponent {
	
	private static final int MIN_WIDTH = 16;
	private static final int MIN_HEIGHT = 16;
	
	private static final int DEFAULT_TEXTURE;
	
//	private GuiManager guiManager;
	
	private CloseButton toggle;
	
	private TextLabel textOutput;
	private TextEdit textInput;
	
	private int originalX;
	private int originalY;
	
	private int originalWidth;
	private int originalHeight;
	
	private boolean maximized;
	
	public Messenger(String id, int x, int y, int width, int height) {
		super(id, x, y, width, height);
		
		originalX = x;
		originalY = y;
		
		originalWidth = width;
		originalHeight = height;
		
		toggle = new CloseButton(x + (width/2) - (MIN_WIDTH/2), y - (height/2) + (MIN_HEIGHT/2), MIN_WIDTH, MIN_HEIGHT);
		
		int textInputHeight = height/8;// Use 8th of total height
		
		textOutput = new TextLabel("", x, y, width, height);
		textInput = new TextEdit("", x, y + (height/2) - (textInputHeight/2), width, textInputHeight);
		
		textOutput.setCenterText(false);
		textOutput.setWrapText(true);
		textOutput.setHideText(true);
		textOutput.setTexture(DEFAULT_TEXTURE);
		
		textInput.setNewLineOnEnter(false);
		
		maximized = true;
		
	}
	
	static {
		
		DEFAULT_TEXTURE = GuiComponent.loadTexture(ResourceManager.getString("TextLabel.base"), "mess");
		
	}
	
	@Override
	public void update(MouseEvent me, KeyboardEvent ke) {
		super.update(me, ke);
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		if(maximized) {
			super.render(g);
		} else {
			toggle.render(g);// Only need to render button when minimized.
		}
		
	}
	
	public void handleButtonClick(Button butt) {
		
		switch(butt.getId()) {
		
		case "Close":
			
			maximized = !maximized;
			
			if(maximized) {
				
				onMaximize();
				
			} else {
				
				onMinimize();
				
			}
			
			break;
		
		}
		
	}
	
	public void onMaximize() {
		
		setWidth(originalWidth);// Always set width and height first
		setHeight(originalHeight);
		
		setX(originalX);
		setY(originalY);
		
		updateHitbox();
		
		toggle.setX(originalX + (originalWidth/2) - (toggle.getWidth()/2));
		toggle.setY(originalY - (originalHeight/2) + (toggle.getHeight()/2));
		
		toggle.updateHitbox();
		
		textOutput.setUpdate(true);
		textInput.setUpdate(true);
		
		textOutput.setRender(true);
		textInput.setRender(true);
		
		textInput.setFocused(true);
		
	}
	
	public void onMinimize() {
		
		int newX = originalX - (originalWidth/2) + (MIN_WIDTH/2);// Positions component in the bottom left corner of where it
		int newY = originalY + (originalHeight/2) - (MIN_HEIGHT/2);// originally was
		
		toggle.setX(newX);
		toggle.setY(newY);
		
		toggle.updateHitbox();
		
		setWidth(MIN_WIDTH);
		setHeight(MIN_HEIGHT);
		
		setX(newX);
		setY(newY);
		
		updateHitbox();
		
		textOutput.setUpdate(false);
		textInput.setUpdate(false);
		
		textOutput.setRender(false);
		textInput.setRender(false);
		
	}
	
	@Override
	public void onAdd(GuiManager manager) {
		super.onAdd(manager);
		
		manager.addComponent(textOutput);
		manager.addComponent(textInput);
		manager.addComponent(toggle);
		
	}
	
	public TextEdit getInputField() {
		return textInput;
	}
	
	public TextLabel getOutputField() {
		return textOutput;
	}
	
}
