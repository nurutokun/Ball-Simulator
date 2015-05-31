package com.rawad.ballsimulator.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GuiManager {
	
	private ArrayList<GuiComponent> components;
	
	private GuiComponent currentIntersectedComponent;
	
	public GuiManager() {
		
		components = new ArrayList<GuiComponent>();
		
	}
	
	public void update(int x, int y) {
		
		for(GuiComponent comp: components) {
			comp.update(x, y);
		}
		
		currentIntersectedComponent = getIntersectedComponent(x, y);
		
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
		
		components.add(comp);
		
	}
	
	public GuiComponent getCurrentIntersectedComponent() {
		return currentIntersectedComponent;
	}
	
}
