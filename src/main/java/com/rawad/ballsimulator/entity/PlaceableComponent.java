package com.rawad.ballsimulator.entity;

import javax.xml.bind.annotation.XmlTransient;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;

public class PlaceableComponent extends Component {
	
	/** Used for passing to the {@link com.rawad.gamehelpers.game.entity.Entity#createEntity(Object)} method. */
	private Object toPlace = null;
	
	private Entity toExtract = null;
	
	private boolean placeRequested = false;
	private boolean removeRequested = false;
	private boolean extractRequested = false;
	
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
	 * @return the toExtract
	 */
	public Entity getToExtract() {
		return toExtract;
	}
	
	/**
	 * @param toExtract the toExtract to set
	 */
	@XmlTransient public void setToExtract(Entity toExtract) {
		this.toExtract = toExtract;
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
	@XmlTransient public void setPlaceRequested(boolean placeRequested) {
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
	@XmlTransient public void setRemoveRequested(boolean removeRequested) {
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
	@XmlTransient public void setExtractRequested(boolean extractRequested) {
		this.extractRequested = extractRequested;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof PlaceableComponent) {
			
			PlaceableComponent placeableComp = (PlaceableComponent) comp;
			
			placeableComp.setPlaceRequested(isPlaceRequested());
			placeableComp.setToPlace(getToPlace());
			
		}
		
		return comp;
	}
	
}
