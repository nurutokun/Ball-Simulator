package com.rawad.ballsimulator.fileparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;

public final class TerrainFileConverter {
	
	private TerrainFileConverter() {}
	
	public static void main(String... args) {
		
		HashMap<String, String> commands = Util.parseCommandLineArguments(args);
		
		ResourceManager.init(commands);
		
		String fileToConvert = commands.get("fileToConvert");
		
		if(fileToConvert == null) {
			
			Logger.log(Logger.SEVERE, "Please add the file you would like to convert: fileToConvert=\"FileName\".");
			
			return;
		}
		
		Loader loader = new Loader();
		
		Logger.log(Logger.DEBUG, "Loading file " + fileToConvert + "...");
		
		BufferedReader reader = loader.readFile(Loader.FOLDER_TERRAIN, fileToConvert);
		
		TerrainFileConverter converter = new TerrainFileConverter();
		
		Logger.log(Logger.DEBUG, "Converting file...");
		
		ArrayList<String> lines = converter.convert2(reader);
		
		Logger.log(Logger.DEBUG, "Done converting.");
		
		String fileToSaveTo = commands.get("fileToSaveTo");
		
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
		
		Logger.log(Logger.DEBUG, "Saving file " + fileToSaveTo + "...");
		
		loader.saveFile(Util.getStringFromLines(Util.NL, false, lines), Loader.FOLDER_TERRAIN, fileToSaveTo);
		
		Logger.log(Logger.DEBUG, "Done!");
		
	}
	
	/**
	 * 
	 * <p>
	 * Converts from: <pre>x,y,scaleX,scaleY</pre> to <pre>originX,originY,scaleX,scaleY</pre> where <code>originX,originY
	 * </code> are simply the center of each entity (the <code>x,y</code> of {@code TransformComponent}).
	 * </p>
	 * 
	 * @param reader
	 */
	public ArrayList<String> convert2(BufferedReader reader) {
		
		ArrayList<String> lines = new ArrayList<String>();
		
		try {
			
			String line = null;
			
			while((line = reader.readLine()) != null) {
				
				String[] data = line.split(",");
				
				double x = Util.parseDouble(data[0]);
				double y = Util.parseDouble(data[1]);
				
				double scaleX = Util.parseDouble(data[2]);
				double scaleY = Util.parseDouble(data[3]);
				
				lines.add((x + (32d /2d * scaleX)) + "," + (y + (32d / 2d * scaleY)) + "," + scaleX + "," + scaleY);
				// Only offset by half the default size (32).
			}
			
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
		return lines;
		
	}
	
	/**
	 * 
	 * <p>
	 * Converts from:
	 * <pre>x,y,width,height</pre> to <pre>x,y,scaleX,scaleY</pre>
	 * </p>
	 * 
	 * @param reader
	 * @return
	 */
	public ArrayList<String> convert1(BufferedReader reader) {
		
		ArrayList<String> lines = new ArrayList<String>();
		
		try {
			
			String line = null;
			
			while((line = reader.readLine()) != null) {
				
				String[] data = line.split(",");
				
				double x = Util.parseDouble(data[0]);
				double y = Util.parseDouble(data[1]);
				
				double width = Util.parseDouble(data[2]);
				double height = Util.parseDouble(data[3]);
				
				lines.add(x + "," + y + "," + width / 32d + "," + height / 32d);
				
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return lines;
		
	}
	
}
