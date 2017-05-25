package com.rawad.ballsimulator.server.gui;

import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.client.renderengine.DebugRender;
import com.rawad.ballsimulator.client.renderengine.WorldRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.entity.UserControlComponent;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.game.CameraRoamingSystem;
import com.rawad.ballsimulator.game.RenderingSystem;
import com.rawad.ballsimulator.game.event.MovementControlHandler;
import com.rawad.ballsimulator.server.Server;
import com.rawad.ballsimulator.server.WorldMP;
import com.rawad.gamehelpers.client.states.State;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.jfxengine.gui.GuiRegister;
import com.rawad.jfxengine.gui.Root;

import javafx.scene.input.KeyEvent;

public class WorldViewState extends State {
	
	private static final double PREFERRED_SCALE = 1d;
	
	private WorldMP world;
	
	private CameraRoamingSystem cameraRoamingSystem;
	
	private Entity camera;
	private UserViewComponent cameraView;
	
	@Override
	public void init() {
		
		Server server = game.getProxies().get(Server.class);
		
		this.world = server.getWorld();
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		camera.addComponent(new MovementComponent());
		camera.addComponent(new UserControlComponent());
		
		TransformComponent cameraTransform = camera.getComponent(TransformComponent.class);
		
		cameraTransform.setScaleX(1d);
		cameraTransform.setScaleY(1d);
		
		cameraTransform.setMaxScaleX(5d);
		cameraTransform.setMaxScaleY(5d);
		
		cameraView = camera.getComponent(UserViewComponent.class);
		cameraView.setPreferredScaleX(PREFERRED_SCALE);
		cameraView.setPreferredScaleY(PREFERRED_SCALE);
		
		world.addEntity(camera);
		
		ServerGui client = game.getProxies().get(ServerGui.class);
		
		WorldRender worldRender = new WorldRender(world, camera);
		
		masterRender.getRenders().put(worldRender);
		masterRender.getRenders().put(new DebugRender(world, client.getRenderingTimer(), camera));
		
		cameraRoamingSystem = new CameraRoamingSystem(eventManager, true, world.getWidth(), world.getHeight());
		
		gameEngine.addGameSystem(cameraRoamingSystem);
		gameEngine.addGameSystem(new RenderingSystem(worldRender));
		
		Root root = GuiRegister.loadGui(this);
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			
			Object action = client.getInputBindings().get(keyEvent.getCode());
			
			if(!(action instanceof InputAction)) return;
			
			switch((InputAction) action) {
			
			case SHOW_WORLD:
				cameraView.setShowEntireWorld(!cameraView.isShowEntireWorld());
				break;
				
			default:
				break;
			
			}
			
		});
		
		root.widthProperty().addListener(e -> cameraView.getRequestedViewport().setWidth(root.getWidth()));
		root.heightProperty().addListener(e -> cameraView.getRequestedViewport().setHeight(root.getHeight()));
		
		MovementControlHandler movementControlHadnler = new MovementControlHandler(game.getGameEngine(), 
				client.getInputBindings(), camera);
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, movementControlHadnler);
		root.addEventHandler(KeyEvent.KEY_RELEASED, movementControlHadnler);
		
	}
	
	@Override
	public void terminate() {}
	
	@Override
	protected void onActivate() {}
	
	@Override
	protected void onDeactivate() {}
	
}
