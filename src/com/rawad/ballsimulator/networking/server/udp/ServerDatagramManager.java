package com.rawad.ballsimulator.networking.server.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import com.rawad.ballsimulator.entity.EntityMovingBase;
import com.rawad.ballsimulator.networking.Packet;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.client.udp.CPacket02Move;
import com.rawad.ballsimulator.networking.server.Server;
import com.rawad.ballsimulator.networking.server.ServerNetworkManager;
import com.rawad.ballsimulator.networking.server.entity.EntityPlayerMP;
import com.rawad.gamehelpers.log.Logger;

/**
 * Handles live-updates for live relaynig of information from server to client and back. 
 * 
 * @author Rawad
 *
 */
public class ServerDatagramManager {
	
	private ServerNetworkManager networkManager;
	
	private DatagramSocket socket;
	
	private Thread packetReceiver;
	
	public ServerDatagramManager(ServerNetworkManager networkManager) {
		this.networkManager = networkManager;
		
		packetReceiver = new Thread(new PacketReceiver(), "Packet Receiver");
		
	}
	
	public void start() {
		
		try {
			socket = new DatagramSocket(Server.PORT);
		} catch (SocketException ex) {
			Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; couldn't start DatagramSocket.");
		}
		
		packetReceiver.start();
		
	}
	
	private void handlePacket(byte[] data, String address, int port) {
		
		UDPPacketType type = Packet.getUDPPacketTypeFromData(data);
		
		switch(type) {
		
		case DAMAGE:
			break;
			
		case MOVE:
			
			CPacket02Move moveRequest = new CPacket02Move(data);
			
			EntityPlayerMP player = (EntityPlayerMP) networkManager.getServer().getController().getWorld()
					.getEntityByName(moveRequest.getUsername());
			
			handleMoveRequest(moveRequest, player);
			
//			SPacket02Move moveReply = new SPacket02Move(moveRequest.getUsername(), player.getX(), player.getY(), 
//					player.getVx(), player.getVy(), player.getAx(), player.getAy());
//			sendPacketToAllClients(moveReply);// Doesn't really make sense here unless we want every player to interpolate pos.
			
			break;
			
		case INVALID:
		default:
			Logger.log(Logger.WARNING, "Invalid packet: \"" + data + "\".");
			break;
		}
		
	}
	
	private void handleMoveRequest(CPacket02Move moveRequest, EntityPlayerMP playerToMove) {
		
		if(moveRequest.isUp()) {
			playerToMove.moveUp();
		} else if(moveRequest.isDown()) {
			playerToMove.moveDown();
		}
		
		if(moveRequest.isRight()) {
			playerToMove.moveRight();
		} else if(moveRequest.isLeft()) {
			playerToMove.moveLeft();
		}
		
	}
	
	public void sendMoveUpdate(EntityMovingBase entity) {
		
		sendPacketToAllClients(new SPacket02Move(entity.getName(), entity.getX(), entity.getY(), entity.getVx(), 
				entity.getVy(), entity.getAx(), entity.getAy()));
		
	}
	
	public void sendPacketToAllClients(Packet packet) {
		
		Socket[] clients = networkManager.getConnectionManager().getClients();
		
		for(Socket client: clients) {
			
			sendPacket(packet, client.getInetAddress().getHostAddress(), client.getPort());
			
		}
		
	}
	
	public void sendPacket(Packet packet, String address, int port) {
		
		try {
			
			byte[] data = packet.getData();
			
			DatagramPacket dataPacket = new DatagramPacket(data, data.length, InetAddress.getByName(address), port);
			
			socket.send(dataPacket);
			
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; couldn't send packet from server.");
		}
		
	}
	
	private class PacketReceiver implements Runnable {
		
		@Override
		public void run() {
			while(networkManager.getServer().getGame().isRunning()) {
				
				try {
					
					byte[] dataBuffer = new byte[Packet.BUFFER_SIZE];
					
					DatagramPacket packet = new DatagramPacket(dataBuffer, dataBuffer.length);
					
					socket.receive(packet);
					
					handlePacket(packet.getData(), packet.getAddress().getHostAddress(), packet.getPort());
					
				} catch (IOException ex) {
					Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; couldn't read packet.");
				}
				
			}
		}
		
	}
	
}
