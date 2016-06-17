package com.rawad.ballsimulator.networking.client.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.ballsimulator.networking.APacket;
import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.ballsimulator.networking.server.udp.SPacket02Move;
import com.rawad.ballsimulator.networking.server.udp.SPacket03Ping;
import com.rawad.ballsimulator.server.Server;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

public class ClientDatagramManager {
	
	private ClientNetworkManager networkManager;
	
	private DatagramSocket socket;
	
	private Thread packetReceiver;
	
	public ClientDatagramManager(ClientNetworkManager networkManager) {
		this.networkManager = networkManager;
	}
	
	public void start() {
		
		try {
			
			socket = new DatagramSocket(networkManager.getConnectionManager().getSocket().getLocalPort());
			// It's better to leave it the same port, it also needs to be for the server to work right.
			
			packetReceiver = new Thread(new PacketReceiver(socket), "Datagram Manager");
			packetReceiver.start();
			
		} catch(Exception ex) {
			Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; Datagram socket couldn't be bound on client's "
					+ "side.");
		}
		
	}
	
	public void stop() {
		Util.silentClose(socket);// socket is null sometimes...
		socket = null;
	}
	
	private void handlePacketData(byte[] data) {
		
		String dataAsString = APacket.getStringFromData(data);
		
		UDPPacketType type = UDPPacket.getPacketTypeFromData(dataAsString);
		
		Entity e = networkManager.getClient().getEntityById(UDPPacket.getEntityIdFromString(dataAsString));
		
		switch(type) {
		
		case DAMAGE:
			break;
			
		case MOVE:
			
			SPacket02Move moveReply = new SPacket02Move(dataAsString);
			
			TransformComponent transformComp = e.getComponent(TransformComponent.class);
			MovementComponent movementComp = e.getComponent(MovementComponent.class);
			
			transformComp.setX(moveReply.getX());
			transformComp.setY(moveReply.getY());
			
			transformComp.setTheta(moveReply.getTheta());
			
			movementComp.setAx(moveReply.getAx());
			movementComp.setAy(moveReply.getAy());
			
			movementComp.setVx(moveReply.getVx());
			movementComp.setVy(moveReply.getVy());
			
			break;
		
		case PING:
			
			SPacket03Ping pingPacket = new SPacket03Ping(dataAsString);
			
			if(pingPacket.isRequest()) {
				
				NetworkComponent networkComp = e.getComponent(NetworkComponent.class);
				UserComponent userComp = e.getComponent(UserComponent.class);
				
				sendPacket(new CPacket03Ping(networkComp, userComp, pingPacket.getTimeStamp()), 
						networkManager.getConnectionManager().getServerAddress(), Server.PORT);
				
			} else {
				
				e.getComponent(UserComponent.class).setPing(pingPacket.getPing());
				
			}
			
			break;
			
		case INVALID:
		default:
			Logger.log(Logger.WARNING, "Invalid packet: \"" + dataAsString + "\".");
			break;
		
		}
		
	}
	
	public void sendPacket(UDPPacket packet, String address, int port) {
		
		if(networkManager.isLoggedIn() && !socket.isClosed()) {
			
			try {
				
				byte[] data = packet.getData();
				
				DatagramPacket dataPacket = new DatagramPacket(data, data.length, InetAddress.getByName(address), 
						port);
				
				socket.send(dataPacket);
				
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; Couldn't send packet from client.");
			}
			
		}
		
	}
	
	private class PacketReceiver implements Runnable {
		
		private DatagramSocket socket;
		
		public PacketReceiver(DatagramSocket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			
			try {
				
				while(networkManager.isConnectedToServer()) {
					
					if(networkManager.isLoggedIn()) {
						
						byte[] dataBuffer = new byte[APacket.BUFFER_SIZE];
						
						DatagramPacket packetReceived = new DatagramPacket(dataBuffer, dataBuffer.length);
						
						socket.receive(packetReceived);
						
						handlePacketData(dataBuffer);
						
					}
					
				}
				
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, ex.getMessage() + "; Couldn't receive packet from server or "
						+ "socket was closed.");
			}
			
		}
		
	}
	
}
