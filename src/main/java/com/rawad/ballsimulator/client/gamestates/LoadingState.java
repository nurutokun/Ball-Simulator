package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.gui.Root;
import com.rawad.ballsimulator.client.renderengine.BackgroundRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * This class could be replaced with some sort of {@code PreLoader}.
 * 
 * @author Rawad
 *
 */
public class LoadingState extends State {
	
	private Entity camera;
	
	private Task<Void> taskToWatch;
	
	@FXML private ProgressBar progressBar;
	@FXML private Label loadingProgressLabel;
	
	public LoadingState(Task<Void> taskToWatch) {
		super();
		
		this.taskToWatch = taskToWatch;	
		
		camera = Entity.createEntity(EEntity.CAMERA);
		
		masterRender.getRenders().put(new BackgroundRender(camera));
		
	}
	
	@Override
	public void initGui() {
		
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
	protected void onActivate() {
		super.onActivate();
		
		world.addEntity(camera);
		
	}
	
}
