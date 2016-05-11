package com.rawad.ballsimulator.client.gamestates;

import com.rawad.gamehelpers.client.gamestates.State;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * This class could be replaced with some sort of <code>PreLoader</code>
 * 
 * @author Rawad
 *
 */
public class LoadingState extends State {
	
	private Task<Integer> taskToWatch;
	
	@FXML private ProgressBar progressBar;
	@FXML private Label loadingProgressLabel;
	
	public LoadingState(Task<Integer> taskToWatch) {
		this.taskToWatch = taskToWatch;	
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		progressBar.progressProperty().bind(taskToWatch.progressProperty());
		loadingProgressLabel.textProperty().bind(taskToWatch.messageProperty());
		
	}
	
}
