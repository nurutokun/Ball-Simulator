package com.rawad.ballsimulator.client.gamestates;

import java.util.ArrayList;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateChangeRequest;
import com.rawad.gamehelpers.client.input.InputBindings;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;

public class ControlState extends State {
	
	@FXML private TableView<InputAction> controlTable;
	
	@FXML private TableColumn<InputAction, String> actionColumn;
	@FXML private TableColumn<InputAction, ArrayList<String>> bindingColumn;
	
	@FXML private Button btnOptions;
	@FXML private Button btnMainMenu;
	
	@Override
	public void initGui() {
		
//		Root root = 
		GuiRegister.loadGui(this);
		
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
				
				ArrayList<KeyCode> keyBindings = inputBindings.getKeybindings().get(param.getValue());
				
				ArrayList<MouseButton> mouseBindings = inputBindings.getMouseBindings().get(param.getValue());
				
				ArrayList<String> cellValue = new ArrayList<String>();
				
				if(keyBindings != null) {
					for(KeyCode key: keyBindings) {
						cellValue.add(key.getName());
					}
				}

				if(mouseBindings != null) {
					for(MouseButton button: mouseBindings) {
						cellValue.add(button.name());
					}
				}
				
				return new ReadOnlyObjectWrapper<ArrayList<String>>(cellValue);
				
			}
		});
		
		for(InputAction action: InputAction.values()) {
			if(action == InputAction.DEFAULT) continue;
			controlTable.getItems().add(action);
		}
		
		btnOptions.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(OptionState.class)));
		btnMainMenu.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(MenuState.class)));
		
	}
	
}
