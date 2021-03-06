package com.rawad.ballsimulator.loader;

import java.io.BufferedReader;

import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.fileparser.ControlsFileParser;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.game.World;
import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.entity.Blueprint;
import com.rawad.gamehelpers.game.entity.BlueprintManager;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.AbstractLoader;
import com.rawad.jfxengine.client.input.InputBindings;

import javafx.concurrent.Task;
import javafx.scene.image.Image;

public class Loader extends AbstractLoader {
	
	private static final String FOLDER_RES = "res";
	private static final String FOLDER_MISC = "files";
	private static final String FOLDER_TERRAIN = "terrains";
	private static final String FOLDER_TEXTURE = "textures";
	private static final String FOLDER_ENTITY = "entity";
	
	private static final String EXTENSION_SETTINGS_FILE = "txt";
	private static final String EXTENSION_INPUTS_FILE = "txt";
	private static final String EXTENSION_TERRAIN_FILE = "txt";
	private static final String EXTENSION_TEXTURE_FILE = "png";
	private static final String EXTENSION_ENTITY_BLUEPRINT_FILE = "xml";
	
	private static final String PROTOCOL_FILE = "file:";
	
	public Loader() {
		super(FOLDER_RES);// Everything to be loaded is in FOLDER_RES.
	}
	
	public Image loadTexture(String subTexturesFolder, String textureName) {
		
		String fullPath = getFilePathFromParts(EXTENSION_TEXTURE_FILE, FOLDER_TEXTURE, subTexturesFolder, textureName);
		
		return new Image(PROTOCOL_FILE + fullPath);
		
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
	
	public void saveTerrain(TerrainFileParser parser, World world, String terrainName) {
		
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
	
	public void loadInputBindings(ControlsFileParser parser, InputBindings inputBindings, String inputBindingsName) {
		
		BufferedReader reader = readFile(EXTENSION_INPUTS_FILE, FOLDER_MISC, inputBindingsName);
		
		parser.setInputBindings(inputBindings);
		
		parser.parseFile(reader);
		
	}
	
	public void saveInputBindings(ControlsFileParser parser, InputBindings inputBindings, String inputBindingsName) {
		
		parser.setInputBindings(inputBindings);
		
		saveFile(parser.getContent(), EXTENSION_INPUTS_FILE, FOLDER_MISC, inputBindingsName);
		
	}
	
	public static Task<Void> getEntityBlueprintLoadingTask(Loader loader, EntityFileParser parser) {
		
		final String[] contextPaths = {
				EEntity.class.getPackage().getName(),
		};
		
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				Logger.log(Logger.DEBUG, "Loading entity blueprints...");
				
				try {
					
					for(EEntity entityKey: EEntity.values()) {
						
						String entityName = entityKey.getName();
						
						Logger.log(Logger.DEBUG, "Loading " + entityName + "...");
						
						BlueprintManager.addBlueprint(entityKey, loader.loadEntityBlueprint(parser, entityName, 
								contextPaths));// Make sure this works.
						
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
