package com.rawad.ballsimulator.networking.server.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.world.WorldRender;
import com.rawad.ballsimulator.main.BallSimulator;
import com.rawad.ballsimulator.networking.server.Server;
import com.rawad.ballsimulator.networking.server.tcp.SPacket03Message;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.input.EventHandler;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.renderengine.MasterRender;
import com.rawad.gamehelpers.utils.Util;

public class WindowManager {

	private static WindowManager instance;

	private final Server server;
	
	private ViewportShell panel;
	
	private JFrame frame;
	private JPanel basePanel;
	
	private JSplitPane serverInfoPanel;
	private JPanel consolePanel;
	private JTextArea consoleOutput;
	private JTextField consoleInputTextField;
	private JSplitPane splitPane;
	private JList<String> playerList;
	private JScrollPane consoleOutputHolder;
	
	private JMenuBar menuBar;
	private JMenu menuOptions;
	private JCheckBoxMenuItem menuCheckboxDebug;
	
	public static void main(String[] args) {

		instance = instance();
		
		Util.invokeAndWait(new Runnable() {
			
			public void run() {

				try {
					
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception ex) {
						Logger.log(Logger.WARNING, "Couldn't load native system's look and feel.");
					}
					
					instance.initialize(instance.server);
					instance.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		instance.server.start();// Wait for server application to be visible
		// before starting the grunt workers...
		
	}

	private WindowManager() {
		
		BallSimulator game = new BallSimulator();
		
		game.serverInit();
		
		MasterRender render = game.getMasterRender();
		
		render.registerRender(new WorldRender());// Because we are rendering the scene on the server as well...
		render.registerRender(new DebugRender());
		
		GameManager.instance().registerGame(game);
		// For things that need them, mainly for the isDebug() method right now.
		// Also, this shouldn't waste any extra resourses because the init
		// method for the game isn't being called. It will cause the Icon to be
		// loaded though...
		
		server = new Server();
		// Here because game needs to initialize the fileparsers so that terrain can be loaded here
		
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 */
	public void initialize(Server server) {
		
		server.initGUI();
		
		frame = new JFrame();
		frame.setTitle(BallSimulator.NAME);
		frame.setPreferredSize(new Dimension(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT));// Is okay?
		frame.setIconImage(GameManager.instance().getCurrentGame().getIcon());
		frame.addWindowListener(EventHandler.instance());
		
		basePanel = new JPanel();
		basePanel.setBackground(Color.WHITE);
		basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.X_AXIS));
		frame.add(basePanel);

		splitPane = new JSplitPane();
		splitPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		splitPane.setResizeWeight(0.5);
		splitPane.setOneTouchExpandable(true);
		splitPane.setAlignmentY(Component.CENTER_ALIGNMENT);
		splitPane.setContinuousLayout(true);
		basePanel.add(splitPane);

		serverInfoPanel = new JSplitPane();
		serverInfoPanel.setContinuousLayout(true);
		serverInfoPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		serverInfoPanel.setResizeWeight(0.5);
		serverInfoPanel.setOneTouchExpandable(true);
		serverInfoPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(serverInfoPanel);
		
		// panel = new ViewportShell(null);
		panel = server.getViewportShell();
		panel.setBorder(new TitledBorder(null, "Server World",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		serverInfoPanel.setTopComponent(panel);
		
		playerList = new JList<String>();
		playerList.setToolTipText("Players");
		serverInfoPanel.setRightComponent(playerList);
		playerList.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Player List",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		
		consolePanel = new JPanel();
		consolePanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		splitPane.setRightComponent(consolePanel);
		consolePanel.setLayout(new BorderLayout(0, 0));
		
		consoleInputTextField = new JTextField();
		consoleInputTextField.setDropMode(DropMode.INSERT);
		consoleInputTextField.addActionListener(new ConsoleInputTextFieldActionListener());
		
		consoleInputTextField.setToolTipText("Input Commands Here");
		consolePanel.add(consoleInputTextField, BorderLayout.SOUTH);
		
		consoleOutputHolder = new JScrollPane();
		consoleOutputHolder.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		consoleOutputHolder.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		consolePanel.add(consoleOutputHolder, BorderLayout.CENTER);
		
		consoleOutput = new JTextArea();
		consoleOutputHolder.setViewportView(consoleOutput);
		consoleOutput.setEditable(false);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		menuOptions = new JMenu("Options");
		menuBar.add(menuOptions);
		
		menuCheckboxDebug = new JCheckBoxMenuItem("Debug");
		menuCheckboxDebug.addActionListener(new MenuCheckboxDebugActionListener());
		menuOptions.add(menuCheckboxDebug);
		
	}

	private class ConsoleInputTextFieldActionListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			JTextField inputField = (JTextField) e.getSource();
			
			String text = inputField.getText();
			
			Logger.log(Logger.DEBUG, "User command: \"" + text + "\"");
			
			if(text.length() > 5) {
				if (text.substring(0, 5).equalsIgnoreCase("/send")) {
					SPacket03Message packet = new SPacket03Message("Server",
							"Server> " + text.substring(5));
					
					server.getNetworkManager().getConnectionManager().sendPacketToAllClients(null, packet);
					
				}
			}
			
			inputField.setText("");
			
		}
		
	}
	
	private class MenuCheckboxDebugActionListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();
			
			server.getGame().setDebug(source.getState());
			
		}
		
	}
	
	public void setPlayerNames(final String[] playerNames) {
		
		Util.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				playerList.setListData(playerNames);
				playerList.revalidate();
			}
			
		});
		
	}
	
	public void addDebugText(final String debugText) {
		
		Util.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				consoleOutput.setText(consoleOutput.getText() + debugText);
//				consoleOutput.revalidate();
			}
			
		});
		
	}
	
	public static WindowManager instance() {
		
		if (instance == null) {
			instance = new WindowManager();
		}
		
		return instance;
		
	}
	
	public JTextArea getConsoleOutput() {
		return consoleOutput;
	}
	
}
