package com.rawad.ballsimulator.server.entity;

import com.rawad.gamehelpers.game.entity.Component;

public class NetworkComponent extends Component {
	
	private int id = -1;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof NetworkComponent) {
			
			NetworkComponent networkComp = (NetworkComponent) comp;
			
			networkComp.setId(getId());
			
			return networkComp;
			
		}
		
		return comp;
		
	}

}
