package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;

public class PlaceableComponent extends Component {
	
	/** Used for passing to the {@link com.rawad.gamehelpers.game.entity.Entity#createEntity(Object)} method. */
	private Object toPlace = null;
	
	private boolean placeRequested = false;
	
	/**
	 * @return the toPlace
	 */
	public Object getToPlace() {
		return toPlace;
	}
	
	/**
	 * @param toPlace the toPlace to set
	 */
	public void setToPlace(Object toPlace) {
		this.toPlace = toPlace;
	}
	
	/**
	 * @return the placeRequested
	 */
	public boolean isPlaceRequested() {
		return placeRequested;
	}
	
	/**
	 * @param placeRequested the placeRequested to set
	 */
	public void setPlaceRequested(boolean placeRequested) {
		this.placeRequested = placeRequested;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof PlaceableComponent) {
			
			PlaceableComponent placeableComp = (PlaceableComponent) comp;
			
			placeableComp.setPlaceRequested(isPlaceRequested());
			placeableComp.setToPlace(getToPlace());
			
			return placeableComp;
			
		}
		
		return comp;
	}
	
}
