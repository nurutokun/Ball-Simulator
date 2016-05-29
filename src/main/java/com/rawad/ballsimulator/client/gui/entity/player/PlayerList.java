package com.rawad.ballsimulator.client.gui.entity.player;

import com.rawad.ballsimulator.server.entity.NetworkComponent;
import com.rawad.gamehelpers.game.entity.Entity;
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
public class PlayerList extends TableView<Entity> {
	
	@FXML private TableColumn<Entity, String> usernameColumn;
	@FXML private TableColumn<Entity, String> infoColumn;
	@FXML private TableColumn<Entity, Integer> pingColumn;
	
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
		
		usernameColumn.setCellValueFactory(new Callback<CellDataFeatures<Entity, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Entity, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().toString());
				// TODO: (PlayerList) Make a name component.
			}
		});
		
		infoColumn.setCellValueFactory(new Callback<CellDataFeatures<Entity, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Entity, String> p) {
				
				String ip = "";
				
				NetworkComponent networkComp = p.getValue().getComponent(NetworkComponent.class);
				
				if(networkComp != null) {
					ip = networkComp.getIp();
				}
				
				return new ReadOnlyObjectWrapper<String>(ip);
			}
		});
		
		pingColumn.setCellValueFactory(new Callback<CellDataFeatures<Entity, Integer>, ObservableValue<Integer>>() {
			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Entity, Integer> p) {
				
				int ping = -1;
				
				NetworkComponent networkComp = p.getValue().getComponent(NetworkComponent.class);
				
				if(networkComp != null) {
					ping = networkComp.getPing();
				}
				
				return new ReadOnlyObjectWrapper<Integer>(ping);
				
			}
		});
		
	}
	
}
