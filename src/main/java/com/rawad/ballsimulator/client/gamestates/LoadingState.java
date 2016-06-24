package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.gui.Transitions;
import com.rawad.ballsimulator.client.renderengine.BackgroundRender;
import com.rawad.ballsimulator.entity.EEntity;
import com.rawad.ballsimulator.entity.UserViewComponent;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.geometry.Rectangle;

import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
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
	
	public LoadingState(StateManager sm, Task<Void> taskToWatch) {
		super(sm);
		
		this.taskToWatch = taskToWatch;	
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		progressBar.progressProperty().bind(taskToWatch.progressProperty());
		loadingProgressLabel.textProperty().bind(taskToWatch.messageProperty());
		taskToWatch.runningProperty().addListener((e, prevRunning, currentlyRunning) -> {
			
			if(currentlyRunning) {
				
				camera = Entity.createEntity(EEntity.CAMERA);// Waits until entities are loaded (this State is special).
				
				world.addEntity(camera);
				
				masterRender.getRenders().put(new BackgroundRender(camera));
				
				Rectangle viewport = camera.getComponent(UserViewComponent.class).getViewport();
				viewport.widthProperty().bind(root.widthProperty());
				viewport.heightProperty().bind(root.heightProperty());
				
			}
			
		});
		
	}
	
	@Override
	public Transition getOnDeactivateTransition() {
		ParallelTransition transition = Transitions.stateOnDeactivate(guiContainer, Transitions.DEFAULT_DURATION);
		
		TranslateTransition slide = (TranslateTransition) transition.getChildren().get(0);
		slide.setToX(-guiContainer.getWidth());
		
		return transition;
	}
	
}
