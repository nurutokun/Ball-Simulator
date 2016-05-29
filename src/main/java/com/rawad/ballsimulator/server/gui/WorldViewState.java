package com.rawad.ballsimulator.server.gui;

import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.game.CameraRoamingSystem;
import com.rawad.ballsimulator.game.MovementControlSystem;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.world.World;

import javafx.scene.input.KeyEvent;

public class WorldViewState extends State {
	
	private MovementControlSystem movementControlSystem;
	
	private Entity camera;
	
	public WorldViewState(World worldToView) {
		super();
		
		this.world = worldToView;
		
		camera = Entity.createEntity(EEntity.USER_CONTROLLABLE_CAMERA);
		
		world.addEntity(camera);
		
		masterRender.registerRender(new WorldRender(world, camera));
		masterRender.registerRender(new DebugRender(camera));
		
		movementControlSystem = new MovementControlSystem();
		
		gameSystems.add(movementControlSystem);
		gameSystems.add(new CameraRoamingSystem(true, world.getWidth(), world.getHeight()));
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		UserViewComponent userView = camera.getComponent(UserViewComponent.class);
		
		userView.getViewport().widthProperty().bind(root.widthProperty());
		userView.getViewport().heightProperty().bind(root.heightProperty());
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, movementControlSystem);
		root.addEventHandler(KeyEvent.KEY_RELEASED, movementControlSystem);
		
	}
	
}
