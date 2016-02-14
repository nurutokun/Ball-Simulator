package com.rawad.ballsimulator.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.rawad.gamehelpers.utils.Util;

public class Messenger extends JPanel {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -9167744656049003114L;
	
	private static final int MIN_WIDTH = 16;
	private static final int MIN_HEIGHT = 16;
	
	private JTextField input;
	private JScrollPane outputContainer;
	private JTextArea output;
	
	private String buffer;
	
	private boolean textReady;
	
	public Messenger() {
		super();
		
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
				
				buffer += input.getText() + Util.NL;
				input.setText("");
				
				textReady = true;
				
				Messenger.this.getParent().requestFocusInWindow();
				
			}
			
		});
		
		add(input, BorderLayout.SOUTH);
		
		outputContainer = new JScrollPane();
		outputContainer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(outputContainer, BorderLayout.CENTER);
		
		output = new JTextArea();
		output.setBackground(Util.TRANSPARENT);
		output.setForeground(Color.WHITE);
		output.setWrapStyleWord(true);
		output.setTabSize(4);
		output.setLineWrap(true);
		output.setEditable(false);
		output.setFocusable(false);
		outputContainer.setViewportView(output);
		
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
