package com.rawad.ballsimulator.loader;

import java.io.BufferedReader;
import java.net.URL;
import java.util.HashMap;

import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.BallSimulator;
import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.entity.Blueprint;
import com.rawad.gamehelpers.game.entity.BlueprintManager;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ALoader;

import javafx.concurrent.Task;
import javafx.scene.image.Image;

public class Loader extends ALoader {
	
	private static final String FOLDER_RES = "res";
	private static final String FOLDER_MISC = "files";
	private static final String FOLDER_TERRAIN = "terrains";
	private static final String FOLDER_TEXTURE = "textures";
	private static final String FOLDER_ENTITY = "entity";
	
	private static final String EXTENSION_SETTINGS_FILE = "txt";
	private static final String EXTENSION_TERRAIN_FILE = "txt";
	private static final String EXTENSION_TEXTURE_FILE = "png";
	private static final String EXTENSION_LAYOUT_FILE = "fxml";
	private static final String EXTENSION_STYLESHEET_FILE = "css";
	private static final String EXTENSION_ENTITY_BLUEPRINT_FILE = "xml";
	
	public Loader() {
		super(BallSimulator.NAME, FOLDER_RES);
	}
	
	public Image loadTexture(String subTexturesFolder, String textureName) {
		
		String path = getFilePathFromParts(EXTENSION_TEXTURE_FILE, FOLDER_TEXTURE, FOLDER_ENTITY, textureName);
		
		return new Image(path);
		
	}
	
	public void loadSettings(SettingsFileParser parser, String settingsFile) {
		
		BufferedReader reader = readFile(EXTENSION_SETTINGS_FILE, FOLDER_MISC, settingsFile);
		
		parser.parseFile(reader);
		
	}
	
	public void saveSettings(SettingsFileParser parser, String settingsFile) {
		
		saveFile(parser.getContent(), EXTENSION_SETTINGS_FILE, FOLDER_MISC, settingsFile);
		
	}
	
	public BufferedReader readTerrainFile(String terrainName) {
		return readFile(EXTENSION_TERRAIN_FILE, FOLDER_TERRAIN, terrainName);
	}
	
	public void saveTerrainFile(String content, String terrainName) {
		saveFile(content, EXTENSION_TERRAIN_FILE, FOLDER_TERRAIN, terrainName);
	}
	
	public void loadTerrain(TerrainFileParser parser, World world, String terrainName) {
		
		BufferedReader reader = readFile(EXTENSION_TERRAIN_FILE, FOLDER_TERRAIN, terrainName);
		
		parser.setWorld(world);
		
		parser.parseFile(reader);
		
	}
	
	public void saveTerrain(World world, TerrainFileParser parser, String terrainName) {
		
		parser.setWorld(world);
		
		saveTerrainFile(parser.getContent(), terrainName);
		
	}
	
	public Blueprint loadEntityBlueprint(EntityFileParser parser, String entityName, String... contextPaths) {
		
		BufferedReader reader = readFile(getEntityBlueprintPath(entityName));
		
		parser.setContextPaths(contextPaths);
		
		parser.parseFile(reader);
		
		Blueprint blueprint = new Blueprint(parser.getEntity());
		
		return blueprint;
		
	}
	
	public String getEntityBlueprintPath(String entityName) {
		return getFilePathFromParts(EXTENSION_ENTITY_BLUEPRINT_FILE, FOLDER_ENTITY, entityName);
	}
	
	/**
	 * Assumes the fxml file is in the same package as the the {@code clazz} given. <b>Note:</b> can be in completely 
	 * different src file.
	 * 
	 * @param clazz
	 * @param fileName
	 * @return
	 */
	public static URL getFxmlLocation(Class<? extends Object> clazz, String fileName) {
		return clazz.getResource(fileName + EXTENSION_LAYOUT_FILE);
	}
	
	/**
	 * Assumes the name of the fxml file is that of the given {@code clazz}.
	 * 
	 * @param clazz
	 * @return
	 */
	public static URL getFxmlLocation(Class<? extends Object> clazz) {
		return Loader.getFxmlLocation(clazz, clazz.getSimpleName());
	}
	
	public static String getStyleSheetLocation(Class<? extends Object> clazz, String styleSheetName) {
		return clazz.getResource(styleSheetName + EXTENSION_STYLESHEET_FILE).toExternalForm();
	}
	
	public static Task<Void> getEntityBlueprintLoadingTask(Loader loader, EntityFileParser parser) {
		
		final String[] contextPaths = {
				EEntity.class.getPackage().getName(),
		};
		
		final HashMap<Object, String> entityBindings = new HashMap<Object, String>();
		
		for(EEntity entity: EEntity.values()) {
			entityBindings.put(entity, entity.getName());
		}
		
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				Logger.log(Logger.DEBUG, "Loading entity blueprints...");
				
				try {
					
					for(Object entityKey: entityBindings.keySet()) {
						
						String entityName = entityBindings.get(entityKey);
						
						BlueprintManager.addBlueprint(entityKey, loader.loadEntityBlueprint(parser, entityName, 
								contextPaths));
						
					}
					
					Logger.log(Logger.DEBUG, "Loaded all entity blueprints.");
					
				} catch(Exception ex) {
					Logger.log(Logger.WARNING, "Entity blueprint loading failed");
					ex.printStackTrace();
				}
				
				return null;
				
			}
		};
		
	}
	
}
