package com.rawad.ballsimulator.log;

public class Logger {
	
	public static final int DEBUG = 0;
	public static final int WARNING = 1;
	public static final int SEVERE = 2;
	
	public static void log(int code, String message) {
		
		System.out.println("Code: " + code + " Message: " + message);
		
	}
	
}
