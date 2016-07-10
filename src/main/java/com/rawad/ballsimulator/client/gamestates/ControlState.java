package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateChangeRequest;
import com.rawad.gamehelpers.client.input.InputBindings;
import com.rawad.gamehelpers.log.Logger;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ControlState extends State {
	
	@FXML private TableView<InputAction> controlTable;
	
	@FXML private TableColumn<InputAction, String> actionColumn;
	@FXML private TableColumn<InputAction, ObservableList<String>> bindingColumn;
	
	@FXML private Button btnOptions;
	@FXML private Button btnMainMenu;
	
	@FXML private Button btnAdd;
	@FXML private Button btnRemove;
	
	private InputAction actionToRemoveInputFrom;
	private Object inputToRemove;
	
	@Override
	public void initGui() {
		
		GuiRegister.loadGui(this);
		
		for(InputAction action: InputAction.values()) {
			if(action == InputAction.DEFAULT) continue;
			controlTable.getItems().add(action);
		}
		
		actionColumn.setCellValueFactory(new Callback<CellDataFeatures<InputAction, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<InputAction, String> param) {
				
				String action = param.getValue().getName();
				
				return new ReadOnlyObjectWrapper<String>(action);
				
			}
		});
		
		Client client = game.getProxies().get(Client.class);
		InputBindings inputBindings = client.getInputBindings();
		
		bindingColumn.setCellValueFactory(new Callback<CellDataFeatures<InputAction, ObservableList<String>>, 
				ObservableValue<ObservableList<String>>>() {
			@Override
			public ObservableValue<ObservableList<String>> call(CellDataFeatures<InputAction, 
					ObservableList<String>> param) {
				
				ObservableList<Object> inputs = FXCollections.observableArrayList(inputBindings.getBindingsMap()
						.get(param.getValue()));
				
				ObservableList<String> cellValue = FXCollections.observableArrayList();
				
				if(inputs != null) {
					for(Object input: inputs) {
						
						String name = input.toString();
						
						if(input instanceof KeyCode) {
							name = ((KeyCode) input).getName();
						} else if(input instanceof MouseButton) {
							name = ((MouseButton) input).name();
						}
						
						cellValue.add(name);
						
					}
				}
				
				return new ReadOnlyObjectWrapper<ObservableList<String>>(cellValue);
				
			}
		});
		
		ObservableList<ObservableList<String>> columnData = FXCollections.observableArrayList();
		
		for(InputAction action: controlTable.getItems()) {
			columnData.add(bindingColumn.getCellObservableValue(action).getValue());
		}
		
		bindingColumn.setCellFactory(new Callback<TableColumn<InputAction, ObservableList<String>>, 
				TableCell<InputAction, ObservableList<String>>>() {
			@Override
			public TableCell<InputAction, ObservableList<String>> call(TableColumn<InputAction, 
					ObservableList<String>> param) {
				
				return new TableCell<InputAction, ObservableList<String>>() {
					
					@Override
					public void updateIndex(int i) {
						super.updateIndex(i);
						
						ObservableList<String> item = param.getCellData(i);
						
						ListView<String> itemView = new ListView<String>();
						
						if(item != null) {
							itemView.setItems(item);
//							itemView.getSelectionModel().select(0);
							Logger.log(Logger.DEBUG, "Added " + item.size() + " to this list view.");
						}
						
						itemView.getSelectionModel().selectedItemProperty().addListener((e, oldInput, newInput) -> {
							
						});
						
						setGraphic(itemView);
						
					}
					
				};
			}
		});
		
		btnOptions.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(OptionState.class)));
		btnMainMenu.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(MenuState.class)));
		
		btnAdd.setOnAction(e -> {
			
			InputAction actionOfInput = controlTable.getSelectionModel().getSelectedItem();
			
			if(actionOfInput == null) return;
			
			ButtonType cancelType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			
			Alert inputAlert = new Alert(AlertType.NONE, "Press Any button", cancelType);
			inputAlert.setTitle("Input Dialog");
			
			DialogPane alertDialog = inputAlert.getDialogPane();
			
			Button cancelButton = (Button) alertDialog.lookupButton(cancelType);
			cancelButton.setFocusTraversable(false);
			
			alertDialog.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
				
				inputBindings.put(actionOfInput, keyEvent.getCode());
				
				cancelButton.fire();
				
				keyEvent.consume();
				
			});
			
			alertDialog.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
				System.out.println("got that mouse press.");
				inputBindings.put(actionOfInput, mouseEvent.getButton());
				
				cancelButton.fire();
				
				mouseEvent.consume();
				
			});
			
			inputAlert.showAndWait();
			
		});
		btnRemove.setOnAction(e -> {
			
			InputAction actionToRemoveFrom = controlTable.getSelectionModel().getSelectedItem();
			
		});
		
	}
	
}
