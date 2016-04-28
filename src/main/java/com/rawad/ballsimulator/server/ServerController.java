package com.rawad.ballsimulator.server;

import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.server.world.WorldMP;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.server.IServerController;

import javafx.concurrent.Task;

public class ServerController implements IServerController {
	
	private Server server;
	
	private WorldMP world;
	
	public ServerController(Server server) {
		super();
		
		this.server = server;
		
		world = new WorldMP(server);
		
	}
	
	public void init(Game game) {
		
		CustomLoader loader = game.getLoader(CustomLoader.class);
		
		TerrainFileParser parser = game.getFileParser(TerrainFileParser.class);
		
		server.addTask(new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				
				Logger.log(Logger.DEBUG, "Loading terrain...");
				world.setTerrain(loader.loadTerrain(parser, Server.TERRAIN_NAME));
				Logger.log(Logger.DEBUG, "Terrain loaded successfully.");
				
				return 0;
			}
		});
		
	}
	
	@Override
	public void tick() {
		
		world.update();
		
	}
	
	public WorldMP getWorld() {
		return world;
	}
	
}
