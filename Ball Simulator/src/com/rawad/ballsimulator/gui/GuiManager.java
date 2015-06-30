package com.rawad.ballsimulator.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.rawad.ballsimulator.input.MouseEvent;

public class GuiManager {
	
	private ArrayList<GuiComponent> components;
	private ArrayList<Button> buttons;
	private ArrayList<DropDown> dropDowns;
	
	private Button currentClickedButton;
	private DropDown currentSelectedDropDown;
	
	public GuiManager() {
		
		components = new ArrayList<GuiComponent>();
		buttons = new ArrayList<Button>();
		dropDowns = new ArrayList<DropDown>();
		
	}
	
	public void update(MouseEvent e) {
		
		for(GuiComponent comp: components) {
			
			comp.update(e);
			
			if(e.isConsumed()) {// No need to update anymore components, the previous one consumed the event.
				break;
			}
			
		}
		
		// Sadly, needs to be done this way.
		for(Button butt: buttons) {
			
			if(butt.isClicked()) {
				currentClickedButton = butt;
				
				butt.setClicked(false);
				
				break;
			}
			
			currentClickedButton = null;
			
		}
		
		for(DropDown drop: dropDowns) {
			
			if(drop.isClicked()) {
				
				drop.setClicked(false);
				
				if(drop.isMenuDown()) {
					// Tell the drop down menu to select an item.
					drop.calculateSelectedItem(e.getY());
					drop.setMenuDown(false);
					
					currentSelectedDropDown = drop;
					
					break;
					
				} else {
					drop.setMenuDown(true);
				}
				
			}
			
			currentSelectedDropDown = null;
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
		for(int i = components.size() - 1; i >= 0; i--) {
			GuiComponent comp = components.get(i);
			
			comp.render(g);
		}
		
	}
	
	public void addComponent(GuiComponent comp) {
		
		// Appends new components to beginning of list so that they are prioritized and are easily loop-able that way
		if(comp instanceof DropDown) {
			dropDowns.add(0, (DropDown) comp);
			
		} else if(comp instanceof Button) {
			buttons.add(0, (Button) comp);
			
		}
		
		// Aren't added backwards for updating purposes
		components.add(comp);
		
	}
	
	public Button getCurrentClickedButton() {
		return currentClickedButton;
	}
	
	public DropDown getCurrentSelectedDropDown() {
		return currentSelectedDropDown;
	}
	
}
