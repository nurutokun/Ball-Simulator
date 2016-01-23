package com.rawad.ballsimulator.client.gui;

import java.awt.BorderLayout;
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
	private CloseButton closeButton;
	
	private String buffer;
	
	private boolean textReady;
	
	public Messenger() {
		super();
		
		initialize();
	}
	
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		
		input = new JTextField();
		input.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				buffer += input.getText() + Util.NL;
				input.setText("");
				
				textReady = true;
				
			}
			
		});
		add(input, BorderLayout.SOUTH);
		
		outputContainer = new JScrollPane();
		outputContainer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(outputContainer, BorderLayout.CENTER);
		
		output = new JTextArea();
		output.setWrapStyleWord(true);
		output.setTabSize(4);
		output.setLineWrap(true);
		output.setEditable(false);
		outputContainer.setViewportView(output);
		
		closeButton = new CloseButton();
		closeButton.setText("");
		closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Messenger.this.setVisible(!Messenger.this.isVisible());
			}
			
		});
		outputContainer.setColumnHeaderView(closeButton);
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
		output.setText(output.getText() + lineToAdd);
	}
	
	public void clearText() {
		output.setText("");
		input.setText("");
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(MIN_WIDTH, MIN_HEIGHT);
	}
	
}
