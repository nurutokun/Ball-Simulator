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
	
	public Rectangle getViewport() {
		return viewport;
	}
	
}
