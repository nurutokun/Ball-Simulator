package com.rawad.ballsimulator.networking.server.tcp;

import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.networking.TCPPacket;
import com.rawad.ballsimulator.networking.TCPPacketType;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;

public class SPacket01Login extends TCPPacket {
	
	private static final int INDEX_ENTITY_ID = 1;
	
	private static final int INDEX_USERNAME = 2;
	private static final int INDEX_IP = 3;
	
	private static final int INDEX_X = 4;
	private static final int INDEX_Y = 5;
	
	private static final int INDEX_SCALE_X = 6;
	private static final int INDEX_SCALE_Y = 7;
	
	private static final int INDEX_THETA = 8;
	
	private static final int INDEX_LOGIN = 9;
	
	public SPacket01Login(NetworkComponent networkComp, UserComponent userComp, TransformComponent transformComp, 
			boolean login) {
		super(TCPPacketType.LOGIN, Integer.toString(networkComp.getId()), userComp.getUsername(), userComp.getIp(), 
				Double.toString(transformComp.getX()), Double.toString(transformComp.getY()), 
				Double.toString(transformComp.getScaleX()), Double.toString(transformComp.getScaleY()),
				Double.toString(transformComp.getTheta()), Boolean.toString(login));
	}
	
	public SPacket01Login(String dataAsString) {
		super(dataAsString);
	}
	
	public int getEntityId() {
		return Integer.parseInt(indexedData[INDEX_ENTITY_ID]);
	}
	
	public String getUsername() {
		return indexedData[INDEX_USERNAME];
	}
	
	public String getIp() {
		return indexedData[INDEX_IP];
	}
	
	public double getX() {
		return Double.parseDouble(indexedData[INDEX_X]);
	}
	
	public double getY() {
		return Double.parseDouble(indexedData[INDEX_Y]);
	}
	
	public double getScaleX() {
		return Double.parseDouble(indexedData[INDEX_SCALE_X]);
	}
	
	public double getScaleY() {
		return Double.parseDouble(indexedData[INDEX_SCALE_Y]);
	}
	
	public double getTheta() {
		return Double.parseDouble(indexedData[INDEX_THETA]);
	}
	
	public boolean canLogin() {
		return Boolean.parseBoolean(indexedData[INDEX_LOGIN]);
	}
	
}
