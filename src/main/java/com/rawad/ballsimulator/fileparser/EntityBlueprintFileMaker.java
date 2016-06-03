package com.rawad.ballsimulator.fileparser;

import com.rawad.ballsimulator.entity.AttachmentComponent;
import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.HealthComponent;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.RollingComponent;
import com.rawad.ballsimulator.entity.SelectionComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;

public class EntityBlueprintFileMaker {
	
	public static void main(String... args) {
		
		ResourceManager.init(Util.parseCommandLineArguments(args));
		
		CustomLoader loader = new CustomLoader();
		
		Entity player = Entity.createEntity();
		
		player.addComponent(new TransformComponent());
		player.addComponent(new CollisionComponent());
		player.addComponent(new HealthComponent());
		player.addComponent(new MovementComponent());
		player.addComponent(new RollingComponent());
		player.addComponent(new RenderingComponent());
		
		EntityFileParser.saveEntityBlueprint(player, loader.getEntityBlueprintSaveFileLocation(EEntity.PLAYER.getFileName()), EEntity.class.getPackage().getName());
		
		Entity eStatic = Entity.createEntity();
		
		eStatic.addComponent(new TransformComponent());
		eStatic.addComponent(new CollisionComponent());
		eStatic.addComponent(new SelectionComponent());
		eStatic.addComponent(new RenderingComponent());
		
		EntityFileParser.saveEntityBlueprint(eStatic, loader.getEntityBlueprintSaveFileLocation(EEntity.STATIC.getFileName()), EEntity.class.getPackage().getName());
		
		Entity camera = Entity.createEntity();
		
		camera.addComponent(new TransformComponent());
		camera.addComponent(new AttachmentComponent());
		camera.addComponent(new UserViewComponent());
		
		EntityFileParser.saveEntityBlueprint(camera, loader.getEntityBlueprintSaveFileLocation(EEntity.CAMERA.getFileName()), EEntity.class.getPackage().getName());
		
	}
	
}
