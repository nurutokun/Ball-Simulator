package com.rawad.ballsimulator.fileparser;

import java.util.Set;

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
		
		if(tokens != null && tokens.length >= 2) {
			
			data.put(tokens[0], tokens[1]);
			
		}
		
	}
	
	@Override
	public String getContent() {
		
		Set<String> keys = data.keySet();
		
		String[] lines = new String[keys.size()];
		
		int i = 0;
		
		for(String key: keys) {
			
			String value = data.get(key);
			
			lines[i] = key + REGEX + value;
			
			i++;
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
