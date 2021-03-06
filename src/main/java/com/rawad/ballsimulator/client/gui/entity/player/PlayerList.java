package com.rawad.ballsimulator.client.gui.entity.player;

import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.jfxengine.loader.GuiLoader;

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
		
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load(GuiLoader.streamLayoutFile(getClass()));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		usernameColumn.setCellValueFactory(new Callback<CellDataFeatures<Entity, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Entity, String> p) {
				
				String username = "";
				
				UserComponent userComp = p.getValue().getComponent(UserComponent.class);
				
				if(userComp != null) {
					username = userComp.getUsername();
				}
				
				return new ReadOnlyObjectWrapper<String>(username);
				
			}
		});
		
		infoColumn.setCellValueFactory(new Callback<CellDataFeatures<Entity, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Entity, String> p) {
				
				String ip = "";
				
				UserComponent userComp = p.getValue().getComponent(UserComponent.class);
				
				if(userComp != null) {
					ip = userComp.getIp();
				}
				
				return new ReadOnlyObjectWrapper<String>(ip);
			}
		});
		
		pingColumn.setCellValueFactory(new Callback<CellDataFeatures<Entity, Integer>, ObservableValue<Integer>>() {
			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Entity, Integer> p) {
				
				int ping = -1;
				
				UserComponent userComp = p.getValue().getComponent(UserComponent.class);
				
				if(userComp != null) {
					ping = userComp.getPing();
				}
				
				return new ReadOnlyObjectWrapper<Integer>(ping);
				
			}
		});
		
	}
	
}
