package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;

public class RollingComponent extends Component {
	
	// TODO: Maybe a special render for applying transformations to entities with this component differently from normal
	// rotations.
	
	@Override
	public Component copyData(Component comp) {
		return comp;
	}
	
}
