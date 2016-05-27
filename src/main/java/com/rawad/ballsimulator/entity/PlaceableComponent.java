package com.rawad.ballsimulator.entity;

import java.util.ArrayList;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Listener;

public class PlaceableComponent extends Component {
	
	private ArrayList<Listener<TransformComponent>> extractionListeners = new ArrayList<Listener<TransformComponent>>();
	
	/** Used for passing to the {@link com.rawad.gamehelpers.game.entity.Entity#createEntity(Object)} method. */
	private Object toPlace = null;
	
	private boolean placeRequested = false;
	private boolean removeRequested = false;
	private boolean extractRequested = false;
	
	public ArrayList<Listener<TransformComponent>> getExtractionListeners() {
		return extractionListeners;
	}
	
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
		
	/**
	 * @return the removeRequested
	 */
	public boolean isRemoveRequested() {
		return removeRequested;
	}
	
	/**
	 * @param removeRequested the removeRequested to set
	 */
	public void setRemoveRequested(boolean removeRequested) {
		this.removeRequested = removeRequested;
	}
		
	/**
	 * @return the extractRequested
	 */
	public boolean isExtractRequested() {
		return extractRequested;
	}
	
	/**
	 * @param extractRequested the extractRequested to set
	 */
	public void setExtractRequested(boolean extractRequested) {
		this.extractRequested = extractRequested;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof PlaceableComponent) {
			
			PlaceableComponent placeableComp = (PlaceableComponent) comp;
			
			placeableComp.setPlaceRequested(isPlaceRequested());
			placeableComp.setToPlace(getToPlace());
			
			placeableComp.getExtractionListeners().addAll(getExtractionListeners());
			
			return placeableComp;
			
		}
		
		return comp;
	}
	
}
