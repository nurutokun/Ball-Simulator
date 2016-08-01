package com.rawad.ballsimulator.fileparser;

import java.util.ArrayList;

import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.gamehelpers.client.input.InputBindings;
import com.rawad.gamehelpers.fileparser.FileParser;

public class ControlsFileParser extends FileParser {
	
	private static final String REGEX = "=";
	private static final String REGEX_INPUTS = ",";
	
	private InputBindings inputBindings;
	
	public ControlsFileParser(InputBindings inputBindings) {
		super();
		
		this.inputBindings = inputBindings;
		
	}
	
	@Override
	protected void parseLine(String line) {
		
	}
	
	@Override
	public String getContent() {
		
		ArrayList<String> lines = new ArrayList<String>();
		
		for(Object key: inputBindings.getBindingsMap().keySet()) {
			
			InputAction action = (InputAction) key;
			
			if(action == InputAction.DEFAULT) continue;
			
//			ArrayList<Objec>
			
			String line = action.getName() + " " + REGEX;
			
		}
		
		return null;
	}
	
}
