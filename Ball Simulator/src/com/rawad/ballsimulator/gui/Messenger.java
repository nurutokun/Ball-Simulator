package com.rawad.ballsimulator.gui;

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
	
	private GuiManager guiManager;
	
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
		
		guiManager = new GuiManager();
		
		toggle = new CloseButton(x + (width/2) - (MIN_WIDTH/2), y - (height/2) + (MIN_HEIGHT/2), MIN_WIDTH, MIN_HEIGHT);
		
		int textInputHeight = height/8;// Use 8th of total height
		
		textOutput = new TextLabel("", x, y, width, height);
		textInput = new TextEdit("", x, y + (height/2) - (textInputHeight/2), width, textInputHeight);
		
		textOutput.setCenterText(false);
		textOutput.setWrapText(true);
		textOutput.setHideText(true);
		textOutput.setTexture(GuiComponent.loadTexture(ResourceManager.getString("TextLabel.base"), "mess"));
		
		textInput.setNewLineOnEnter(false);
		
		guiManager.addComponent(textOutput);
		guiManager.addComponent(textInput);
		guiManager.addComponent(toggle);
		
		maximized = true;
		
	}
	
	static {
		
	}
	
	@Override
	public void update(MouseEvent me, KeyboardEvent ke) {
		// No need to call super method, this is a multi-component
		
		guiManager.update(me, ke);
		
		Button butt = guiManager.getCurrentClickedButton();
		
		if(butt != null) {
			handleButtonClick(butt);
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		if(maximized) {
			guiManager.render(g);
		} else {
			toggle.render(g);// Only need to render button when minimized.
		}
		
		super.render(g);
		
		
		
	}
	
	private void handleButtonClick(Button comp) {
		
		switch(comp.getId()) {
		
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
		
	}
	
	public TextEdit getInputField() {
		return textInput;
	}
	
	public TextLabel getOutputField() {
		return textOutput;
	}
	
}
