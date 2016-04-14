package com.rawad.ballsimulator.networking.client.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.rawad.ballsimulator.entity.EntityPlayer;
import com.rawad.ballsimulator.networking.Packet;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.client.ClientNetworkManager;
import com.rawad.ballsimulator.networking.server.udp.SPacket02Move;
import com.rawad.gamehelpers.log.Logger;

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
		socket.close();
		socket = null;
	}
	
	private void handlePacketData(byte[] data) {
		
		UDPPacketType type = Packet.getUDPPacketTypeFromData(data);
		
		switch(type) {
		
		case DAMAGE:
			break;
			
		case MOVE:
			
			SPacket02Move moveReply = new SPacket02Move(data);
			
			EntityPlayer player = (EntityPlayer) networkManager.getClient().getWorld()
					.getEntityByName(moveReply.getUsername());
			
//			EntityMovingBase entityToMove = (EntityMovingBase) world.getEntityByName(moveReply.getUsername());
			
			/**/
			player.setX(moveReply.getX());
			player.setY(moveReply.getY());
			
			player.setVx(moveReply.getVx());
			player.setVy(moveReply.getVy());
			
			player.setAx(moveReply.getAx());
			player.setAy(moveReply.getAy());
			
			player.updateHitbox();/**/
			
			break;
		
		case INVALID:
		default:
			Logger.log(Logger.WARNING, "Invalid packet: \"" + data + "\".");
			break;
		
		}
		
	}
	
	public void sendPacket(Packet packet, String address, int port) {
		
		if(networkManager.isConnectedToServer() && !socket.isClosed()) {
			
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
					
					byte[] dataBuffer = new byte[Packet.BUFFER_SIZE];
					
					DatagramPacket packetReceived = new DatagramPacket(dataBuffer, dataBuffer.length);
					
					socket.receive(packetReceived);
					
					handlePacketData(dataBuffer);
					
				}
				
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; Couldn't receive packet from server or "
						+ "socket was closed.");
			}
			
		}
		
	}
	
}
