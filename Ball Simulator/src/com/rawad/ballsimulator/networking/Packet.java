package com.rawad.ballsimulator.networking;

import java.io.UnsupportedEncodingException;

import com.rawad.gamehelpers.log.Logger;

/**
 * "C" packets are client packets SENT only by client and "S" are server packets SENT only by the server.
 * 
 * @author Rawad
 *
 */
public abstract class Packet {
	
	public static final int BUFFER_SIZE = 512;
	
	/**
	 * Used to split between different sets of data in String form.
	 */
	private static final String REGEX = "::";
	
	private static final int IDENTIFIER_LENGTH = 2;// 2 hex bits (one binary byte) reserved for the packet's id.
	
	/**
	 * Index of the player's username in the {@code dataString} array
	 */
	private static final int USERNAME_INDEX = 0;
	
	private final String typeId;
	
	protected String[] dataString;
	
	private Packet(String typeId, String rawData) {
		this.typeId = typeId;
		
//		System.out.println(rawData);
		
		dataString = rawData.split(REGEX);
		
	}
	
	protected Packet(String typeId, byte[] data) {
		this(typeId, parsePacketData(data));
	}
	
	protected Packet(String typeId, String username, String... data) {
		this(typeId, getFormattedDataString(REGEX, appendStringToBeginning(username, data)));
	}
	
	/**
	 * For use by UDP.
	 * 
	 * @return
	 */
	public final byte[] getData() {
		
		String formattedData = getFormattedDataString(REGEX, dataString);
		
		return (typeId + formattedData).getBytes();
	}
	
	/**
	 * For use by TCP.
	 * 
	 * @return
	 */
	public final String getDataAsString() {
		return getStringFromData(getData());
	}
	
	/**
	 * Just because every {@code Packet} seemed to be declaring one of these, we'll abstract it out. <b>Note:</b> index <i>0</i> is reserved
	 * for the username.
	 * 
	 * @return
	 */
	public final String getUsername() {
		return dataString[USERNAME_INDEX];
	}
	
	/**
	 * Adds regex's to an array of String objects that are to be formatted.
	 * 
	 * @param datas
	 * @return
	 */
	protected static String getFormattedDataString(String regex, String... datas) {
		
		String buffer = "";
		
		if(datas.length <= 0) {
			return buffer;
		}
		
		for(int i = 0; i < datas.length; i++) {
			buffer += datas[i].toString();
			
			if(i >= datas.length - 1) {
				// Don't add regex for last thing
			} else {
				buffer += regex;
			}
			
		}
		
		return buffer;
		
	}
	
	public static TCPPacketType getTCPPacketTypeFromData(byte[] data) {
		
		String packetInfo = getStringFromData(data);
		
		String id = packetInfo.substring(0, IDENTIFIER_LENGTH);// could change this into a method on its own, much like of "parsePacketData"
		
		return TCPPacketType.getPacketTypeById(id);
		
	}
	
	public static UDPPacketType getUDPPacketTypeFromData(byte[] data) {
		
		String packetInfo = getStringFromData(data);
		
		String id = packetInfo.substring(0, IDENTIFIER_LENGTH);
		
		return UDPPacketType.getPacketTypeById(id);
		
	}
	
	/**
	 * Get string of data from its byte array.
	 * 
	 * @param data
	 * @return
	 */
	private static String parsePacketData(byte[] data) {
		return getStringFromData(data).substring(IDENTIFIER_LENGTH);// Skips the first 2 cahracters because those are our identifiers
	}
	
	private static String[] appendStringToBeginning(String stringToAppend, String[] arrayToAppendTo) {
		
		String[] re = new String[arrayToAppendTo.length + 1];
		
		re[0] = stringToAppend;
		
		for(int i = 1; i < re.length; i++) {
			re[i] = arrayToAppendTo[i-1];
		}
		
		return re;
		
	}
	
	/**
	 * Directly converts byte array data to String; convenience method for error checking.
	 * 
	 * @param data
	 * @return
	 */
	private static String getStringFromData(byte[] data) {
		String re;
		
		try {
			re = new String(data, "UTF-8").trim();
		} catch (UnsupportedEncodingException ex) {
			Logger.log(Logger.WARNING, "Data couldn't be decoded into String using UTF-8 charset.");
			re = TCPPacketType.INVALID.getId();
		}
		
		return re;
	}
	
}
