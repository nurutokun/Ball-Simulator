package com.rawad.ballsimulator.fileparser;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.HealthComponent;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.PlaceableComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.RollingComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;

public class EntityBlueprintFileMaker {
	
	public static void main(String... args) {
		
		final String ENTITY_PACKAGE = EEntity.class.getPackage().getName();
		
		ResourceManager.init(Util.parseCommandLineArguments(args));
		
		CustomLoader loader = new CustomLoader();
		
		Entity player = Entity.createEntity();
		
		CollisionComponent playerCollision = new CollisionComponent();
		
		playerCollision.setHitbox(new Rectangle(-20, -20, 40, 40));
		
		HealthComponent playerHealth = new HealthComponent();
		playerHealth.setHealth(20);
		playerHealth.setMaxHealth(20);
		playerHealth.setRegen(true);
		playerHealth.setRegenRate(0.1);
		
		player.addComponent(new TransformComponent());
		player.addComponent(playerCollision);
		player.addComponent(playerHealth);
		player.addComponent(new MovementComponent());// Could customize how fast entities can go here.
		player.addComponent(new RollingComponent());
		player.addComponent(new RenderingComponent());
		
		EntityFileParser.saveEntityBlueprint(player, loader.getEntityBlueprintSaveFileLocation(EEntity.PLAYER.getFileName()), ENTITY_PACKAGE);
		
		Entity eStatic = Entity.createEntity();
		
		CollisionComponent staticCollision = new CollisionComponent();
		staticCollision.setHitbox(new Rectangle(-16, -16, 32, 32));
		
		eStatic.addComponent(new TransformComponent());
		eStatic.addComponent(staticCollision);
		eStatic.addComponent(new SelectionComponent());
		eStatic.addComponent(new RenderingComponent());
		
		EntityFileParser.saveEntityBlueprint(eStatic, loader.getEntityBlueprintSaveFileLocation(EEntity.STATIC.getFileName()), ENTITY_PACKAGE);
		
		Entity camera = Entity.createEntity();
		
		camera.addComponent(new TransformComponent());
		camera.addComponent(new UserViewComponent());
		
		EntityFileParser.saveEntityBlueprint(camera, loader.getEntityBlueprintSaveFileLocation(EEntity.CAMERA.getFileName()), ENTITY_PACKAGE);
		
		Entity placeable = Entity.createEntity();
		
		placeable.addComponent(new TransformComponent());
		placeable.addComponent(staticCollision);// For now...
		placeable.addComponent(new SelectionComponent());
		placeable.addComponent(new PlaceableComponent());
		placeable.addComponent(new RenderingComponent());
		
		EntityFileParser.saveEntityBlueprint(placeable, loader.getEntityBlueprintSaveFileLocation(EEntity.PLACEABLE.getFileName()), ENTITY_PACKAGE);
		
	}
	
}
