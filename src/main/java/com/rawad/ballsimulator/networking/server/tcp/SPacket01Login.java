package com.rawad.ballsimulator.networking.server.tcp;

import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;
import com.rawad.ballsimulator.server.entity.NetworkComponent;
import com.rawad.ballsimulator.server.entity.UserComponent;

public class SPacket01Login extends TCPPacket {
	
	private static final int ENTITY_ID_INDEX = 1;
	
	private static final int USERNAME_INDEX = 2;
	private static final int IP_INDEX = 3;
	
	private static final int X_INDEX = 4;
	private static final int Y_INDEX = 5;
	
	private static final int SCALE_X_INDEX = 6;
	private static final int SCALE_Y_INDEX = 7;
	
	private static final int THETA_INDEX = 8;
	
	public SPacket01Login(NetworkComponent networkComp, UserComponent userComp, TransformComponent transformComp) {
		super(TCPPacketType.LOGIN, Integer.toString(networkComp.getId()), userComp.getUsername(), userComp.getIp(), 
				Double.toString(transformComp.getX()), Double.toString(transformComp.getY()), 
				Double.toString(transformComp.getScaleX()), Double.toString(transformComp.getScaleY()),
				Double.toString(transformComp.getTheta()));
	}
	
	public SPacket01Login(String dataAsString) {
		super(dataAsString);
	}
	
	public int getEntityId() {
		return Integer.parseInt(indexedData[ENTITY_ID_INDEX]);
	}
	
	public String getUsername() {
		return indexedData[USERNAME_INDEX];
	}
	
	public String getIp() {
		return indexedData[IP_INDEX];
	}
	
	public double getX() {
		return Double.parseDouble(indexedData[X_INDEX]);
	}
	
	public double getY() {
		return Double.parseDouble(indexedData[Y_INDEX]);
	}
	
	public double getScaleX() {
		return Double.parseDouble(indexedData[SCALE_X_INDEX]);
	}
	
	public double getScaleY() {
		return Double.parseDouble(indexedData[SCALE_Y_INDEX]);
	}
	
	public double getTheta() {
		return Double.parseDouble(indexedData[THETA_INDEX]);
	}
	
}
