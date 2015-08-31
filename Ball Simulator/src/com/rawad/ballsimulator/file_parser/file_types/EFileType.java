package com.rawad.ballsimulator.file_parser.file_types;

public enum EFileType {
	
	SETTINGS("0"),
	MULTIPLAYER_SETTINGS("1");
	
	private final String id;
	
	private EFileType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public static EFileType getTypeById(String id) {
		
		EFileType[] types = EFileType.values();
		
		for(EFileType type: types) {
			
			if(type.getId().equals(id)) {
				return type;
			}
			
		}
		
		return null;
		
	}
	
}
