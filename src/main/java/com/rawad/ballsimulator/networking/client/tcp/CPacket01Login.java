package com.rawad.ballsimulator.networking.client.tcp;

import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;

public class CPacket01Login extends TCPPacket {
	
	public CPacket01Login(String username) {
		super(TCPPacketType.LOGIN, username);
		
	}
	
}
