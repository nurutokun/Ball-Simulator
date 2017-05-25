package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.renderengine.BackgroundRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.ballsimulator.game.World;
import com.rawad.ballsimulator.geometry.Rectangle;
import com.rawad.gamehelpers.client.states.State;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.jfxengine.gui.GuiRegister;
import com.rawad.jfxengine.gui.Root;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class LoadingState extends State {
	
	private World world;
	
	private Entity camera;
	
	private Task<Void> taskToWatch;
	
	@FXML private ProgressBar progressBar;
	@FXML private Label loadingProgressLabel;
	
	public LoadingState(Task<Void> taskToWatch) {
		super();
		
		this.taskToWatch = taskToWatch;	
		
	}
	
	@Override
	public void init() {
		
		world = new World(gameEngine.getEntities());
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		masterRender.getRenders().put(new BackgroundRender(camera));
		
		Root root = GuiRegister.loadGui(this);
		
		Platform.runLater(() -> {
			
			progressBar.progressProperty().bind(taskToWatch.progressProperty());
			loadingProgressLabel.textProperty().bind(taskToWatch.messageProperty());
			
			Rectangle viewport = camera.getComponent(UserViewComponent.class).getViewport();
			viewport.widthProperty().bind(root.widthProperty());
			viewport.heightProperty().bind(root.heightProperty());
			
		});
		
	}
	
	@Override
	public void terminate() {}
	
	@Override
	protected void onActivate() {
		world.addEntity(camera);
	}
	
	@Override
	protected void onDeactivate() {}
	
}
