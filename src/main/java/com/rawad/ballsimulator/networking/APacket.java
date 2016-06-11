package com.rawad.ballsimulator.networking;

import java.io.UnsupportedEncodingException;

import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

/**
 * "C" packets are client packets SENT only by client and "S" are server packets SENT only by the server.
 * 
 * @author Rawad
 *
 */
public abstract class APacket {
	
	/**
	 * Maximum size of a packet.
	 */
	public static final int BUFFER_SIZE = 512;
	
	/**
	 * Used to split between different sets of data in {@code String} form.
	 */
	protected static final String REGEX = "::";
	
	protected static final int IDENTIFIER_LENGTH = 2;// 2 hex bits (one binary byte) reserved for the packet's id.
	
	private static final int PACKET_ID_INDEX = 0;
	
	protected String[] indexedData;
	
	protected String dataAsString;
	
	public APacket(String dataAsString) {
		
		this.dataAsString = dataAsString;
		
		indexedData = dataAsString.split(REGEX);
		
	}
	
	public String getPacketId() {
		return indexedData[PACKET_ID_INDEX];
	}
	
	/**
	 * For use by UDP.
	 * 
	 * @return
	 */
	public final byte[] getData() {
		return dataAsString.getBytes();
	}
	
	/**
	 * For use by TCP.
	 * 
	 * @return
	 */
	public final String getDataAsString() {
		return dataAsString;
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
		
		buffer = Util.getStringFromLines(datas, regex, false);
		
		return buffer;
		
	}
	
	/**
	 * Get {@code String} of data from its {@code byte[]} array.
	 * 
	 * @param data
	 * @return
	 */
	public static String getStringFromData(byte[] data) {
		
		String re;
		
		try {
			re = new String(data, "UTF-8").trim();
		} catch (UnsupportedEncodingException ex) {
			Logger.log(Logger.WARNING, "Data couldn't be decoded into String using UTF-8 charset.");
			re = TCPPacketType.INVALID.getId();
		}
		
		return re;
		
	}
	
	public static String getPacketIdFromString(String dataAsString) {
		
		String[] splitData = dataAsString.split(REGEX);
		
		return splitData[PACKET_ID_INDEX];
				
	}
	
}
