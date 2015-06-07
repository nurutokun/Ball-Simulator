package com.rawad.ballsimulator.gamestates;

import java.awt.Graphics2D;

import com.rawad.ballsimulator.gui.Button;
import com.rawad.ballsimulator.gui.GuiComponent;
import com.rawad.ballsimulator.gui.GuiManager;
import com.rawad.ballsimulator.input.MouseInput;

public abstract class State {
	
	private final StateEnum stateType;
	
	private GuiManager guiManager;
	
	protected StateManager sm;
	
	public State(StateEnum stateType) {
		this.stateType = stateType;
		
		guiManager = new GuiManager();
		
	}
	
	/**
	 * Should be called by subclass whenever anything GUI-related is being done
	 */
	public void update() {
		
		update(MouseInput.getX(), MouseInput.getY(), MouseInput.isButtonDown(MouseInput.LEFT_MOUSE_BUTTON));
		
	}
	
	/**
	 * Optional in case different values want to be used. e.g. when mouse is clamped, maybe use coordinates of an on-screen object.
	 * 
	 * @param x
	 * @param y
	 * @param mouseButtonDown
	 */
	public void update(int x, int y, boolean mouseButtonDown) {
		
		guiManager.update(x,y, mouseButtonDown);
		
		Button butt = guiManager.getCurrentClickedButton();
		
		if(butt != null) {
			handleButtonClick(butt);
			butt.setClicked(false);
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
	
	protected void handleButtonClick(Button butt) {}
	
	protected final void addGuiComponent(GuiComponent comp) {
		
		guiManager.addComponent(comp);
		
	}
	
	protected void setStateManager(StateManager sm) {
		this.sm = sm;
	}
	
	public final StateEnum getStateType() {
		return stateType;
	}
	
}
