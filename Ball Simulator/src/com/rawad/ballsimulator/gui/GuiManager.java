package com.rawad.ballsimulator.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GuiManager {
	
	private ArrayList<GuiComponent> components;
	private ArrayList<Button> buttons;
	
	private GuiComponent currentIntersectedComponent;
	private Button currentClickedButton;
	
	public GuiManager() {
		
		components = new ArrayList<GuiComponent>();
		buttons = new ArrayList<Button>();
		
	}
	
	public void update(int x, int y) {
		
		currentIntersectedComponent = getIntersectedComponent(x, y);
		
		for(GuiComponent comp: components) {
			comp.update(x, y);
		}
		
		for(Button butt: buttons) {
			
			if(butt.isClicked()) {
				currentClickedButton = butt;
				break;
			}
			
			currentClickedButton = null;
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
		for(GuiComponent comp: components) {
			comp.render(g);
		}
		
	}
	
	private GuiComponent getIntersectedComponent(int x, int y) {
		
		for(int i = components.size() - 1; i >= 0; i--) {// Last-added component gets superiority
			
			GuiComponent comp = components.get(i);
			
			if(comp.intersects(x, y)) {
				return comp;
			}
			
		}
		
		return null;
		
	}
	
	public void addComponent(GuiComponent comp) {
		
		if(comp instanceof Button) {
			buttons.add((Button) comp);
		}
		
		components.add(comp);
		
	}
	
	public Button getCurrentClickedButton() {
		return currentClickedButton;
	}
	
	public GuiComponent getCurrentIntersectedComponent() {
		return currentIntersectedComponent;
	}
	
}
