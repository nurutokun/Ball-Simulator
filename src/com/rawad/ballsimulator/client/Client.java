package com.rawad.ballsimulator.client;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.rawad.ballsimulator.client.gamestates.EState;
import com.rawad.ballsimulator.client.gamestates.GameState;
import com.rawad.ballsimulator.client.gamestates.MenuState;
import com.rawad.ballsimulator.client.gamestates.MultiplayerGameState;
import com.rawad.ballsimulator.client.gamestates.OptionState;
import com.rawad.ballsimulator.client.gamestates.WorldEditorState;
import com.rawad.ballsimulator.client.gui.entity.item.ItemSlot;
import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.fileparser.SettingsFileParser;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.display.SuperContainer;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.IController;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.gamestates.LoadingState;
import com.rawad.gamehelpers.gamestates.StateManager;
import com.rawad.gamehelpers.gui.Background;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.gui.TextLabel;
import com.rawad.gamehelpers.input.Mouse;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.ResourceManager;

// TODO: Could make the whole "rotating base", "moving base", etc. just a bunch of interfaces; for the player that 
// is. Could also make it component based (one entity class and just add the different components you want it to 
// have, e.g. moving, rotating, health). Keep in mind having it mobile-portable.
public class Client extends Proxy {
	
	// 17:00, episode 427
	// MRN 150, 9:30 and at 11 am, Sept. 8
	
	private static final String DEFAULT_FONT = "Y2K Neophyte";
	
	private Game game;
	
	private StateManager sm;
	
	private ClientNetworkManager networkManager;
	
	private LoadingState loadingState;
	
	public Client() {
		
	}
	
	@Override
	public void initGUI() {
		
		DisplayManager.init(game.toString());
		
		SuperContainer container = DisplayManager.getContainer();
		
		container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F11"), "doFullscreen");
		container.getActionMap().put("doFullscreen", new AbstractAction() {
			
			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = 6907263934985705439L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(DisplayManager.getDisplayMode() == DisplayManager.Mode.FULLSCREEN) {
					DisplayManager.requestDisplayModeChange(DisplayManager.Mode.WINDOWED);
				} else {
					DisplayManager.requestDisplayModeChange(DisplayManager.Mode.FULLSCREEN);
				}
				
			}
			
		});
		
		container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F3"), "doDebug");
		container.getActionMap().put("doDebug", new AbstractAction() {
			
			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = 6907263934985705439L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				game.setDebug(!game.isDebug());
				
			}
			
		});
		
		setLookAndFeel();
		
		sm.requestStateChange(LoadingState.class);
		
		DisplayManager.showDisplayMode(DisplayManager.Mode.WINDOWED);
		
	}
	
	private void setLookAndFeel() {
		
		boolean setLookAndFeel = false;
		
		try {
			
			for(LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()) {
				
				if("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					
					setLookAndFeel = true;
					
					break;
				}
				
			}
			
			if(!setLookAndFeel) {
				throw new Exception("Couldn't set Nimbus Look and Feel!");
			}
			
		} catch(Exception e) {
			
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException ex) {
				
				Logger.log(Logger.WARNING, "Couldn't load system look and feel; " + ex.getMessage() 
						+ ". Using default look and feel instead.");
				
			}
			
		}
		
		GameHelpersLoader gameHelpersLoader = game.getLoader(GameHelpersLoader.class);
		
		try {
			
			Font f = Font.createFont(Font.PLAIN, gameHelpersLoader.loadFontFile(DEFAULT_FONT)).deriveFont(13f);
			
			LookAndFeel laf = UIManager.getLookAndFeel();
			laf.getDefaults().put("defaultFont", f);
			
		} catch (FontFormatException | IOException ex) {
			ex.printStackTrace();
			
			Logger.log(Logger.DEBUG, "Couln't load font: \"" + DEFAULT_FONT + "\"");
			
		}
		
	}
	
	private void initResources() {
		
		GameHelpersLoader ghLoader = game.getLoader(GameHelpersLoader.class);
		CustomLoader loader = game.getLoader(CustomLoader.class);
		
		game.registerTextures();
		
		ResourceManager.getTextureObject(game.getIconLocation()).setOnloadAction(new Runnable() {
			
			@Override
			public void run() {
				DisplayManager.setIcon(ResourceManager.getTexture(game.getIconLocation()));
			}
			
		});
		
		Background.registerTextures(ghLoader, game);
		
		Button.registerTextures(ghLoader);
		DropDown.registerTextures(ghLoader);
		TextLabel.registerTextures(ghLoader);
		
		EntityPlayer.registerTextures(loader);
		ItemSlot.registerTextures(loader);
		
	}
	
	@Override
	public void init(Game game) {
		
		this.game = game;
		
		SettingsFileParser settings = game.getFileParser(SettingsFileParser.class);
		
		CustomLoader loader = game.getLoader(CustomLoader.class);
		
		loader.loadSettings(settings, game.getSettingsFileName());
		
		DisplayManager.changeFullScreenResolution(settings.getFullScreenResolution());
		
		networkManager = new ClientNetworkManager();
		
		sm = new StateManager(game);
		
		loadingState = new LoadingState(new Runnable() {
			
			@Override
			public void run() {
				
				initResources();
				
				ResourceManager.loadAllTextures();
				
				sm.requestStateChange(EState.MENU);
				
			}
			
		});
		
		sm.addState(loadingState);
		sm.addState(new MenuState());
		sm.addState(new GameState());
		sm.addState(new OptionState());
		sm.addState(new WorldEditorState());
		sm.addState(new MultiplayerGameState(networkManager));
		
	}
	
	@Override
	public void tick() {
		
		IController controller = getController();
		
		if(controller != null) {
			
			controller.tick();
			
		}
		
		sm.update();
		
		Mouse.update(DisplayManager.getContainer());
		
		DisplayManager.update();
		
	}
	
	@Override
	public void stop() {
		
		sm.stop();
		
		DisplayManager.destroyWindow();
		
		game.setBackground(null);// For proper resetting of overall "game state".
		
	}
	
	public void connectToServer(String serverAddress) {
		
		networkManager.init(serverAddress);
		
	}
	
	public ClientNetworkManager getNetworkManager() {
		return networkManager;
	}
	
}