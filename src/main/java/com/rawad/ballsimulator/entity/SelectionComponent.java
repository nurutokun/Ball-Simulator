package com.rawad.ballsimulator.entity;

import javax.xml.bind.annotation.XmlTransient;

import com.rawad.gamehelpers.game.entity.Component;

public class SelectionComponent extends Component {
	
	private boolean selected;
	
	private boolean highlighted;
	
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
	
	/**
	 * @return the highlighted
	 */
	public boolean isHighlighted() {
		return highlighted;
	}
	
	/**
	 * @param highlighted the highlighted to set
	 */
	@XmlTransient public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof SelectionComponent) {
			
			SelectionComponent selectionComp = (SelectionComponent) comp;
			
			selectionComp.setSelected(isSelected());
			selectionComp.setHighlighted(isHighlighted());
			
		}
		
		return comp;
	}
	
}
