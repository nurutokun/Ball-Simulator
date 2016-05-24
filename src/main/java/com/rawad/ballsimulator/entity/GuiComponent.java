package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;

import javafx.scene.Node;

/**
 * Represents a GUI that can be displayed by an {@code Entity}.
 * 
 * @author Rawad
 *
 */
public class GuiComponent extends Component {
	
	private Node gui;
	
	/**
	 * @param gui the gui to set
	 */
	public void setGui(Node gui) {
		this.gui = gui;
	}
	
	/**
	 * @return the gui
	 */
	public Node getGui() {
		return gui;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof GuiComponent) {
			
			GuiComponent guiComp = (GuiComponent) comp;
			
			guiComp.setGui(getGui());
			
			return guiComp;
			
		}
		
		return comp;
	}
	
}