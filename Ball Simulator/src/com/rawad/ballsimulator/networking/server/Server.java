package com.rawad.ballsimulator.networking.server;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.rawad.ballsimulator.client.Viewport;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.ballsimulator.networking.server.entity.EntityPlayerMP;
import com.rawad.ballsimulator.networking.server.main.ViewportShell;
import com.rawad.ballsimulator.networking.server.main.WindowManager;
import com.rawad.ballsimulator.networking.server.world.WorldMP;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.log.Logger;

public class Server {
	
	public static final int PORT = 8008;
	
	public static final String TERRAIN_NAME = "terrain";
	
	private ServerNetworkManager networkManager;
	
	private ViewportShell viewportShell;
	
	private WorldMP world;
	
	private Thread mainLooper;
	
	private Object lock;
	
	private boolean running;
	
	public Server() { 
		
		networkManager = new ServerNetworkManager(this);
		
		world = new WorldMP(this);
		
		world.setTerrain(Loader.loadTerrain(TERRAIN_NAME));
		
		viewportShell = new ViewportShell(new Viewport(world));
		
		mainLooper = new Thread(new Looper(), "Looper");
		
		lock = new Object();
		
	}
	
	public void start() {
		// Initialize server stuff
		
		running = true;
		
		mainLooper.start();
		
		networkManager.init();
		
	}
	
	private void update(long timePassed) {
		
		synchronized(lock) {// ... maybe ... ?
			
			int dx = getDelta(KeyEvent.VK_D, KeyEvent.VK_A) | getDelta(KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT);
			int dy = getDelta(KeyEvent.VK_S, KeyEvent.VK_W) | getDelta(KeyEvent.VK_DOWN, KeyEvent.VK_UP);
			
			if(KeyboardInput.isKeyDown(KeyEvent.VK_C, true)) {
				viewportShell.setFreeRoam(!viewportShell.isFreeRoam());
			}
			
			world.update(timePassed);
			
			viewportShell.update(timePassed, dx, dy);
			
			viewportShell.repaint();
			
			if(WindowManager.instance().getConsoleOutput() != null) {
				WindowManager.instance().addDebugText(Logger.getBuffer());
			}
			
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
	
	private class Looper implements Runnable {
		
		@Override
		public void run() {
			
			long prevTime = System.currentTimeMillis();
			
			while(running) {
				
				long curTime = System.currentTimeMillis();
				
				long timePassed = curTime - prevTime;
				
				update(timePassed);
				
				prevTime = curTime;
				
				try {
					Thread.sleep(1000/DisplayManager.REFRESH_RATE);
				} catch(Exception ex) {
					ex.printStackTrace();
					break;
				}
				
			}
		}
		
	}
	
	public void updatePlayerNamesList() {
		
		ArrayList<EntityPlayerMP> players = world.getPlayers();
		
		String[] names = new String[players.size()];
		
		for(int i = 0; i < players.size(); i++) {
			
			names[i] = players.get(i).getName();
			
		}
		
		WindowManager.instance().setPlayerNames(names);
		
	}
	
	public ServerNetworkManager getNetworkManager() {
		return networkManager;
	}
	
	public ViewportShell getViewportShell() {
		return viewportShell;
	}
	
	public WorldMP getWorld() {
		return world;
	}
	
	public boolean isRunning() {
		return running;
	}
	
}
