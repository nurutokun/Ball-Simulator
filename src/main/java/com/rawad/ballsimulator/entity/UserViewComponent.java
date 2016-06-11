package com.rawad.ballsimulator.entity;

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
	
	private Rectangle viewport = new Rectangle(0 ,0 ,0, 0);
	
	public void setViewport(Rectangle viewport) {
		this.viewport = viewport;
	}
	
	public Rectangle getViewport() {
		return viewport;
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
