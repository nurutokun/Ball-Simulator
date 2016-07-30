package com.rawad.ballsimulator.client;

import com.rawad.ballsimulator.client.gui.Background;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.client.GameTextures;
import com.rawad.gamehelpers.game.entity.BlueprintManager;

public final class GameTextures {
	
	public static final Object GAME_ICON = new Object();// Reserved for game icon texture.
	
	private static final HashMap
	
	private static final String FOLDER_ENTITY = "entity";
	
	private static final String TEXTURE_UNKNOWN = "unknown";
	
	private static final String FILE_STATIC_OBJECT = "staticEntity";
	private static final String FILE_PLAYER = "player";
	
	private GameTextures() {}
	
	/**
	 * Loads all textures used in this {@code BallSimulator} game.
	 */
	public static void loadTextures(Loader loader) {
		
		loader.registerUnknownTexture(TEXTURE_UNKNOWN);
		
		GameTextures.putTexture(GAME_ICON, loader.registerTexture("", "game_icon"));
		
		Background.registerTextures(loader);
		
		GameTextures.putTexture(EEntity.STATIC, loader.registerTexture(FOLDER_ENTITY, FILE_STATIC_OBJECT));
		GameTextures.putTexture(EEntity.PLAYER, loader.registerTexture(FOLDER_ENTITY, FILE_PLAYER));
		
		BlueprintManager.getBlueprint(EEntity.STATIC).getEntityBase().getComponent(RenderingComponent.class)
					.setTexture(GameTextures.findTexture(EEntity.STATIC));
		
		BlueprintManager.getBlueprint(EEntity.PLAYER).getEntityBase().getComponent(RenderingComponent.class)
					.setTexture(GameTextures.findTexture(EEntity.PLAYER));
		
	}
	
}
