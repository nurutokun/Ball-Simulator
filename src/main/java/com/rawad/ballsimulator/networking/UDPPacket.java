package com.rawad.ballsimulator.networking;

import com.rawad.gamehelpers.utils.Util;

public abstract class UDPPacket extends APacket {
	
	/**
	 * Index of the {@code Entity}'s id in the {@code indexedData} array of {@code APacket}.
	 */
	private static final int ENTITY_ID_INDEX = 1;
	
	public UDPPacket(UDPPacketType type, int entityId, String... data) {
		super(APacket.getFormattedDataString(REGEX, Util.prepend(data, type.getId(), Integer.toString(entityId))));
	}
	
	public UDPPacket(String dataAsString) {
		super(dataAsString);
	}
	
	public int getEntityId() {
		return Integer.parseInt(indexedData[ENTITY_ID_INDEX]);
	}
	
	public static int getEntityIdFromString(String dataAsString) {
		
		String[] splitData = dataAsString.split(REGEX);
		
		return Integer.parseInt(splitData[ENTITY_ID_INDEX]);
		
	}
	
	public static UDPPacketType getPacketTypeFromData(String dataAsString) {
		return UDPPacketType.getPacketTypeById(APacket.getPacketIdFromString(dataAsString));
	}
	
}
