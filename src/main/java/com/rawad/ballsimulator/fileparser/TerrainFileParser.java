package com.rawad.ballsimulator.fileparser;

import java.util.ArrayList;

import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.game.World;
import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.game.entity.BlueprintManager;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.utils.Util;

public class TerrainFileParser extends FileParser {
	
	public static final Double[] DIMS = {1D/16D, 1D/8D, 1D/4D, 1D/2D, 1D, 2D, 4D, 8D, 16D};// Base: 32x32
	
	private static final String REGEX = ",";
	
	private static final int INDEX_X = 0;
	private static final int INDEX_Y = 1;
	private static final int INDEX_SCALE_X = 2;
	private static final int INDEX_SCALE_Y = 3;
	
	private ArrayList<Entity> staticEntities = new ArrayList<Entity>();
	
	private World world;
	
	@Override
	protected void start() {
		super.start();
		
		world.removeAllEntities(staticEntities);
		staticEntities.clear();
		
	}
	
	@Override
	protected void parseLine(String line) {
		
		String[] tokens = line.split(REGEX);
		
		double x = Util.parseDouble(tokens[INDEX_X]);
		double y = Util.parseDouble(tokens[INDEX_Y]);
		
		double scaleX = Util.parseDouble(tokens[INDEX_SCALE_X]);
		double scaleY = Util.parseDouble(tokens[INDEX_SCALE_Y]);
		
		Entity staticEntity = Entity.createEntity(EEntity.STATIC);
		
		TransformComponent transformComp = staticEntity.getComponent(TransformComponent.class);
		transformComp.setX(x);
		transformComp.setY(y);
		transformComp.setScaleX(scaleX);
		transformComp.setScaleY(scaleY);
		
		staticEntities.add(staticEntity);
		
	}
	
	@Override
	protected void stop() {
		super.stop();
		
		for(Entity staticEntity: staticEntities) {
			world.addEntity(staticEntity);
		}
		
	}
	
	@Override
	public String getContent() {
		
		ArrayList<String> lines = new ArrayList<String>();
		
		for(Entity e: world.getEntities()) {
			
			if(Entity.compare(e, BlueprintManager.getBlueprint(EEntity.STATIC).getEntityBase())) {
// TODO: Test if this compare works properly (Entity.compare(Entity, Class<? extends Component>[]) may be deprecated now).
				
				TransformComponent transformComp = e.getComponent(TransformComponent.class);
				
				lines.add(transformComp.getX() + REGEX + transformComp.getY() + REGEX + transformComp.getScaleX() + REGEX
						+ transformComp.getScaleY());
				
			}
			
		}
		
		return Util.getStringFromLines(Util.NL, false, lines);
		
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
}
