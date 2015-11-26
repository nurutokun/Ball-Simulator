package com.rawad.ballsimulator.files;

import java.util.ArrayList;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.files.FileType;
import com.rawad.gamehelpers.utils.Util;

public class SettingsLoader extends FileType {
	
	private String ip;
	
	private String fullscreenResolution;
	
	public SettingsLoader() {
		
		ip = "localhost";
		fullscreenResolution = DisplayManager.getFullScreenResolution();
		
	}
	
	@Override
	public void parseData(ArrayList<String> lines) {
		super.parseData(lines);
		
		this.ip = Util.getNullSafe(data.get("ip"), this.ip);
		
		this.fullscreenResolution = Util.getNullSafe(data.get("fullscreenResolution"), fullscreenResolution);
		
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
		
		data.put("ip", ip);
		
	}
	
	public String getFullScreenResolution() {
		return fullscreenResolution;
	}
	
	public void setFullScreenResolution(String fullscreenResolution) {
		this.fullscreenResolution = fullscreenResolution;
		
		data.put("fullscreenResolution", fullscreenResolution);
		
	}
	
}
