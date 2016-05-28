package com.rawad.ballsimulator.server.entity;

import com.rawad.gamehelpers.game.entity.Component;

public class NetworkComponent extends Component {
	
	private String ip = "localhost";
	
	private int ping = 0;
	
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
	 * @return the ping
	 */
	public int getPing() {
		return ping;
	}
	
	/**
	 * @param ping the ping to set
	 */
	public void setPing(int ping) {
		this.ping = ping;
	}

	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof NetworkComponent) {
			
			NetworkComponent networkComp = (NetworkComponent) comp;
			
			networkComp.setPing(getPing());
			
			return networkComp;
			
		}
		
		return comp;
		
	}

}
