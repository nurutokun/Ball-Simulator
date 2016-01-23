package com.rawad.ballsimulator.client;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.renderengine.MasterRender;

public class Client {
	
	// 17:00, episode 427
	// MRN 150, 9:30 and at 11 am, Sept. 8
	
	private MasterRender masterRender;
	
	private Viewport viewport;
	
	private World world;
	
	private EntityPlayer player;
	
	private ClientNetworkManager networkManager;
	
	private PlayerInventory inventory;
	
	private boolean paused;
	private boolean showPauseScreen;
	
	public Client(MasterRender masterRender) {
		
		this.masterRender = masterRender;
		
		world = new World();
		
		player = new EntityPlayer(world);
		player.setName("Player" + (int) (new Random().nextDouble()*999));
		
		viewport = new Viewport(masterRender, world, player);
		
		networkManager = new ClientNetworkManager(this);
		
	}
	
	public void initGUI() {
		
		inventory = new PlayerInventory();
		
	}
	
	public void update(long timePassed) {
		
		handleKeyboardInput();
		
		handleMouseInput();
		
		if(!paused) {
			
			boolean updateGameLogic = !networkManager.isConnectedToServer();
			
			if(updateGameLogic) {
				world.update(timePassed);
				
				player.update(timePassed, MouseInput.getX(true) + (int) viewport.getCamera().getX(),
						MouseInput.getY(true) + (int) viewport.getCamera().getY());
				
			}
			
		}
		
		viewport.update(timePassed);
		
	}
	
	private void handleKeyboardInput() {
		
		boolean togglePause = KeyboardInput.isKeyDown(KeyEvent.VK_ESCAPE);
		
		if(togglePause) {
			
			setShowPauseScreen(!showPauseScreen());
			
			setPaused(showPauseScreen() || inventory.isShowing());
			
		}
		
		boolean toggleInventory = KeyboardInput.isKeyDown(KeyEvent.VK_E);
		
		if(!showPauseScreen() && toggleInventory) {
			
			inventory.toggle();
			
			setPaused(inventory.isShowing());
			
		}
		
		KeyboardInput.setConsumeAfterRequest(false);
		
		boolean up = KeyboardInput.isKeyDown(KeyEvent.VK_UP) | KeyboardInput.isKeyDown(KeyEvent.VK_W);
		boolean down = KeyboardInput.isKeyDown(KeyEvent.VK_DOWN) | KeyboardInput.isKeyDown(KeyEvent.VK_S);
		boolean right = KeyboardInput.isKeyDown(KeyEvent.VK_RIGHT) | KeyboardInput.isKeyDown(KeyEvent.VK_D);
		boolean left = KeyboardInput.isKeyDown(KeyEvent.VK_LEFT) | KeyboardInput.isKeyDown(KeyEvent.VK_A);
		
		KeyboardInput.setConsumeAfterRequest(true);
		
		if(!paused && (up || down || right || left)) {
			
			if(networkManager.isLoggedIn()) {
				networkManager.updatePlayerMovement(up, down, right, left);
			}
			
			// For buffered key press events.
			player.stopMoving();
			
			if(up) {
				player.moveUp();
			} else if(down) {
				player.moveDown();
			}
			
			if(right) {
				player.moveRight();
			} else if(left) {
				player.moveLeft();
			}
			
		} 
//		else if(!(pressedKeys.length > 0)) {
//			player.stopMoving();
//		}
		
		if(!paused) {
			viewport.handleKeyboardInput(KeyboardInput.isKeyDown(KeyEvent.VK_C, true), 
					KeyboardInput.isKeyDown(KeyEvent.VK_Z, true), 
					KeyboardInput.isKeyDown(KeyEvent.VK_X, true));
		}
		
	}
	
	private void handleMouseInput() {
		
		viewport.setCameraLocked(!MouseInput.isClamped());
		
	}
	
	public void onExit() {
		
		inventory.setVisible(false);
		
		setPaused(false);
		setShowPauseScreen(false);
		
	}
	
	public void render(Graphics g, int width, int height) {
		
		masterRender.render();
		
		g.drawImage(masterRender.getBuffer(), 0, 0, width, height, null);
		
	}
	
	public void connectToServer(String serverAddress) {
		
		networkManager.init(serverAddress);
		
	}
	
	public ClientNetworkManager getNetworkManager() {
		return networkManager;
	}
	
	public void setPaused(boolean paused) {
		
		if(inventory.isShowing()) {
			
			if(!paused) {// Resuming gameplay with inventory open; just keep game paused.
				return;
			}
			
		}
		
		this.paused = paused;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public void setShowPauseScreen(boolean showPauseScreen) {
		this.showPauseScreen = showPauseScreen;
	}
	
	public boolean showPauseScreen() {
		return showPauseScreen;
	}
	
	public void init(String terrainName) {
		
		Game game = GameManager.instance().getCurrentGame();
		
		CustomLoader loader = game.getLoader(game.toString());
		
		TerrainFileParser parser = game.getFileParser(TerrainFileParser.class);
		
		world.setTerrain(loader.loadTerrain(parser, terrainName));
		world.generateCoordinates(player);// TODO: Keep this marked for changing in future.
		
	}
	
	public PlayerInventory getPlayerInventory() {
		return inventory;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	public Viewport getViewport() {
		return viewport;
	}
	
	public void setWorld(World world) {
		this.world = world;
		
		viewport.setWorld(world);
	}
	
	public World getWorld() {
		return world;
	}
	
}
