package com.rawad.ballsimulator.entity;

public enum EEntity {
	
	CAMERA("Camera"),
	PLAYER("Player"),
	STATIC("Static"),
	PLACEABLE("Placeable");
	
	private final String name;
	
	/**
	 * The {@code entityBlueprintName} specified should be found in the same package as this {@code EEntity} class.
	 * 
	 * @param entityBlueprintName
	 */
	private EEntity(String entityBlueprintName) {
		this.name = entityBlueprintName;
	}
	
	public String getName() {
		return name;
	}
	
	public static EEntity getByName(String name) {
		
		EEntity[] entityBlueprints = EEntity.values();
		
		for(EEntity entityBlueprint: entityBlueprints) {
			if(entityBlueprint.getName().equals(name)) return entityBlueprint;
		}
		
		return null;
		
	}
	
}
