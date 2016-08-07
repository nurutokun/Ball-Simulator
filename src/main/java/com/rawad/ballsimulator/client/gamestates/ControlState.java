package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.client.input.InputBindings;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateChangeRequest;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ControlState extends State {
	
	@FXML private TableView<InputAction> controlsTable;
	
	@FXML private TableColumn<InputAction, String> columnAction;
	@FXML private TableColumn<InputAction, InputAction> columnInput;
	
	@FXML private Button btnOptions;
	@FXML private Button btnMainMenu;
	
	private InputBindings inputBindings;
	
	@Override
	public void initGui() {
		
		GuiRegister.loadGui(this);
		
		Client client = game.getProxies().get(Client.class);
		
		inputBindings = client.getInputBindings();
		
		columnAction.setCellValueFactory(new Callback<CellDataFeatures<InputAction, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<InputAction, String> param) {
				return new ReadOnlyStringWrapper(param.getValue().getName());
			}
		});
		
		columnInput.setCellValueFactory(new Callback<CellDataFeatures<InputAction, InputAction>, 
				ObservableValue<InputAction>>() {
			@Override
			public ObservableValue<InputAction> call(CellDataFeatures<InputAction, InputAction> param) {
				return new ReadOnlyObjectWrapper<InputAction>(param.getValue());
			}
		});
		
		columnInput.setCellFactory(new Callback<TableColumn<InputAction, InputAction>, TableCell<InputAction, InputAction>>() {
			@Override
			public TableCell<InputAction, InputAction> call(TableColumn<InputAction, InputAction> param) {
				return new TableCell<InputAction, InputAction>() {
					
					@Override
					protected void updateItem(InputAction item, boolean empty) {
						super.updateItem(item, empty);
						
						if(empty || item == null) {
							setText(null);
							setGraphic(null);
						} else {
							final Button button = new Button(inputBindings.get(item).getName());
							button.setOnAction(ControlState.createInputChangeHandler(inputBindings, item));
							setGraphic(button);
						}
						
					}
					
				};
			}
		});
		
		for(InputAction action: InputAction.values()) {
			if(action == InputAction.DEFAULT) continue;
			
			controlsTable.getItems().add(action);
			
		}
		
		btnOptions.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(OptionState.class)));
		btnMainMenu.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(MenuState.class)));
		
	}
	
	private static final EventHandler<ActionEvent> createInputChangeHandler(InputBindings inputBindings, 
			InputAction action) {
		return e -> {
			
			ButtonType cancelType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			
			Alert inputAlert = new Alert(AlertType.NONE, "Press Any button", cancelType);
			inputAlert.setTitle("Input Dialog");
			
			DialogPane alertDialog = inputAlert.getDialogPane();
			
			Button cancelButton = (Button) alertDialog.lookupButton(cancelType);
			cancelButton.setFocusTraversable(false);
			cancelButton.setCancelButton(true);
			
			alertDialog.getChildren().remove(cancelButton);
			
			alertDialog.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
				
				inputBindings.put(action, keyEvent.getCode());
				
				cancelButton.fire();
				
				keyEvent.consume();
				
			});
			
			alertDialog.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
				
				inputBindings.put(action, mouseEvent.getButton());
				
				cancelButton.fire();
				
				mouseEvent.consume();
				
			});
			
			alertDialog.requestFocus();
			inputAlert.showAndWait();
			
		};
	}
	/*
	private void createControlsCell(InputBindings inputBindings, InputAction action, int row) {
		
		Label actionLabel = new Label(action.getName());
		
		ComboBox<String> inputButton = new ComboBox<String>();
		
		Button btnAddInput = new Button("Add");
		Button btnRemoveInput = new Button("Remove");
		
		inputButton.setItems(inputBindings.getBindingsMap().get(action));
		inputButton.getSelectionModel().select(0);
		
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
				
				Input input = new Input(keyEvent.getCode());
				
				inputBindings.put(action, input);
				inputButton.getSelectionModel().select(input);
				
				cancelButton.fire();
				
				keyEvent.consume();
				
			});
			
			alertDialog.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
				
				Input input = new Input(mouseEvent.getButton());
				
				inputBindings.put(action, input);
				inputButton.getSelectionModel().select(input);
				
				cancelButton.fire();
				
				mouseEvent.consume();
				
			});
			
			alertDialog.requestFocus();
			inputAlert.showAndWait();
			
		});
		
		btnRemoveInput.setOnAction(e -> {
			
			try {
				
				AInput inputToRemove = inputButton.getSelectionModel().getSelectedItem();
				
				if(inputBindings.getBindingsMap().remove(action, inputToRemove)) {
					inputButton.getSelectionModel().selectFirst();
				}
				
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
		});
		
		controlsTable.add(actionLabel, 0, row);
		controlsTable.add(inputButton, 1, row);
		
	}*/
	
}
