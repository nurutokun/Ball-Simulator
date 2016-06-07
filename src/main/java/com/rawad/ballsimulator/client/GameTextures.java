package com.rawad.ballsimulator.client;

import java.util.HashMap;

import com.rawad.ballsimulator.client.gui.Background;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.BlueprintManager;
import com.rawad.gamehelpers.resources.ResourceManager;

public final class GameTextures {
	
	private static final HashMap<Object, Integer> textures = new HashMap<Object, Integer>();
	
	private static final String FOLDER_ENTITY = "entity";
	
	private static final String FILE_STATIC_OBJECT = "staticEntity";
	private static final String FILE_PLAYER = "player";
	
	private GameTextures() {}
	
	/**
	 * Registers all textures used in this {@code BallSimulator} game.
	 * 
	 * @param game
	 */
	public static void registerTextures(Game game) {
		
		ResourceManager.registerUnkownTexture();
		
		game.registerTextures();
		
		CustomLoader loader = game.getLoader(CustomLoader.class);
		
		Background.registerTextures(loader);
		
		textures.put(EEntity.STATIC, loader.registerTexture(FOLDER_ENTITY, FILE_STATIC_OBJECT));
		textures.put(EEntity.PLAYER, loader.registerTexture(FOLDER_ENTITY, FILE_PLAYER));
		
		BlueprintManager.getBlueprint(EEntity.STATIC).getEntityBase().getComponent(RenderingComponent.class)
					.setTexture(findTexture(EEntity.STATIC));
		
	}
	
	/**
	 * @param id {@code EEntity} object.
	 * @return
	 * 
	 * @see com.rawad.ballsimulator.entity.EEntity
	 * 
	 */
	public static int findTexture(Object id) {
		return textures.get(id);
	}
	
}
