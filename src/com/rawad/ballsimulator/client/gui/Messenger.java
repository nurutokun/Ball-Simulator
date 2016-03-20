package com.rawad.ballsimulator.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import com.rawad.gamehelpers.gui.ScrollBar;
import com.rawad.gamehelpers.utils.Util;

public class Messenger extends JPanel implements FocusListener {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -9167744656049003114L;
	
	private static final Color FOCUSED_BACKGROUND = new Color(0, 0, 0, 128);
	
	private static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(5, 5, 5, 5);
	private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();
	
	private static final int MIN_WIDTH = 16;
	private static final int MIN_HEIGHT = 16;
	
	private JTextField input;
	private JScrollPane outputContainer;
	private JTextArea output;
	private ScrollBar scrollbar;
	
	private Border focusedInputBorder;
	
	private String buffer;
	
	private boolean textReady;
	
	public Messenger() {
		super();
		
		buffer = "";
		
		initialize();
		
	}
	
	private void initialize() {
		
		setBackground(Util.TRANSPARENT);
		setFocusable(false);
		
		setLayout(new BorderLayout());
		
		input = new JTextField();
		input.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String text = input.getText();
				
				if(!"".equals(text)) {
					buffer += input.getText() + Util.NL;
				}
				
				textReady = true;
				
				Messenger.this.getParent().requestFocusInWindow();
				
			}
			
		});
		
		input.addFocusListener(this);
		input.setBackground(Util.TRANSPARENT);
		focusedInputBorder = input.getBorder();
		input.setBorder(DEFAULT_BORDER);
		
		input.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
		input.getActionMap().put("cancel", new AbstractAction() {
			
			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = -1651740958568598268L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Messenger.this.getParent().requestFocusInWindow();
				
			}
			
		});
		
		add(input, BorderLayout.SOUTH);
		
		output = new JTextArea(MIN_WIDTH, MIN_HEIGHT);
		output.setBackground(Util.TRANSPARENT);
		output.setForeground(Color.WHITE);
		output.setWrapStyleWord(true);
		output.setTabSize(4);
		output.setEditable(false);
		output.setFocusable(false);
		output.setLineWrap(true);
		output.setWrapStyleWord(true);
		output.setBorder(EMPTY_BORDER);
		
		outputContainer = new JScrollPane(output, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outputContainer.getViewport().setBackground(Util.TRANSPARENT);
		outputContainer.setBackground(Util.TRANSPARENT);
		outputContainer.setViewportBorder(null);
		outputContainer.setBorder(null);
		outputContainer.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);// So scroll bar is on left side.
		
		scrollbar = new ScrollBar(Util.TRANSPARENT);
		outputContainer.setVerticalScrollBar(scrollbar);
		
		add(outputContainer, BorderLayout.CENTER);
		
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		input.setBorder(focusedInputBorder);
		input.setBackground(Color.WHITE);
		
		output.setBackground(FOCUSED_BACKGROUND);
		
		scrollbar.setBaseColor(FOCUSED_BACKGROUND);
		
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		input.setBorder(DEFAULT_BORDER);
		input.setBackground(Util.TRANSPARENT);
		
		output.setBackground(Util.TRANSPARENT);
		
		scrollbar.setBaseColor(Util.TRANSPARENT);
		
		Util.setTextSafely(input, "");
		
	}
	
	public String getText() {
		
		String re = "";
		
		if(textReady) {
			textReady = false;
			
			re = buffer;
			buffer = "";
		}
		
		return re;
		
	}
	
	public void addNewLine(String lineToAdd) {
		Util.setTextSafely(output, output.getText() + lineToAdd);
	}
	
	public void clearText() {
		Util.setTextSafely(output, "");
		Util.setTextSafely(input, "");
	}
	
	@Override
	public boolean requestFocusInWindow() {
		return input.requestFocusInWindow();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(MIN_WIDTH, MIN_HEIGHT);
	}
	
}
