package com.rawad.ballsimulator.gamestates;

import java.awt.Graphics2D;

import com.rawad.ballsimulator.gui.GuiComponent;
import com.rawad.ballsimulator.gui.GuiManager;
import com.rawad.ballsimulator.input.MouseInput;

public abstract class State {
	
	private final StateEnum stateType;
	
	private GuiManager guiManager;
	
	public State(StateEnum stateType) {
		this.stateType = stateType;
		
		guiManager = new GuiManager();
		
	}
	
	/**
	 * Should be called by subclass whenever anything GUI-related is being done
	 */
	public void update() {
		
		guiManager.update(MouseInput.getX(), MouseInput.getY());
		
		GuiComponent comp = guiManager.getCurrentIntersectedComponent();
		
		if(comp != null) {
			
			if(MouseInput.isButtonClicked(MouseInput.LEFT_MOUSE_BUTTON)) {
				handleMouseHover(comp);
				handleMouseClick(comp);
				
				MouseInput.setButtonClicked(MouseInput.LEFT_MOUSE_BUTTON, false);
			} else {
				handleMouseHover(comp);
				// Could add more here, of course
			}
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
		guiManager.render(g);
		
	}
	
	/**
	 * Should be inherited to implement component clicking
	 * 
	 * @param comp
	 */
	protected void handleMouseClick(GuiComponent comp) {}
	
	/**
	 * Should be inherited to implement component hovering
	 * 
	 * @param comp
	 */
	protected void handleMouseHover(GuiComponent comp) {}
	
	protected final void addGuiComponent(GuiComponent comp) {
		
		guiManager.addComponent(comp);
		
	}
	
	public final StateEnum getStateType() {
		return stateType;
	}
	
}
