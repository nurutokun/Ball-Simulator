package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class CPacket03Ping extends TCPPacket {
	
	private static final int INDEX_TIME_STAMP = 1;
	
	public CPacket03Ping(String username, long timeStamp) {
		super(TCPPacketType.PING, username, Long.toString(timeStamp));
	}
	
	public CPacket03Ping(byte[] data) {
		super(TCPPacketType.PING, data);
	}
	
	/**
	 * 
	 * @return <stroke>The amount returned from {@link System#nanoTime()} when this packet is created and sent.</stroke>
	 */
	public long getTimeStamp() {
		return Long.parseLong(dataString[INDEX_TIME_STAMP]);
		// TODO: Find good way to calculate time ping packet is sent (NTP <- some form of time protocol).
		// http://www.javacodex.com/More-Examples/5/1 <- maybe don't use packets...
		// Also, check out these websites: https://developer.valvesoftware.com/wiki/Source_Multiplayer_Networking and
		// http://fabiensanglard.net/quake3/network.php for networking.
	}
	
}
