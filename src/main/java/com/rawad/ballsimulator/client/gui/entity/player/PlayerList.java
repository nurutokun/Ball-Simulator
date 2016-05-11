package com.rawad.ballsimulator.client.gui.entity.player;

import com.rawad.ballsimulator.server.entity.EntityPlayerMP;
import com.rawad.gamehelpers.resources.Loader;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

// TODO: Style PlayerList + PlayerInventory (/slots).
public class PlayerList extends TableView<EntityPlayerMP> {

	@FXML private TableColumn<EntityPlayerMP, String> usernameColumn;
	@FXML private TableColumn<EntityPlayerMP, String> infoColumn;
	@FXML private TableColumn<EntityPlayerMP, Integer> pingColumn;
	
	public PlayerList() {
		super();
		
		FXMLLoader loader = new FXMLLoader(Loader.getFxmlLocation(getClass()));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		usernameColumn.setCellValueFactory(new Callback<CellDataFeatures<EntityPlayerMP, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<EntityPlayerMP, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().toString());// TODO: (PlayerList) Make a name component.
			}
		});
		
		infoColumn.setCellValueFactory(new Callback<CellDataFeatures<EntityPlayerMP, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<EntityPlayerMP, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getAddress());
			}
		});
		
		pingColumn.setCellValueFactory(new Callback<CellDataFeatures<EntityPlayerMP, Integer>, ObservableValue<Integer>>() {
			@Override
			public ObservableValue<Integer> call(CellDataFeatures<EntityPlayerMP, Integer> p) {
				return new ReadOnlyObjectWrapper<Integer>(-1);
			}
		});
		
	}
	
}
