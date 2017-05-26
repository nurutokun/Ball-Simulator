package com.rawad.ballsimulator.networking.entity;

import com.rawad.gamehelpers.game.entity.Component;

/**
 * Attaches a user to an {@code Entity}.
 * 
 * @author Rawad
 *
 */
public class UserComponent extends Component {
	
	private String username = "";
	
	private String ip = "localhost";
	
	private int ping = 0;
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
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
		
		if(comp instanceof UserComponent) {
			
			UserComponent userComp = (UserComponent) comp;
			
			userComp.setUsername(getUsername());
			userComp.setIp(getIp());
			userComp.setPing(getPing());
			
			return userComp;
			
		}
		
		return comp;
		
	}
	
}
