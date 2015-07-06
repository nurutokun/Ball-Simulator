package com.rawad.ballsimulator.networking.server.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import com.rawad.ballsimulator.main.BallSimulator;
import com.rawad.ballsimulator.networking.server.Server;
import com.rawad.gamehelpers.displaymanager.DisplayManager;
import com.rawad.gamehelpers.log.Logger;

import javax.swing.JScrollPane;

public class WindowManager {

	private static WindowManager instance;

	private JFrame frame;
	private JPanel basePanel;
	private JSplitPane serverInfoPanel;
	private JPanel consolePanel;
	private JTextArea consoleOutput;
	private JTextField consoleInputTextField;
	private JSplitPane splitPane;
	private ViewportShell panel;
	private JList<String> playerList;
	private JScrollPane consoleOutputHolder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception ex) {
					Logger.log(Logger.WARNING,
							"Couldn't load native system's look and feel for the server window.");
				}

				try {
					instance = new WindowManager();

					Server server = new Server();
					server.start();

					instance.initialize(server);
					instance.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private WindowManager() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			Logger.log(Logger.WARNING,
					"Couldn't load native system's look and feel for the server window.");
		}

	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 */
	public void initialize(Server server) {
		frame = new JFrame();
		frame.setTitle(BallSimulator.NAME);
		frame.getContentPane().setPreferredSize(
				new Dimension(DisplayManager.getScreenWidth(), DisplayManager
						.getScreenHeight()));// default.

		basePanel = new JPanel();
		basePanel.setBackground(Color.WHITE);
		frame.getContentPane().add(basePanel, BorderLayout.CENTER);
		basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.X_AXIS));

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
		consolePanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null,
				null, null, null));
		splitPane.setRightComponent(consolePanel);
		consolePanel.setLayout(new BorderLayout(0, 0));

		consoleInputTextField = new JTextField();
		consoleInputTextField.setDropMode(DropMode.INSERT);
		consoleInputTextField
				.addActionListener(new ConsoleInputTextFieldActionListener());

		consoleInputTextField.setToolTipText("Input Commands Here");
		consolePanel.add(consoleInputTextField, BorderLayout.SOUTH);

		consoleOutputHolder = new JScrollPane();
		consolePanel.add(consoleOutputHolder, BorderLayout.CENTER);

		consoleOutput = new JTextArea();
		consoleOutputHolder.setViewportView(consoleOutput);
		consoleOutput.setEditable(false);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private class ConsoleInputTextFieldActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JTextField inputField = (JTextField) e.getSource();

			Logger.log(Logger.DEBUG, "User command: \"" + inputField.getText()
					+ "\"");

			inputField.setText("");

		}

	}

	public void setPlayerNames(String[] playerNames) {
		playerList.setListData(playerNames);
		playerList.revalidate();
	}

	public void addDebugText(String debugText) {
		consoleOutput.setText(consoleOutput.getText() + debugText);
		consoleOutput.revalidate();
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
