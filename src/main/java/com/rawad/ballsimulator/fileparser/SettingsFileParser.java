package com.rawad.ballsimulator.fileparser;

import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.utils.Util;

public class SettingsFileParser extends FileParser {
	
	private static final String REGEX = "=";
	
	private static final String KEY_IP = "ip";
	private static final String KEY_RES = "fullscreenResolution";
	
	public SettingsFileParser() {
		super();
		
		data.put(KEY_IP, "localhost");// Just so there's some default value... Could also be done in start() method.
		data.put(KEY_RES, "640x320");
		
	}
	
	@Override
	protected void parseLine(String line) {
		
		String[] tokens = line.split(REGEX);
		
		if(tokens.length >= 2) {
			
			data.put(tokens[0].trim(), tokens[1].trim());
			
		}
		
	}
	
	@Override
	public String getContent() {
		
		String[] lines = new String[data.size()];
		
		int i = 0;
		
		for(String key: data.keySet()) {
			
			lines[i++] = key + " " + REGEX + " " + data.get(key);
			
		}
		
		return Util.getStringFromLines(lines, Util.NL, false);
	}
	
	public void setIp(String ip) {
		data.put(KEY_IP, ip);
	}
	
	public String getIp() {
		return data.get(KEY_IP);
	}
	
	public void setFullScreenResolution(String resolution) {
		data.put(KEY_RES, resolution);
	}
	
	public String getFullScreenResolution() {
		return data.get(KEY_RES);
	}
	
}
