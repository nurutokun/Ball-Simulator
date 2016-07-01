package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

/**
 * Sends a request to the server to send the client all the entities currently in the server's world.
 * 
 * @author Rawad
 *
 */
public class CPacket04Entity extends TCPPacket {
	
	public CPacket04Entity() {
		super(TCPPacketType.ENTITY);
	}
	
	public CPacket04Entity(String dataAsString) {
		super(dataAsString);
	}
	
}
