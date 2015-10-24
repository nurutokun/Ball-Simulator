package com.rawad.ballsimulator.files;

import java.util.ArrayList;

import com.rawad.gamehelpers.files.FileType;
import com.rawad.gamehelpers.utils.Util;

public class SettingsLoader extends FileType {
	
	private String ip;
	
	public SettingsLoader() {
		
		ip = "localhost";
		
	}
	
	@Override
	public void parseData(ArrayList<String> lines) {
		super.parseData(lines);
		
		setIp(Util.getNullSafe(data.get("ip"), ip));
		
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
		
		data.put("ip", ip);
		
	}
	
}
