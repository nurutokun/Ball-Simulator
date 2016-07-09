package com.rawad.ballsimulator.client.gamestates;

import java.util.ArrayList;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.gui.Root;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateChangeRequest;
import com.rawad.gamehelpers.client.input.InputBindings;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ControlState extends State {
	
	@FXML private TableView<InputAction> controlTable;
	
	@FXML private TableColumn<InputAction, String> actionColumn;
	@FXML private TableColumn<InputAction, ArrayList<String>> bindingColumn;
	
	@FXML private Button btnOptions;
	@FXML private Button btnMainMenu;
	
	@Override
	public void initGui() {
		
		Root root = GuiRegister.loadGui(this);
		
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
		
		bindingColumn.setCellValueFactory(new Callback<CellDataFeatures<InputAction, ArrayList<String>>, 
				ObservableValue<ArrayList<String>>>() {
			@Override
			public ObservableValue<ArrayList<String>> call(CellDataFeatures<InputAction, ArrayList<String>> param) {
				
				ArrayList<Object> inputs = inputBindings.getBindingsMap().get(param.getValue());
				
				ArrayList<String> cellValue = new ArrayList<String>();
				
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
				
				return new ReadOnlyObjectWrapper<ArrayList<String>>(cellValue);
				
			}
		});
		
		ObservableList<ArrayList<String>> columnData = FXCollections.observableArrayList();
		
		for(InputAction action: controlTable.getItems()) {
			columnData.add(bindingColumn.getCellObservableValue(action).getValue());
		}
		
		bindingColumn.setCellFactory(new Callback<TableColumn<InputAction, ArrayList<String>>, 
				TableCell<InputAction, ArrayList<String>>>() {
			@Override
			public TableCell<InputAction, ArrayList<String>> call(TableColumn<InputAction, ArrayList<String>> param) {
				
				return new TableCell<InputAction, ArrayList<String>>() {
					
					@Override
					public void updateIndex(int i) {
						super.updateIndex(i);
						
						ArrayList<String> item = param.getCellData(i);
						
						HBox container = new HBox();
						
						ComboBox<String> comboBox = new ComboBox<String>();
						
						Button btnAdd = new Button("Add");
						Button btnRemove = new Button("Remove");
						
						if(item != null) {
							comboBox.getItems().addAll(
//									FXCollections.observableArrayList(
											item
//											)
									);
							comboBox.getSelectionModel().select(0);
						}
						
						InputAction action = controlTable.getItems().get(i);
						
						btnAdd.setOnAction(e -> {
							// TODO: Maybe open some sort of dialog.
							
//							Alert inputAlert = new Alert(AlertType.);
							
							inputBindings.put(action, null);
							comboBox.getItems().add(null);
							
						});
						
						btnRemove.setOnAction(e -> {
							
							if(comboBox.getItems().size() > 0) {
								
								int selectedInputIndex = comboBox.getSelectionModel().getSelectedIndex();
								
								inputBindings.getBindingsMap().get(action).remove(selectedInputIndex);
								comboBox.getItems().remove(selectedInputIndex);
								comboBox.getSelectionModel().select(0);
								
							}
							
						});
						
						container.getChildren().addAll(comboBox, btnAdd, btnRemove);
						
						setGraphic(container);
						
					}
					
				};
			}
		});
//		Alert inputAlert = new Alert(AlertType.);
		btnOptions.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(OptionState.class)));
		btnMainMenu.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(MenuState.class)));
		
	}
	
}
