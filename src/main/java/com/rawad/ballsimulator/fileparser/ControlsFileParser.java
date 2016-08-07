package com.rawad.ballsimulator.fileparser;

import java.util.ArrayList;

import com.rawad.ballsimulator.client.input.Input;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.client.input.InputBindings;
import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.utils.Util;

public class ControlsFileParser extends FileParser {
	
	private static final String REGEX = "=";
//	private static final String REGEX_INPUTS = ",";
	
	private static final int INDEX_ACTION = 0;
	private static final int INDEX_INPUT = 1;
	
	private InputBindings inputBindings;
	
	public ControlsFileParser(InputBindings inputBindings) {
		super();
		
		this.inputBindings = inputBindings;
		
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
		
		for(InputAction action: inputBindings.getBindings().keySet()) {
			
			Input input = inputBindings.get(action);
			
			lines.add(action.getName() + REGEX + input.getName());
			
		}
		
		return Util.getStringFromLines(Util.NL, false, lines);
		
	}
	
}
