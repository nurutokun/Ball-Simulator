package com.rawad.ballsimulator.server.gui;

import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.ballsimulator.game.CameraRoamingSystem;
import com.rawad.ballsimulator.game.MovementControlSystem;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.scene.input.KeyEvent;

public class WorldViewState extends State {
	
	private MovementControlSystem movementControlSystem;
	private CameraRoamingSystem cameraRoamingSystem;
	
	private Entity camera;
	
	public WorldViewState(StateManager sm) {
		super(sm);
		
		this.world = game.getWorld();
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		camera.addComponent(new MovementComponent());
		camera.addComponent(new UserControlComponent());
		
		TransformComponent cameraTransform = camera.getComponent(TransformComponent.class);
		
		cameraTransform.setScaleX(1d);
		cameraTransform.setScaleY(1d);
		
		cameraTransform.setMaxScaleX(5D);
		cameraTransform.setMaxScaleY(5D);
		
		world.addEntity(camera);
		
		ServerGui client = game.getProxies().get(ServerGui.class);
		
		masterRender.getRenders().put(new WorldRender(world, camera));
		masterRender.getRenders().put(new DebugRender(client, camera));
		
		movementControlSystem = new MovementControlSystem(client.getInputBindings());
		cameraRoamingSystem = new CameraRoamingSystem(true, world.getWidth(), world.getHeight());
		
		client.getGame().getGameEngine().getGameSystems().put(movementControlSystem);
		client.getGame().getGameEngine().getGameSystems().put(cameraRoamingSystem);
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		root.widthProperty().addListener(e -> cameraRoamingSystem.requestNewViewportWidth(root.getWidth()));
		root.heightProperty().addListener(e -> cameraRoamingSystem.requestNewViewportHeight(root.getHeight()));
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, movementControlSystem);
		root.addEventHandler(KeyEvent.KEY_RELEASED, movementControlSystem);
		
	}
		
}
