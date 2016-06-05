package com.rawad.ballsimulator.entity;

public enum EEntity {
	
	CAMERA("Camera"),
	PLAYER("Player"),
	STATIC("Static"),
	PLACEABLE("Placeable");
	
	private final String fileName;
	
	/**
	 * The {@code entityBlueprintFilename} specified should be found in the same package as this {@code EEntity} class.
	 * 
	 * @param entityBlueprintFilename
	 */
	private EEntity(String entityBlueprintFilename) {
		this.fileName = entityBlueprintFilename;
	}
	
	public String getFileName() {
		return fileName;
	}
	
}
