package com.rawad.ballsimulator.file_parser.file_types;

import java.util.HashMap;

import com.rawad.gamehelpers.utils.Util;

public class MultiplayerSettings extends FileType {
	
	public static final String FILE_NAME = "multiplayer_settings";
	
	private String ip;
	
	public MultiplayerSettings() {
		super(EFileType.MULTIPLAYER_SETTINGS, FILE_NAME);
		
		ip = "localhost";
		
	}
	
	@Override
	protected void parseData(HashMap<String, String> data) {
		
		setIp(Util.getNullSafe(data.get("ip"), ip));
		
	}
	
	public void setIp(String ip) {
		this.ip = ip;
		
		data.put("ip", ip);
		
	}
	
	public String getIp() {
		return ip;
	}
	
}
