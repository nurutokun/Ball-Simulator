package com.rawad.ballsimulator.fileparser;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.CollisionComponent;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.geometry.Rectangle;
import com.rawad.gamehelpers.utils.Util;

public class TerrainFileParser extends FileParser {
	
	private static final String REGEX = ",";
	
	private static final int INDEX_X = 0;
	private static final int INDEX_Y = 1;
	private static final int INDEX_WIDTH = 2;
	private static final int INDEX_HEIGHT = 3;
	
	private ArrayList<Entity> staticEntities = new ArrayList<Entity>();
	
	private World world;
	
	@Override
	protected void parseLine(String line) {
		
		String[] tokens = line.split(REGEX);
		
		double x = Util.parseDouble(tokens[INDEX_X]);
		double y = Util.parseDouble(tokens[INDEX_Y]);
		
		double width = Util.parseDouble(tokens[INDEX_WIDTH]);
		double height = Util.parseDouble(tokens[INDEX_HEIGHT]);
		
		Entity staticEntity = Entity.createEntity(EEntity.STATIC);
		world.addEntity(staticEntity);
		staticEntities.add(staticEntity);
		
		TransformComponent transformComp = staticEntity.getComponent(TransformComponent.class);
		transformComp.setX(x);
		transformComp.setY(y);
		
		CollisionComponent collisionComp = staticEntity.getComponent(CollisionComponent.class);
		
		Rectangle hitbox = collisionComp.getHitbox();
		hitbox.setX(x);
		hitbox.setY(y);
		hitbox.setWidth(width);
		hitbox.setHeight(height);
		
	}
	
	@Override
	protected void start() {
		super.start();
		
		staticEntities = new ArrayList<Entity>();
		
	}
	
	@Override
	public String getContent() {
		
		String[] lines = new String[staticEntities.size()];
		
		int i = 0;
		
		for(Entity staticEntity: staticEntities) {
			
			CollisionComponent collisionComp = staticEntity.getComponent(CollisionComponent.class);
			
			Rectangle hitbox = collisionComp.getHitbox();
			
			lines[i] = hitbox.getX() + REGEX + hitbox.getY() + REGEX + hitbox.getWidth() + REGEX + hitbox.getHeight();
			
			i++;
		}
		
		return Util.getStringFromLines(lines, Util.NL, false);
		
	}
	
	public void setWorld(World world) {
		this.world = world;
		
		world.getEntitiesAsList().removeAll(staticEntities);
		staticEntities.clear();
		
	}
	
}
