package com.rawad.ballsimulator.client;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.world.World;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;

public class Client {
	
	private Viewport viewport;
	
	private World world;
	
	private EntityPlayer player;
	
	private ClientNetworkManager networkManager;
	
	private boolean paused;
	
	public Client() {
		
		world = new World();
		
		player = new EntityPlayer(world);
		player.setName("Player" + (int) (new Random().nextDouble()*999));
		
		viewport = new Viewport(world, player);
		
		networkManager = new ClientNetworkManager(this);
		
	}
	
	public void update(KeyboardEvent ke, long timePassed) {
		
		if(!paused) {
			
			if(!ke.isConsumed()) {
				handleKeyboardInput(ke);
			}
			
			handleMouseInput();
			
			boolean updateGameLogic = !networkManager.isConnectedToServer();
			
			if(updateGameLogic) {
				world.update(timePassed);
				
				player.update(timePassed, new MouseEvent(MouseInput.getX() + (int) viewport.getCamera().getX(),
						MouseInput.getY() + (int) viewport.getCamera().getY(), MouseInput.LEFT_MOUSE_BUTTON));
				
			}
			
			viewport.update(timePassed);
			
		}
		
	}
	
	private void handleKeyboardInput(KeyboardEvent e) {

		KeyboardInput.setConsumeAfterRequest(false);
		
		boolean up = KeyboardInput.isKeyDown(KeyEvent.VK_UP) | KeyboardInput.isKeyDown(KeyEvent.VK_W);
		boolean down = KeyboardInput.isKeyDown(KeyEvent.VK_DOWN) | KeyboardInput.isKeyDown(KeyEvent.VK_S);
		boolean right = KeyboardInput.isKeyDown(KeyEvent.VK_RIGHT) | KeyboardInput.isKeyDown(KeyEvent.VK_D);
		boolean left = KeyboardInput.isKeyDown(KeyEvent.VK_LEFT) | KeyboardInput.isKeyDown(KeyEvent.VK_A);
		
		KeyboardInput.setConsumeAfterRequest(true);
		
		if(up || down || right || left) {
			
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
			
			// U-Pass: UCU, room 07 (SFUO office) august 18 - september 18, becomes active september 1
		} 
//		else if(!(pressedKeys.length > 0)) {
//			player.stopMoving();
//		}
		
		if(!e.isConsumed()) {
			viewport.handleKeyboardInput(KeyboardInput.isKeyDown(KeyEvent.VK_E, true), KeyboardInput.isKeyDown(KeyEvent.VK_Q, true), 
					KeyboardInput.isKeyDown(KeyEvent.VK_R, true));
		}
		
	}
	
	private void handleMouseInput() {
		
		viewport.setCameraLocked(!MouseInput.isClamped());
		
	}
	
	public void render(Graphics2D g) {
		
		viewport.render(g);
		
	}
	
	public void connectToServer(String serverAddress) {
		
		networkManager.init(serverAddress);
		
	}
	
	public ClientNetworkManager getNetworkManager() {
		return networkManager;
	}
	
	public void pauseGame(boolean paused) {
		this.paused = paused;
	}
	
	public void init() {
		
		loadTerrain("terrain");
		world.generateCoordinates(player);// TODO: Keep this marked for changing in future.
		
	}
	
	public void loadTerrain(String terrainName) {
		world.setTerrain(Loader.loadTerrain(terrainName));
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	public World getWorld() {
		return world;
	}
	
}
