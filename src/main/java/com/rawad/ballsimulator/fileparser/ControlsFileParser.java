package com.rawad.ballsimulator.fileparser;

import java.util.ArrayList;

import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.utils.Util;
import com.rawad.jfxengine.client.input.Input;
import com.rawad.jfxengine.client.input.InputBindings;

public class ControlsFileParser extends FileParser {
	
	private static final String REGEX = "=";
	
	private static final int INDEX_ACTION = 0;
	private static final int INDEX_INPUT = 1;
	
	private InputBindings inputBindings;
	
	public ControlsFileParser() {
		super();
		
		this.inputBindings = new InputBindings();
		
	}
	
	@Override
	protected void parseLine(String line) {
		
		String[] tokens = line.split(REGEX);
		
		InputAction action = InputAction.getByName(tokens[INDEX_ACTION].trim());
		Input input = Input.getByName(tokens[INDEX_INPUT].trim());
		
		inputBindings.put(action, input);
		
	}
	
	@Override
	public String getContent() {
		
		ArrayList<String> lines = new ArrayList<String>();
		
		for(InputAction action: InputAction.values()) {
			
			if(action == InputAction.DEFAULT) continue;
			
			Input input = inputBindings.get(action);
			
			String line = action.getName() + REGEX + input.getName();
			
			lines.add(line);
			
		}
		
		return Util.getStringFromLines(Util.NL, false, lines);
		
	}
	
	public void setInputBindings(InputBindings inputBindings) {
		this.inputBindings = inputBindings;
	}
	
}
