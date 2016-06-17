package com.rawad.ballsimulator.networking;

public enum TCPPacketType {
	
	INVALID("00"),
	LOGIN("01"),
	LOGOUT("02"),
	MESSAGE("03"),
	ENTITY("04");
	
	private final String id;
	
	private TCPPacketType(String id) {
		this.id = id;
	}
	
	public static TCPPacketType getPacketTypeById(String id) {
		
		TCPPacketType[] types = TCPPacketType.values();
		
		for(TCPPacketType type: types) {
			
			if(id.equals(type.getId())) {
				return type;
			}
			
		}
		
		return INVALID;
		
	}
	
	public String getId() {
		return id;
	}
		
}
