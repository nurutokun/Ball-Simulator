package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class CPacket03Ping extends TCPPacket {
	
	private static final int INDEX_TIME_STAMP = 1;
	
	public CPacket03Ping(long timeStamp) {
		super(TCPPacketType.PING, Long.toString(timeStamp));
	}
	
	/**
	 * 
	 * @return The amount returned from {@link System#nanoTime()} when this packet is created and sent.
	 */
	public long getTimeStamp() {
		return Long.parseLong(indexedData[INDEX_TIME_STAMP]);
		// Check out these websites: https://developer.valvesoftware.com/wiki/Source_Multiplayer_Networking and
		// http://fabiensanglard.net/quake3/network.php for networking.
	}
	
}
