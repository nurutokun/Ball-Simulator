package com.rawad.ballsimulator.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GuiManager {
	
	private ArrayList<GuiComponent> components;
	private ArrayList<Button> buttons;
	private ArrayList<DropDown> dropDowns;
	
	private GuiComponent currentIntersectedComponent;
	private Button currentClickedButton;
	private DropDown currentSelectedDropDown;
	
	public GuiManager() {
		
		components = new ArrayList<GuiComponent>();
		buttons = new ArrayList<Button>();
		dropDowns = new ArrayList<DropDown>();
		
	}
	
	public void update(int x, int y, boolean mouseButtonDown) {
		
		currentIntersectedComponent = getIntersectedComponent(x, y);
		
		for(GuiComponent comp: components) {
			comp.update(x, y, mouseButtonDown);
		}
		
		// Sadly needs to be done this way.
		for(Button butt: buttons) {
			
			if(butt.isClicked()) {
				currentClickedButton = butt;
				break;
			}
			
			currentClickedButton = null;
			
		}
		
		for(DropDown drop: dropDowns) {
			
			if(drop.isClicked()) {
				
				drop.setClicked(false);
				
				if(drop.isMenuDown()) {
					// Tell the drop down menu to select an item.
					drop.calculateSelectedItem(y);
					drop.setMenuDown(false);
					
					currentSelectedDropDown = drop;
					
					return;
					
				} else {
					drop.setMenuDown(true);
				}
				
			}
			
			currentSelectedDropDown = null;
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
		for(GuiComponent comp: components) {
			comp.render(g);
		}
		
	}
	
	private GuiComponent getIntersectedComponent(int x, int y) {
		
		for(int i = components.size() - 1; i >= 0; i--) {// Last-added component gets prioritized.
			
			GuiComponent comp = components.get(i);
			
			if(comp.intersects(x, y)) {
				return comp;
			}
			
		}
		
		return null;
		
	}
	
	public void addComponent(GuiComponent comp) {
		
		if(comp instanceof DropDown) {
			dropDowns.add((DropDown) comp);
			
		} else if(comp instanceof Button) {
			buttons.add((Button) comp);
			
		}
		
		components.add(comp);
		
	}
	
	public Button getCurrentClickedButton() {
		return currentClickedButton;
	}
	
	public DropDown getCurrentSelectedDropDown() {
		return currentSelectedDropDown;
	}
	
	public GuiComponent getCurrentIntersectedComponent() {
		return currentIntersectedComponent;
	}
	
}
