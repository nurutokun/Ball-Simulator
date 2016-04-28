package com.rawad.ballsimulator.server.entity;

import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.world.World;

public class EntityPlayerMP extends EntityPlayer {
	
	private final String address;
	
	public EntityPlayerMP(World world, String username, String address) {
		super(world);
		
		this.name = username;
		this.address = address;
		
	}
	
	/**
	 * Address which the player is located on.
	 * 
	 * @return
	 */
	public String getAddress() {
		return address;
	}
	
}
