package com.rawad.ballsimulator.gui;

import java.util.ArrayList;

public class OverlayManager {
	
	private ArrayList<Overlay> overlays;
	
	private Button currentClickedButton;
	
	public OverlayManager() {
		
		overlays = new ArrayList<Overlay>();
		
	}
	
	public void update(int x, int y, boolean mouseButtonDown) {
		
		for(Overlay over: overlays) {
			over.update(x, y, mouseButtonDown);
		}
		
		for(Overlay over: overlays) {
			currentClickedButton = over.getClickedButton();
			
			if(currentClickedButton != null) {
				break;
			}
			
		}
		
	}
	
	public void addOverlay(Overlay over) {
		overlays.add(over);
	}
	
	public Button getClickedButton() {
		return currentClickedButton;
	}
	
}
