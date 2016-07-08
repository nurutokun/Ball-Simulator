package com.rawad.ballsimulator.server.gui;

import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.gui.Root;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.game.CameraRoamingSystem;
import com.rawad.ballsimulator.game.MovementControlSystem;
import com.rawad.ballsimulator.game.RenderingSystem;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.utils.ClassMap;

import javafx.scene.input.KeyEvent;

public class WorldViewState extends State {
	
	private static final double PREFERRED_SCALE = 1d;
	
	private MovementControlSystem movementControlSystem;
	private CameraRoamingSystem cameraRoamingSystem;
	
	private Entity camera;
	private UserViewComponent cameraView;
	
	@Override
	public void init(StateManager sm) {
		super.init(sm);
		
		this.world = game.getWorld();
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		camera.addComponent(new MovementComponent());
		camera.addComponent(new UserControlComponent());
		
		TransformComponent cameraTransform = camera.getComponent(TransformComponent.class);
		
		cameraTransform.setMaxScaleX(5D);
		cameraTransform.setMaxScaleY(5D);
		
		cameraView = camera.getComponent(UserViewComponent.class);
		cameraView.setPreferredScaleX(PREFERRED_SCALE);
		cameraView.setPreferredScaleY(PREFERRED_SCALE);
		
		world.addEntity(camera);
		
		ServerGui client = game.getProxies().get(ServerGui.class);
		
		WorldRender worldRender = new WorldRender(world, camera);
		
		masterRender.getRenders().put(worldRender);
		masterRender.getRenders().put(new DebugRender(client, camera));
		
		movementControlSystem = new MovementControlSystem(client.getInputBindings());
		cameraRoamingSystem = new CameraRoamingSystem(true, world.getWidth(), world.getHeight());
		
		ClassMap<GameSystem> gameSystems = sm.getGame().getGameEngine().getGameSystems();
		
		gameSystems.put(movementControlSystem);
		gameSystems.put(cameraRoamingSystem);
		gameSystems.put(new RenderingSystem(worldRender));
		
	}
	
	@Override
	public void initGui() {
		
		Root root = GuiRegister.loadGui(this);
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			InputAction action = (InputAction) game.getProxies().get(ServerGui.class).getInputBindings()
					.get(keyEvent.getCode());
			
			switch(action) {
			
			case SHOW_WORLD:
				cameraView.setShowEntireWorld(!cameraView.isShowEntireWorld());
				break;
				
			default:
				break;
			
			}
			
		});
		
		root.widthProperty().addListener(e -> cameraView.getRequestedViewport().setWidth(root.getWidth()));
		root.heightProperty().addListener(e -> cameraView.getRequestedViewport().setHeight(root.getHeight()));
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, movementControlSystem);
		root.addEventHandler(KeyEvent.KEY_RELEASED, movementControlSystem);
		
	}
		
}
