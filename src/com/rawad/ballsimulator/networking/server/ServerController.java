package com.rawad.ballsimulator.networking.server;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import com.rawad.ballsimulator.client.Camera;
import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.fileparser.TerrainFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.server.world.WorldMP;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.IController;
import com.rawad.gamehelpers.input.EventHandler;
import com.rawad.gamehelpers.input.KeyboardInput;

public class ServerController implements IController {
	
	private Viewport viewport;
	
	private WorldMP world;
	private Camera camera;
	
	private JPanel panel;
	
	/**
	 * If true, world scale is 1:1 and manual movement will be activated; false and the world will be scaled to 
	 * the JPanel's dimensions.
	 */
	private boolean freeRoam;
	
	public ServerController(Server server) {
		super();
		
		server.setController(this);
		
		viewport = new Viewport();
		
		world = new WorldMP(server);
		
		camera = new Camera();
		
	}
	
	public void init(Game game) {
		
		CustomLoader loader = game.getLoader(CustomLoader.class);
		
		TerrainFileParser parser = game.getFileParser(TerrainFileParser.class);
		
		world.setTerrain(loader.loadTerrain(parser, Server.TERRAIN_NAME));
		
	}
	
	public void initGUI() {
		
		panel = new JPanel() {
			
			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = -2677329891767235880L;
			
			{
				
				addKeyListener(EventHandler.instance());
				
			}
			
			@Override
			protected void paintComponent(Graphics graphics) {
				
				Graphics2D g = (Graphics2D) graphics;
				
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				super.paintComponent(graphics);
				
				viewport.drawScene(g, panel.getWidth(), panel.getHeight());
				
				graphics.dispose();
				g.dispose();
				
			}
			
		};
		
	}
	
	@Override
	public void tick() {
		
		handleKeyboardInput();
		
		world.update();
		
		viewport.update(world, camera);
		
		viewport.render();
		
		panel.repaint();
		
	}
	
	@Override
	public void handleMouseInput() {
		
	}
	
	@Override
	public void handleKeyboardInput() {
		
		int dx = getDelta(KeyEvent.VK_D, KeyEvent.VK_A) | getDelta(KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT);
		int dy = getDelta(KeyEvent.VK_S, KeyEvent.VK_W) | getDelta(KeyEvent.VK_DOWN, KeyEvent.VK_UP);
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_C, true)) {
			freeRoam = !freeRoam;
		}
		
		int maxWidth = Game.SCREEN_WIDTH;
		int maxHeight = Game.SCREEN_HEIGHT;
		
		if(freeRoam) {
			camera.setScale(1, 1);
			camera.update(
					camera.getX() + ((double) maxWidth / camera.getXScale() / 2d) + dx, 
					camera.getY() + ((double) maxHeight / camera.getYScale() / 2d) + dy, 
					world.getWidth(), world.getHeight(), 0, 0, maxWidth, maxHeight);
		} else {
			camera.setScale(
					(double) maxWidth / (double) world.getWidth(), 
					(double) maxHeight / (double) world.getHeight());
			camera.update(0, 0, world.getWidth(), world.getHeight(), 0, 0, maxWidth, maxHeight);
		}
		
	}
	
	private int getDelta(int keyPositive, int keyNegative) {
		
		int delta = 0;
		
		if(KeyboardInput.isKeyDown(keyPositive, false)) {
			delta = 5;
		}
		
		if(KeyboardInput.isKeyDown(keyNegative, false)) {
			delta = -5;
		}
		
		return delta;
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public WorldMP getWorld() {
		return world;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public void setFreeRoam(boolean freeRoam) {
		this.freeRoam = freeRoam;
	}
	
	public boolean isFreeRoam() {
		return freeRoam;
	}
	
}
