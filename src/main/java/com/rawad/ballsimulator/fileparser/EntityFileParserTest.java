package com.rawad.ballsimulator.fileparser;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.loader.CustomLoader;
import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;

public class EntityFileParserTest {
	
	public static void main(String... args) {
		
		ResourceManager.init(null);
		
		CustomLoader loader = new CustomLoader();
		
		ArrayList<Class<? extends Component>> componentClasses = new ArrayList<Class<? extends Component>>();
		componentClasses.add(TransformComponent.class);
		componentClasses.add(CollisionComponent.class);
		
		Entity e = EntityFileParser.parseEntityFile(EEntity.class, "Player", TransformComponent.class, CollisionComponent.class);
		
		Logger.log(Logger.DEBUG, "Total components added to this entity: " + e.getComponentsAsList().size());
		
		for(Component comp: e.getComponentsAsList()) {
			Logger.log(Logger.DEBUG, "Listing comp: " + comp.toString());
		}
		
		Entity player = Entity.createEntity();
		
		player.addComponent(new TransformComponent());
		player.addComponent(new CollisionComponent());
		
		EntityFileParser.saveEntityBlueprint(player, loader.getEntityBlueprintSaveFileLocation("Player"), TransformComponent.class, CollisionComponent.class);
		
	}
	
}
