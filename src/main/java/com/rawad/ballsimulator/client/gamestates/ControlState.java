package com.rawad.ballsimulator.client.gamestates;

import com.rawad.ballsimulator.client.Client;
import com.rawad.ballsimulator.client.gui.GuiRegister;
import com.rawad.ballsimulator.client.input.InputAction;
import com.rawad.ballsimulator.client.input.InputBindings;
import com.rawad.ballsimulator.fileparser.ControlsFileParser;
import com.rawad.ballsimulator.loader.Loader;
import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.client.gamestates.StateChangeRequest;
import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.game.Game;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
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
	
	private Loader loader;
	private ControlsFileParser parser;
	
	private InputBindings inputBindings;
	
	@Override
	public void init(StateManager sm, Game game) {
		super.init(sm, game);
		
		loader = game.getProxies().get(Client.class).getLoaders().get(Loader.class);
		parser = game.getProxies().get(Client.class).getFileParsers().get(ControlsFileParser.class);
		
	}
	
	@Override
	public void initGui() {
		
		GuiRegister.loadGui(this);
		
		Client client = game.getProxies().get(Client.class);
		
		inputBindings = client.getInputBindings();
		
		columnAction.setCellValueFactory(new Callback<CellDataFeatures<InputAction, String>, 
				ObservableValue<String>>() {
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
		
		columnInput.setCellFactory(new Callback<TableColumn<InputAction, InputAction>, 
				TableCell<InputAction, InputAction>>() {
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
							button.setOnAction(createInputChangeHandler(item));
							setGraphic(button);
						}
						
					}
					
				};
			}
		});
		
		refreshTable();
		
		btnOptions.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(OptionState.class)));
		btnMainMenu.setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(MenuState.class)));
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		Loader.addTask(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				controlsTable.setVisible(false);
				
				loader.loadInputBindings(parser, inputBindings, "inputs");
				
				controlsTable.setVisible(true);
				
				return null;
				
			}
		});
		
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		
		Loader.addTask(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				loader.saveInputBindings(parser, inputBindings, "inputs");
				
				return null;
				
			}
		});
		
	}
	
	private final EventHandler<ActionEvent> createInputChangeHandler(InputAction action) {
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
				
				refreshTable();
				
				cancelButton.fire();
				
				keyEvent.consume();
				
			});
			
			alertDialog.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
				
				inputBindings.put(action, mouseEvent.getButton());
				
				refreshTable();
				
				cancelButton.fire();
				
				mouseEvent.consume();
				
			});
			
			alertDialog.requestFocus();
			inputAlert.showAndWait();
			
		};
	}
	
	private final void refreshTable() {
		
		controlsTable.getItems().clear();
		
		for(InputAction action: InputAction.values()) {
			if(action == InputAction.DEFAULT) continue;
			
			controlsTable.getItems().add(action);
			
		}
		
	}
	
}
