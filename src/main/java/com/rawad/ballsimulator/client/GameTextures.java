package com.rawad.ballsimulator.client;

import java.util.HashMap;

import com.rawad.ballsimulator.client.gui.Background;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.game.entity.BlueprintManager;

import javafx.scene.image.Image;

public final class GameTextures {
	
	public static final Object TEXTURE_UNKNOWN = new Object();
	public static final Object TEXTURE_GAME_ICON = new Object();// Reserved for game icon texture.
	
	private static final HashMap<Object, Image> textures = new HashMap<Object, Image>();
	
	private static final String FOLDER_ENTITY = "entity";
	
	private static final String FILE_UNKNOWN_TEXTURE = "unknown";
	private static final String FILE_GAME_ICON = "game_icon";
	private static final String FILE_STATIC_OBJECT = "staticEntity";
	private static final String FILE_PLAYER = "player";
	
	private GameTextures() {}
	
	/**
	 * Loads all textures used in this {@code BallSimulator} game.
	 */
	public static void loadTextures(Loader loader) {
		
		textures.put(TEXTURE_UNKNOWN, loader.loadTexture("", FILE_UNKNOWN_TEXTURE));
		
		textures.put(EEntity.STATIC, loader.loadTexture(FOLDER_ENTITY, FILE_STATIC_OBJECT));
		textures.put(EEntity.PLAYER, loader.loadTexture(FOLDER_ENTITY, FILE_PLAYER));
		
		BlueprintManager.getBlueprint(EEntity.STATIC).getEntityBase().getComponent(RenderingComponent.class)
					.setTexture(GameTextures.findTexture(EEntity.STATIC));
		
		BlueprintManager.getBlueprint(EEntity.PLAYER).getEntityBase().getComponent(RenderingComponent.class)
					.setTexture(GameTextures.findTexture(EEntity.PLAYER));
		
		Background.loadTextures(loader);
		
	}
	
	public static void loadGameIcon(Loader loader) {
		textures.put(TEXTURE_GAME_ICON, loader.loadTexture("", FILE_GAME_ICON));
	}
	
	public static void deleteTextures() {
		for(Object textureKey: textures.keySet()) {
			textures.put(textureKey, null);
		}
	}
	
	public static Image findTexture(Object key) {
		return textures.getOrDefault(key, textures.get(TEXTURE_UNKNOWN));
	}
	
}
