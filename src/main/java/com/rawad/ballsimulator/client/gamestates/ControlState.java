package com.rawad.ballsimulator.client.gamestates;

import java.util.ArrayList;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateChangeRequest;
import com.rawad.gamehelpers.client.input.InputBindings;
import com.rawad.gamehelpers.log.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class ControlState extends State {
	
	/** Represents the number of nodes created by the {@link #createControlsCell(InputBindings, InputAction)} method. */
	private static final int COLS = 4;
	
	@FXML private GridPane controlsDisplay;
	
	@FXML private Button btnOptions;
	@FXML private Button btnMainMenu;
	
	@Override
	public void initGui() {
		
		GuiRegister.loadGui(this);
		
		Client client = game.getProxies().get(Client.class);
		InputBindings inputBindings = client.getInputBindings();
		
		for(int i = 0; i < COLS; i++) {
			
			ColumnConstraints col = new ColumnConstraints();
			col.setPercentWidth(i / COLS * 100d);
			
			controlsDisplay.getColumnConstraints().add(col);
			
		}
		
		int rowIndex = 0;
		
		for(InputAction action: InputAction.values()) {
			if(action == InputAction.DEFAULT) continue;
			
			RowConstraints row = new RowConstraints();
			controlsDisplay.getRowConstraints().add(row);
			
			createControlsCell(inputBindings, action, rowIndex++);
			
		}
		
		btnOptions.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(OptionState.class)));
		btnMainMenu.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(MenuState.class)));
		
	}
	
	/**
	 * Returns the equivalent of a row in a {@code TableView}.
	 * 
	 * @param inputBindings
	 * @param action
	 * @param row
	 * 
	 */
	private void createControlsCell(InputBindings inputBindings, InputAction action, int row) {
		
		Label actionLabel = new Label(action.getName());
		
		ComboBox<Object> inputsSelector = new ComboBox<Object>();
		
		Button btnAddInput = new Button("Add");
		Button btnRemoveInput = new Button("Remove");
		
		ObservableList<Object> oInputs = FXCollections.observableArrayList(inputBindings.getBindingsMap().get(action));
		
		inputsSelector.setItems(oInputs);
		inputsSelector.getSelectionModel().select(0);
		
		btnAddInput.setOnAction(e -> {
			
			ButtonType cancelType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			
			Alert inputAlert = new Alert(AlertType.NONE, "Press Any button", cancelType);
			inputAlert.setTitle("Input Dialog");
			
			DialogPane alertDialog = inputAlert.getDialogPane();
			
			Button cancelButton = (Button) alertDialog.lookupButton(cancelType);
			cancelButton.setFocusTraversable(false);
			cancelButton.setCancelButton(true);
			
			alertDialog.getChildren().remove(cancelButton);
			
			alertDialog.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
				
				inputBindings.getBindingsMap().get(action).add(keyEvent.getCode());
				Logger.log(Logger.DEBUG, "(K) Added " + keyEvent.getCode().getName() + " to "+ action.getName() + ".");
				cancelButton.fire();
				
				keyEvent.consume();
				
			});
			
			alertDialog.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
				
				inputBindings.getBindingsMap().get(action).add(mouseEvent.getButton());
				Logger.log(Logger.DEBUG, "(M) Added " + mouseEvent.getButton().name() + " to "+ action.getName() + ".");
				cancelButton.fire();
				
				mouseEvent.consume();
				
			});
			
			inputAlert.showAndWait();
			
		});
		
		btnRemoveInput.setOnAction(e -> {
			
			ArrayList<Object> inputs = inputBindings.getBindingsMap().get(action);
			
			try {
				
				Object inputToRemove = inputsSelector.getSelectionModel().getSelectedItem();
				
				if(inputs.remove(inputToRemove)) {
					inputsSelector.getItems().remove(inputToRemove);
					inputsSelector.getSelectionModel().select(0);
				}
				
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
		});
		
		controlsDisplay.add(actionLabel, 0, row);
		controlsDisplay.add(inputsSelector, 1, row);
		controlsDisplay.add(btnAddInput, 2, row);
		controlsDisplay.add(btnRemoveInput, 3, row);
		
	}
	
}
