package com.rawad.ballsimulator.client.gamestates;

import java.util.ArrayList;
import java.util.Random;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.client.gui.Messenger;
import com.rawad.ballsimulator.entity.Entity;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.gamestates.State;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.renderengine.BackgroundRender;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

public class MultiplayerGameState extends State {
	
	private CustomLoader loader;
	private TerrainFileParser terrainParser;
	private SettingsFileParser settingsParser;
	
	@FXML private Messenger mess;
	@FXML private Label lblConnectingMessage;
	
	private ClientNetworkManager networkManager;
	
	private Viewport viewport;
	
	private World world;
	private Camera camera;
	
	private EntityPlayer player;
	
	// TODO: Fix major update lag somewhere in here... Seems like something on EDT causes spike every so often (not just here)
	public MultiplayerGameState(ClientNetworkManager networkManager) {
		super();
		
		this.networkManager = networkManager;
		networkManager.setController(this);
		
		viewport = new Viewport();
		
		world = new World();
		
		player = new EntityPlayer(world);
		player.setName("Player" + (int) (new Random().nextDouble()*999));
		
		camera = new Camera();
		camera.setScale(1d/2d, 1d/2d);
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			switch(keyEvent.getCode()) {
			
			case E:
				break;
			
			default:
				break;
				
			}
			
		});
		
	}
	
	@Override
	public void tick() {
		
		if(networkManager.isLoggedIn()) {// Start updating world, player and camera once login is successful
			
			world.update();
			
			camera.update(player.getX(), player.getY(), world.getWidth(), world.getHeight(), 0, 0, 
					Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
			
			viewport.update(world, camera);
			
		}
		
	}
	
	@Override
	public void render() {
		super.render();
		
		if(networkManager.isLoggedIn()) {
			viewport.render(canvas, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		} else {
			BackgroundRender.instance().render(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight());
		}
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		Game game = sm.getGame();
		
		loader = game.getLoader(CustomLoader.class);
		
		terrainParser = game.getFileParser(TerrainFileParser.class);
		settingsParser = game.getFileParser(SettingsFileParser.class);
		
		loader.loadSettings(settingsParser, game.getSettingsFileName());
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				networkManager.init(settingsParser.getIp());// TODO: Separate this.
				
			}
			
		}, "MP Connection Thread").start();
		
		String text = "Connecting To " + settingsParser.getIp() + " ...";
		
		Logger.log(Logger.DEBUG, text);
		lblConnectingMessage.setText(text);
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		networkManager.requestDisconnect();
		
		mess.setVisible(false);
		
	}
	
	public void onConnect() {
		mess.setVisible(true);
	}
	
	public void onDisconnect() {
		
		ArrayList<Entity> entities = world.getEntities();
		ArrayList<Entity> entitiesToRemove = new ArrayList<Entity>();
		
		for(Entity e: entities) {
			
			if(e instanceof EntityPlayer && !e.equals(player)) {
				entitiesToRemove.add(e);
			}
			
		}
		
		for(Entity e: entitiesToRemove) {
			
			world.removeEntity(e);
			
		}
		
		sm.requestStateChange(MenuState.class);
		
	}
	
	public void loadTerrain(String terrainName) {
		world.setTerrain(loader.loadTerrain(terrainParser, terrainName));
	}
	
	public World getWorld() {
		return world;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
}
