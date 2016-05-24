package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;

public class SelectionComponent extends Component {
	
	private boolean selected;
	
	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}
	
	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof SelectionComponent) {
			
			SelectionComponent selectionComp = (SelectionComponent) comp;
			
			selectionComp.setSelected(isSelected());
			
			return selectionComp;
			
		}
		
		return comp;
	}
	
}
