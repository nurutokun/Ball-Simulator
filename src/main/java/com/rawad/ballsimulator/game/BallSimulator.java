package com.rawad.ballsimulator.game;

import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.Blueprint;
import com.rawad.gamehelpers.game.entity.BlueprintManager;
import com.rawad.gamehelpers.log.Logger;

import javafx.concurrent.Task;

public class BallSimulator extends Game {
	
	/**
	 * @Temporary
	 */
	public static final String NAME = "Ball Simulator";
	
	private static int ICON;
	
	public BallSimulator() {
		super();
	}
	
	@Override
	public void registerTextures() {
		
		ICON = loaders.get(CustomLoader.class).registerTexture("", "game_icon");
		
	}
	
	@Override
	protected void init() {
		super.init();
		
		loaders.put(CustomLoader.class, new CustomLoader());
		
		fileParsers.put(TerrainFileParser.class, new TerrainFileParser());
		fileParsers.put(SettingsFileParser.class, new SettingsFileParser());
		
		getProxy().addTask(new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				
				Logger.log(Logger.DEBUG, "Loading entity blueprints...");
				
				EEntity[] entities = EEntity.values();
				
				for(EEntity entity: entities) {
					BlueprintManager.addBlueprint(entity, new Blueprint(EntityFileParser.parseEntityFile(EEntity.class, 
							entity.getName(), EEntity.class.getPackage().getName())));
				}
				
				Logger.log(Logger.DEBUG, "Loaded all entity blueprints.");
				
				return 0;
				
			}
		});
		
	}
	
	@Override
	public int getIconLocation() {
		return ICON;
	}
	
	@Override
	public String toString() {
		return NAME;
	}
	
}
