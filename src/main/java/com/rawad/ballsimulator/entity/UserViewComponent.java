package com.rawad.ballsimulator.entity;

import javax.xml.bind.annotation.XmlTransient;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.geometry.Rectangle;

/**
 * Marks an entity as being able to provide a view to the user.
 * 
 * @author Rawad
 * 
 * @see com.rawad.ballsimulator.game.CameraFollowSystem
 * @see com.rawad.ballsimulator.game.CameraRoamingSystem
 * @see com.rawad.ballsimulator.entity.EEntity#CAMERA
 * 
 */
public class UserViewComponent extends Component {
	
	private Rectangle viewport = new Rectangle(0, 0, 0, 0);
	
	@XmlTransient private Rectangle requestedViewport = new Rectangle(0, 0, 0, 0);
	
	@XmlTransient private double preferredScaleX = 1d;
	@XmlTransient private double preferredScaleY = 1d;
	
	public void setViewport(Rectangle viewport) {
		this.viewport = viewport;
	}
	
	public Rectangle getViewport() {
		return viewport;
	}
	
	public Rectangle getRequestedViewport() {
		return requestedViewport;
	}
	
	/**
	 * @return the preferredScaleX
	 */
	public double getPreferredScaleX() {
		return preferredScaleX;
	}
	
	/**
	 * @param preferredScaleX the preferredScaleX to set
	 */
	public void setPreferredScaleX(double preferredScaleX) {
		this.preferredScaleX = preferredScaleX;
	}
	
	/**
	 * @return the preferredScaleY
	 */
	public double getPreferredScaleY() {
		return preferredScaleY;
	}
	
	/**
	 * @param preferredScaleY the preferredScaleY to set
	 */
	public void setPreferredScaleY(double preferredScaleY) {
		this.preferredScaleY = preferredScaleY;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof UserViewComponent) {
			
			UserViewComponent userViewComp = (UserViewComponent) comp;
			
			Rectangle viewport = userViewComp.getViewport();
			
			viewport.setX(this.viewport.getX());
			viewport.setY(this.viewport.getY());
			
			viewport.setWidth(this.viewport.getWidth());
			viewport.setHeight(this.viewport.getHeight());
			
			return userViewComp;
			
		}
		
		return comp;
		
	}
	
}
