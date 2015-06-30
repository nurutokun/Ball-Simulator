package com.rawad.ballsimulator.gui.overlay;

import java.awt.Color;

import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.gui.Button;
import com.rawad.ballsimulator.input.MouseEvent;

public class PauseOverlay extends Overlay {
	
	private boolean paused;
	
	public PauseOverlay(Color backgroundColor, int x, int y, int width, int height) {
		super(backgroundColor, x, y, width, height);
		
		int centerX = this.width/2;
		int verticalSections = this.height/7;
		
		addComponent(new Button("Resume", centerX, verticalSections * 3));
		addComponent(new Button("Main Menu", centerX, verticalSections * 4));
		
	}
	
	public PauseOverlay(Color backgroundColor, int x, int y) {
		this(backgroundColor, x, y, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
		
	}
	
	public PauseOverlay(Color backgroundColor) {
		this(backgroundColor, 0, 0);
		
	}
	
	public PauseOverlay(int x, int y, int width, int height) {
		this(Color.GRAY, x, y, width, height);
		
	}
	
	public PauseOverlay(int width, int height) {
		this(0, 0, width, height);
		
	}
	
	@Override
	public void update(MouseEvent e) {
		
		if(isPaused()) {
			super.update(e);
		}
		
	}
	
	@Override
	public Button getClickedButton() {
		
		if(isPaused()) {
			return super.getClickedButton();
		} else {
			return null;
		}
		
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
}
