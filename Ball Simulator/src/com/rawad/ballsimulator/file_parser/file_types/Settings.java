package com.rawad.ballsimulator.file_parser.file_types;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import com.rawad.gamehelpers.utils.Util;

public class Settings extends FileType {
	
	private static final String FILE_NAME = "settings";
	
	private int up_primary;
	private int down_primary;
	private int right_primary;
	private int left_primary;
	
	private int up_secondary;
	private int down_secondary;
	private int right_secondary;
	private int left_secondary;
	
	public Settings() {
		super(EFileType.SETTINGS, FILE_NAME);
		
		up_primary = KeyEvent.VK_UP;
		down_primary = KeyEvent.VK_DOWN;
		right_primary = KeyEvent.VK_RIGHT;
		left_primary = KeyEvent.VK_LEFT;
		
		up_secondary = KeyEvent.VK_W;
		down_secondary = KeyEvent.VK_S;
		right_secondary = KeyEvent.VK_D;
		left_secondary = KeyEvent.VK_A;
		
	}
	
	@Override
	public void parseData(HashMap<String, String> data) {
		
		up_primary = Util.getNullSafe(Util.parseInt(data.get("up1")), up_primary);
		down_primary = Util.getNullSafe(Util.parseInt(data.get("down1")), down_primary);
		right_primary = Util.getNullSafe(Util.parseInt(data.get("right1")), right_primary);
		left_primary = Util.getNullSafe(Util.parseInt(data.get("left1")), up_primary);
		
		up_secondary = Util.getNullSafe(Util.parseInt(data.get("up2")), up_secondary);
		down_secondary = Util.getNullSafe(Util.parseInt(data.get("down2")), down_secondary);
		right_secondary = Util.getNullSafe(Util.parseInt(data.get("right2")), right_secondary);
		left_secondary = Util.getNullSafe(Util.parseInt(data.get("left2")), up_primary);
		
	}
	
	public boolean isUp(int up) {
		
		if(up == up_primary || up == up_secondary) {
			return true;
		}
		
		return false;
		
	}
	
	public boolean isDown(int down) {
		
		if(down == down_primary || down == down_secondary) {
			return true;
		}
		
		return false;
		
	}
	
	public boolean isRight(int right) {
		
		if(right == right_primary || right == right_secondary) {
			return true;
		}
		
		return false;
		
	}
	
	public boolean isLeft(int left) {
		
		if(left == left_primary || left == left_secondary) {
			return true;
		}
		
		return false;
		
	}
	
	public void setUp1(int up) {
		up_primary = up;
		
		data.put("up1", up + "");
		
	}
	
	public void setDown1(int down) {
		down_primary = down;
		
		data.put("down1", down + "");
	}
	
	public void setRight1(int right) {
		right_primary = right;
		
		data.put("right1", right + "");
	}
	
	public void setLeft1(int left) {
		left_primary = left;
		
		data.put("left1", left + "");
	}
	
}
