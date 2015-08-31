package com.rawad.ballsimulator.file_parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.rawad.ballsimulator.file_parser.file_types.EFileType;
import com.rawad.ballsimulator.file_parser.file_types.FileType;
import com.rawad.ballsimulator.file_parser.file_types.MultiplayerSettings;
import com.rawad.ballsimulator.file_parser.file_types.Settings;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.log.Logger;

public class FileParser {
	
	public FileParser() {
		
	}
	
	public static FileType parseFile(String fileName) {
		
		BufferedReader fileReader = Loader.readFile(fileName);
		
		ArrayList<String> lines = new ArrayList<String>();
		
		String currentLine = null;
		
		try {
			
			while((currentLine = fileReader.readLine()) != null) {
				lines.add(currentLine);
			}
			
		} catch (IOException ex) {
			Logger.log(Logger.SEVERE, "Couldn't read from file.");
			ex.printStackTrace();
		}
		
		FileType re = getFileTypeById(lines.get(0));
		
		lines.remove(0);
//		lines.trimToSize();// For safety though really unnecessary...
		
		re.parseData(lines.toArray(new String[lines.size()]));
		
		return re;
		
	}
	
	private static FileType getFileTypeById(String id) {
		
		EFileType type = EFileType.getTypeById(id);
		
		switch(type) {
		
		case SETTINGS:
			new Settings();
		
		case MULTIPLAYER_SETTINGS:
			return new MultiplayerSettings();
			
		default:
			return null;
		
		}
		
	}
	
}
