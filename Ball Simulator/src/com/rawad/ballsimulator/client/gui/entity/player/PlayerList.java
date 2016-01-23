package com.rawad.ballsimulator.client.gui.entity.player;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.rawad.ballsimulator.entity.EntityPlayer;

/**
 * Need to implement better {@code String} rendering to allow us to get a proper width/height of the newly rendered 
 * {@code String}.
 * 
 * @author Rawad
 *
 */
public class PlayerList extends JTable {// TODO: Could Make JList
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5609701213952163115L;
	
	private final String id;

	private DefaultTableModel model;
	
	private ArrayList<EntityPlayer> players;
	
	public PlayerList(String id) {
		super();
		
		this.id = "Player List";
		
		model = new DefaultTableModel(1, 1);
		model.setColumnIdentifiers(new String[]{"Player"});
		
		players = new ArrayList<EntityPlayer>();
		
		
	}
	
	public String getId() {
		return id;
	}
	
	public void addPlayer(EntityPlayer player) {
		
		model.addRow(new String[]{player.getName()});
		
		players.add(model.getRowCount() - 1, player);// -1 to go from length -> index
		
	}
	
	public void removePlayer(EntityPlayer player) {
		
		model.removeRow(players.indexOf(player));
		
		players.remove(player);
	}
	
}
