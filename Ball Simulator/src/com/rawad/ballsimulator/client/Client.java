package com.rawad.ballsimulator.client;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.rawad.ballsimulator.client.gui.entity.player.PlayerInventory;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gui.GuiManager;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.renderengine.MasterRender;
import com.rawad.gamehelpers.renderengine.gui.GuiRender;

public class Client {
	
	// 17:00, episode 427
	// MRN 150, 9:30 and at 11 am, Sept. 8
	
	private GuiRender guiRender;
	
	private Viewport viewport;
	
	private World world;
	
	private EntityPlayer player;
	
	private ClientNetworkManager networkManager;
	
	private GuiManager guiManager;
	
	private PlayerInventory inventory;
	
	private boolean paused;
	private boolean showPauseScreen;
	
	public Client(MasterRender masterRender) {
		
		this.guiRender = (GuiRender) masterRender.getRender(GuiRender.class);
		
		world = new World();
		
		player = new EntityPlayer(world);
		player.setName("Player" + (int) (new Random().nextDouble()*999));
		
		viewport = new Viewport(masterRender, world, player);
		
		networkManager = new ClientNetworkManager(this);
		
		guiManager = new GuiManager();// Could make this an argument.
		
		inventory = new PlayerInventory("Player Inventory", 
				Game.SCREEN_WIDTH/2, Game.SCREEN_HEIGHT/2);
		
		guiManager.addComponent(inventory);
		
	}
	
	public void update(MouseEvent me, KeyboardEvent ke, long timePassed) {
		
		guiManager.update(me, ke);
		
		if(!ke.isConsumed()) {
			handleKeyboardInput(ke);
		}
		
		if(!me.isConsumed()) {
			handleMouseInput(me);
		}
		
		if(!paused) {
			
			boolean updateGameLogic = !networkManager.isConnectedToServer();
			
			if(updateGameLogic) {
				world.update(timePassed);
				
				player.update(timePassed, new MouseEvent(MouseInput.getX() + (int) viewport.getCamera().getX(),
						MouseInput.getY() + (int) viewport.getCamera().getY()));
				
			}
			
		}
		
		viewport.update(timePassed);
		
		guiRender.addGuiComponents(guiManager.getComponents());
		
	}
	
	private void handleKeyboardInput(KeyboardEvent e) {
		
		boolean togglePause = KeyboardInput.isKeyDown(KeyEvent.VK_ESCAPE);
		
		if(togglePause) {
			
			setShowPauseScreen(!showPauseScreen());
			
			setPaused(showPauseScreen() || inventory.isShowing());
			
			e.consume();
			
		}
		
		boolean toggleInventory = KeyboardInput.isKeyDown(KeyEvent.VK_E);
		
		if(!showPauseScreen() && toggleInventory) {
			
			inventory.toggle();
			
			setPaused(inventory.isShowing());
			
			e.consume();
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
			
			e.consume();
			
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
	
	private void handleMouseInput(MouseEvent e) {
		
		viewport.setCameraLocked(!MouseInput.isClamped());
		
	}
	
	public void onExit() {
		
		inventory.hide();
		
		setPaused(false);
		setShowPauseScreen(false);
		
	}
	
	public void render(Graphics2D g) {
		
		viewport.render(g);
		
		guiManager.render(g);
		
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
	
	public void init() {
		
		loadTerrain("terrain");
		world.generateCoordinates(player);// TODO: Keep this marked for changing in future.
		
	}
	
	public void loadTerrain(String terrainName) {
		viewport.loadTerrain(terrainName);
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	public Viewport getViewport() {
		return viewport;
	}
	
	public World getWorld() {
		return world;
	}
	
}
