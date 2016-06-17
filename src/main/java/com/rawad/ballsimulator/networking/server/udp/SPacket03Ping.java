package com.rawad.ballsimulator.networking.server.udp;

import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;

public class SPacket03Ping extends UDPPacket {
	
	private static final int PING_INDEX = 2;
	
	private static final int TIME_STAMP_INDEX = 3;
	
	private static final int REQUEST_INDEX = 4;
	
	/**
	 * 
	 * @param networkComp
	 * @param userComp
	 * @param timeStamp 
	 * 			In nano seconds, see {@link System#nanoTime()}. Should be used when {@code request} is {@code true}.
	 * @param request
	 */
	public SPacket03Ping(NetworkComponent networkComp, UserComponent userComp, long timeStamp, boolean request) {
		super(UDPPacketType.PING, networkComp.getId(), Integer.toString(userComp.getPing()), Long.toString(timeStamp), 
				Boolean.toString(request));
	}
	
	public SPacket03Ping(String dataAsString) {
		super(dataAsString);
	}
	
	public int getPing() {
		return Integer.parseInt(indexedData[PING_INDEX]);
	}
	
	public long getTimeStamp() {
		return Long.parseLong(indexedData[TIME_STAMP_INDEX]);
	}
	
	public boolean isRequest() {
		return Boolean.parseBoolean(indexedData[REQUEST_INDEX]);
	}
	
}
