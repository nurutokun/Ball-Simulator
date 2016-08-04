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
import com.rawad.ballsimulator.geometry.Rectangle;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.entity.Entity;

public class EntityBlueprintFileMaker {
	
	public static void main(String... args) {
		
		final String[] contextPaths = {
				EEntity.class.getPackage().getName()
		};
		
		Loader loader = new Loader();
		EntityFileParser parser = new EntityFileParser();
		
		parser.setContextPaths(contextPaths);
		
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
		
		parser.saveEntityBlueprint(player, loader.getEntityBlueprintPath(EEntity.PLAYER.getName()));
		
		Entity eStatic = Entity.createEntity();
		
		CollisionComponent staticCollision = new CollisionComponent();
		staticCollision.setHitbox(new Rectangle(-16, -16, 32, 32));
		
		eStatic.addComponent(new TransformComponent());
		eStatic.addComponent(staticCollision);
		eStatic.addComponent(new SelectionComponent());
		eStatic.addComponent(new RenderingComponent());
		
		parser.saveEntityBlueprint(eStatic, loader.getEntityBlueprintPath(EEntity.STATIC.getName()));
		
		Entity camera = Entity.createEntity();
		
		camera.addComponent(new TransformComponent());
		camera.addComponent(new UserViewComponent());
		
		parser.saveEntityBlueprint(camera, loader.getEntityBlueprintPath(EEntity.CAMERA.getName()));
		
		Entity placeable = Entity.createEntity();
		
		placeable.addComponent(new TransformComponent());
		placeable.addComponent(staticCollision);// For now...
		placeable.addComponent(new SelectionComponent());
		placeable.addComponent(new PlaceableComponent());
		placeable.addComponent(new RenderingComponent());
		
		parser.saveEntityBlueprint(placeable, loader.getEntityBlueprintPath(EEntity.PLACEABLE.getName()));
		
	}
	
}
