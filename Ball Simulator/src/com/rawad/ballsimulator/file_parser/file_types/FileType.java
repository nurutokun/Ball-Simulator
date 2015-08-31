package com.rawad.ballsimulator.file_parser.file_types;

import java.util.HashMap;
import java.util.Iterator;

import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

/**
 * Format:
 * 
 * 1. id
 * 2. first setting token = first setting value
 * 3. ...
 * 
 * Note that this is also a way in which different game state can communicate with one another.
 * 
 * @author Rawad
 *
 */
public abstract class FileType {
	
	public static final String TOKEN = "=";
	
	private final EFileType type;
	private final String fileName;
	
	protected HashMap<String, String> data;
	
	public FileType(EFileType type, String fileName) {
		this.type = type;
		this.fileName = fileName;
		
		data = new HashMap<String, String>();
		
	}
	
	/**
	 * 
	 * @param fileData Lines of the file.
	 */
	public final void parseData(String[] fileData) {
		
		HashMap<String, String> data = new HashMap<String, String>();
		
		for(String line: fileData) {
			
			String[] halves = line.split(TOKEN);
			
			try {
				String key = Util.getNullSafe(halves[0].trim(), "");
				String value = Util.getNullSafe(halves[1].trim(), "");
				
				data.put(key, value);
			} catch(ArrayIndexOutOfBoundsException ex) {
				Logger.log(Logger.WARNING, "File doesn't seem to be formatted properly: \"" + line + "\"");
				ex.printStackTrace();
			}
			
		}
		
		parseData(data);
		
	}
	
	protected abstract void parseData(HashMap<String, String> data);
	
	public void saveFile() {
		Loader.saveFile(fileName, formatLines(data));
	}
	
	/**
	 * Converts lines into a single string that can be easily saved.
	 * 
	 * @param lines
	 * @return
	 */
	private String formatLines(HashMap<String, String> lines) {
		
		String re = "" + type.getId() + Util.NL;
		
		Iterator<String> keys = lines.keySet().iterator();// TODO: Might want to get the actual lines themselves to use the Util method.
		
		for(int i = 0; i < lines.size(); i++) {
			
			String key = keys.next();
			
			re += key + TOKEN + lines.get(key);
			
			if(!(i >= lines.size() - 1)) {// Don't add new line for last line.
				re += Util.NL;
			}
			
		}
		
		return re;
		
	}
	
	public EFileType getFileType() {
		return type;
	}
	
	public String getTypeId() {
		return type.getId();
	}
	
}
