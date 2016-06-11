package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;


public class RandomPositionComponent extends Component {
	
	private boolean generateNewPosition;
	
	/**
	 * @return the generateNewPosition
	 */
	public boolean isGenerateNewPosition() {
		return generateNewPosition;
	}
	
	/**
	 * @param generateNewPosition the generateNewPosition to set
	 */
	public void setGenerateNewPosition(boolean generateNewPosition) {
		this.generateNewPosition = generateNewPosition;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof RandomPositionComponent) {
			
			RandomPositionComponent randomPositionComp = (RandomPositionComponent) comp;
			
			randomPositionComp.setGenerateNewPosition(isGenerateNewPosition());
			
			return randomPositionComp;
			
		}
		
		return comp;
	}
	
}
