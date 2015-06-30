package com.rawad.ballsimulator.gamestates;

import java.awt.Graphics2D;

import com.rawad.ballsimulator.gui.Button;
import com.rawad.ballsimulator.gui.DropDown;
import com.rawad.ballsimulator.gui.GuiComponent;
import com.rawad.ballsimulator.gui.GuiManager;
import com.rawad.ballsimulator.gui.overlay.Overlay;
import com.rawad.ballsimulator.gui.overlay.OverlayManager;
import com.rawad.ballsimulator.input.MouseEvent;
import com.rawad.ballsimulator.input.MouseInput;

public abstract class State {
	
	private final StateEnum stateType;
	
	private GuiManager guiManager;
	private OverlayManager overlayManager;
	
	protected StateManager sm;
	
	private MouseEvent e;
	
	public State(StateEnum stateType) {
		this.stateType = stateType;
		
		guiManager = new GuiManager();
		overlayManager = new OverlayManager();
		
	}
	
	/**
	 * Should be called by subclass whenever anything GUI-related is being done
	 */
	public void update() {
		
		e = new MouseEvent(MouseInput.getX(), MouseInput.getY(), MouseInput.LEFT_MOUSE_BUTTON);
		
		this.update(e);
		
	}
	
	/**
	 * Optional in case different values want to be used. e.g. when mouse is clamped, maybe use coordinates of an on-screen object.
	 * 
	 * @param x
	 * @param y
	 * @param mouseButtonDown
	 */
	public void update(MouseEvent e) {
		
		guiManager.update(e);
		
		Button butt = guiManager.getCurrentClickedButton();
		
		if(butt != null) {
			handleButtonClick(butt);
			
		}
		
		DropDown drop = guiManager.getCurrentSelectedDropDown();
		
		if(drop != null) {
			handleDropDownMenuSelect(drop);
			
		}
		
		overlayManager.update(e);
		
		butt = overlayManager.getClickedButton();
		
		if(butt != null) {
			handleButtonClick(butt);
			
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
	
	protected void handleDropDownMenuSelect(DropDown drop) {}
	
	protected final void addGuiComponent(GuiComponent comp) {
		
		guiManager.addComponent(comp);
		
	}
	
	protected final void addOverlay(Overlay overlay) {
		
		overlayManager.addOverlay(overlay);
		
	}
	
	protected void setStateManager(StateManager sm) {
		this.sm = sm;
	}
	
	public final StateEnum getStateType() {
		return stateType;
	}
	
}
