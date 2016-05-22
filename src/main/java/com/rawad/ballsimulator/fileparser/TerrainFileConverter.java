package com.rawad.ballsimulator.fileparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;

public class TerrainFileConverter {
	
	public static void main(String... args) {
		
		HashMap<String, String> commands = Util.parseCommandLineArguments(args);
		
		ResourceManager.init(commands);
		
		String fileToConvert = commands.get("fileToConvert");
		
		if(fileToConvert == null) {
			
			Logger.log(Logger.SEVERE, "Please add the file you would like to convert: fileToConvert=\"FileName\".");
			
			return;
		}
		
		String fileToSaveTo = commands.get("fileToSaveTo");
		
		CustomLoader loader = new CustomLoader();
		
		BufferedReader reader = loader.readFile(CustomLoader.TERRAIN_FOLDER, fileToConvert);
		
		ArrayList<String> lines = new ArrayList<String>();
		
		try {
			
			String line = null;
			
			while((line = reader.readLine()) != null) {
				
				String[] data = line.split(",");
				
				double x = Util.parseDouble(data[0]);
				double y = Util.parseDouble(data[1]);
				
				double width = Util.parseDouble(data[2]);
				double height = Util.parseDouble(data[3]);
				
				lines.add(x + "," + y + "," + width/32d + "," + height/32d);
				
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		if(fileToSaveTo == null) {
			Logger.log(Logger.WARNING, "File to save to can be set by: fileToSaveTo=\"FileName\". Save as " 
					+ fileToConvert + " instead? Y/N: ");
			
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			
			String response;
			
			try {
				
				response = userInput.readLine();
				
				if(!response.equals("Y")) {
					return;
				}
				
				fileToSaveTo = fileToConvert;
				
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
			
		}
		
		loader.saveFile(Util.getStringFromLines(lines.toArray(new String[lines.size()]), Util.NL, false), 
				CustomLoader.TERRAIN_FOLDER, fileToSaveTo);
		
	}
	
}
