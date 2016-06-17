package com.rawad.ballsimulator.networking;

public enum UDPPacketType {
	
	INVALID("00"),
	DAMAGE("01"),
	MOVE("02"),
	PING("03");
	
	private final String id;
	
	private UDPPacketType(String id) {
		this.id = id;
	}
	
	public static UDPPacketType getPacketTypeById(String id) {
		
		UDPPacketType[] types = UDPPacketType.values();
		
		for(UDPPacketType type: types) {
			
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
