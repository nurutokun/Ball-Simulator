package com.rawad.ballsimulator.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import com.rawad.gamehelpers.utils.Util;

public class Messenger extends JPanel implements FocusListener {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -9167744656049003114L;
	
	private static final Color FOCUSED_BACKGROUND = new Color(0, 0, 0, 128);
	
	private static final int MIN_WIDTH = 16;
	private static final int MIN_HEIGHT = 16;
	
	private JTextField input;
	private JScrollPane outputContainer;
	private JTextArea output;
	
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
				
				buffer += input.getText() + Util.NL;
				input.setText("");
				
				textReady = true;
				
				Messenger.this.getParent().requestFocusInWindow();
				
			}
			
		});
		
		input.addFocusListener(this);
		input.setBackground(Util.TRANSPARENT);
		focusedInputBorder = input.getBorder();
		input.setBorder(BorderFactory.createLineBorder(Util.TRANSPARENT, 5));
		
		add(input, BorderLayout.SOUTH);
		
		outputContainer = new JScrollPane();
		outputContainer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outputContainer.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		outputContainer.getViewport().setBackground(Util.TRANSPARENT);
		
		output = new JTextArea(MIN_WIDTH, MIN_HEIGHT);// TODO: Make dynamic; as size of mess changes so should these.
		output.setBackground(Util.TRANSPARENT);
		output.setForeground(Color.WHITE);
		output.setWrapStyleWord(true);
		output.setTabSize(4);
		output.setEditable(false);
		output.setFocusable(false);
		output.setLineWrap(true);
		output.setWrapStyleWord(true);
		outputContainer.setViewportView(output);
		
		add(outputContainer, BorderLayout.CENTER);
		
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		input.setBackground(Color.WHITE);
		input.setBorder(focusedInputBorder);
		output.setBackground(FOCUSED_BACKGROUND);
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		input.setBackground(Util.TRANSPARENT);
		input.setBorder(BorderFactory.createLineBorder(Util.TRANSPARENT, 5));
		output.setBackground(Util.TRANSPARENT);
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
