package com.rawad.ballsimulator.client;

import com.rawad.ballsimulator.client.gui.Background;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.client.GameTextures;
import com.rawad.gamehelpers.game.entity.BlueprintManager;
import com.rawad.gamehelpers.resources.ResourceManager;

public final class TexturesRegister {
	
	private static final String FOLDER_ENTITY = "entity";
	
	private static final String FILE_STATIC_OBJECT = "staticEntity";
	private static final String FILE_PLAYER = "player";
	
	private TexturesRegister() {}
	
	/**
	 * Registers all textures used in this {@code BallSimulator} game.
	 */
	public static void registerTextures(CustomLoader loader) {
		
		ResourceManager.registerUnkownTexture();
		
		GameTextures.putTexture(GameTextures.GAME_ICON, loader.registerTexture("", "game_icon"));
		
		Background.registerTextures(loader);
		
		GameTextures.putTexture(EEntity.STATIC, loader.registerTexture(FOLDER_ENTITY, FILE_STATIC_OBJECT));
		GameTextures.putTexture(EEntity.PLAYER, loader.registerTexture(FOLDER_ENTITY, FILE_PLAYER));
		
		BlueprintManager.getBlueprint(EEntity.STATIC).getEntityBase().getComponent(RenderingComponent.class)
					.setTexture(GameTextures.findTexture(EEntity.STATIC));
		
		BlueprintManager.getBlueprint(EEntity.PLAYER).getEntityBase().getComponent(RenderingComponent.class)
					.setTexture(GameTextures.findTexture(EEntity.PLAYER));
		
	}
	
}
