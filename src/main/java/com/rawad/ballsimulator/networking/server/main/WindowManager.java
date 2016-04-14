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

import com.rawad.ballsimulator.game.BallSimulator;
import com.rawad.ballsimulator.networking.server.Server;
import com.rawad.ballsimulator.networking.server.tcp.SPacket03Message;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.input.EventHandler;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;

public class WindowManager {

	private static WindowManager instance;

	private final Server server;
	
	private BallSimulator game;
	
	private JFrame frame;
	private JPanel basePanel;
	
	private JPanel viewportPanel;
	
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
		
		ResourceManager.init(args);
		
		instance = instance();
		
		GameManager.instance().launchGame(instance.game, instance.server);
		
		Util.invokeLater(new Runnable() {
			
			public void run() {

				try {
					
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception ex) {
						Logger.log(Logger.WARNING, "Couldn't load native system's look and feel.");
					}
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
		});
		
	}

	private WindowManager() {
		
		game = new BallSimulator();
		
		server = new Server();
		// Here because game needs to initialize the fileparsers so that terrain can be loaded here
		
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 * 
	 */
	public void initialize(final Server server) {
		
		frame = new JFrame() {
			
			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = 2494144741047124345L;

			@Override
			public boolean requestFocusInWindow() {
				return server.getController().getPanel().requestFocusInWindow();
			}
			
		};
		frame.setTitle(BallSimulator.NAME);
		frame.setPreferredSize(new Dimension(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT));// Is okay?
		frame.addWindowListener(EventHandler.instance());
		
		basePanel = new JPanel();
		basePanel.setBackground(Color.WHITE);
		basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.X_AXIS));
		frame.getContentPane().add(basePanel);

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
		
		viewportPanel = server.getController().getPanel();
		viewportPanel.setBorder(new TitledBorder(null, "Server World",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		viewportPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		serverInfoPanel.setTopComponent(viewportPanel);
		
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
		consoleOutput.setEditable(false);
		consoleOutputHolder.setViewportView(consoleOutput);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		menuOptions = new JMenu("Options");
		menuBar.add(menuOptions);
		
		menuCheckboxDebug = new JCheckBoxMenuItem("Debug");
		menuCheckboxDebug.addActionListener(new MenuCheckboxDebugActionListener());
		menuOptions.add(menuCheckboxDebug);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
		
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
			}
			
		});
		
	}
	
	public void addDebugText(final String debugText) {
		
		Util.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				consoleOutput.setText(consoleOutput.getText() + debugText);
			}
			
		});
		
	}
	
	public void setIcon(final int location) {
		
		Util.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setIconImage(ResourceManager.getTexture(location));
				frame.revalidate();
			}
		});
		
	}
	
	public void close() {
		frame.dispose();
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
