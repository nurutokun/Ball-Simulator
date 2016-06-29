package com.rawad.ballsimulator.game;

import java.util.HashMap;

import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.gamehelpers.game.Game;

public class BallSimulator extends Game {
	
	public static final String NAME = "Ball Simulator";
	
	@Override
	protected EntityBlueprintLoadObject geEntityBlueprintLoadObject() {
		
		final HashMap<Object, String> entityBindings = new HashMap<Object, String>();
		
		for(EEntity entity: EEntity.values()) {
			entityBindings.put(entity, entity.getName());
		}
		
		final String[] contextPaths = {
				EEntity.class.getPackage().getName()
		};
		
		return new EntityBlueprintLoadObject(entityBindings, EEntity.class, contextPaths);
		
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
}
