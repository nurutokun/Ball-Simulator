package com.rawad.ballsimulator.networking.server.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.networking.APacket;
import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.client.udp.CPacket02Move;
import com.rawad.ballsimulator.networking.server.ServerNetworkManager;
import com.rawad.ballsimulator.server.Server;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

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
			networkManager.getServer().getGame().requestStop();
			return;
		}
		
		packetReceiver.start();
		
	}
	
	public void stop() {
		Util.silentClose(socket);
	}
	
	private void handlePacket(byte[] data, String address, int port) {
		
		String dataAsString = APacket.getStringFromData(data);
		
		UDPPacketType type = UDPPacket.getPacketTypeFromData(dataAsString);
		
		Entity e = networkManager.getServer().getGame().getWorld().getEntitiesAsList().get(
				UDPPacket.getEntityIdFromString(dataAsString));
		
		switch(type) {
		
		case DAMAGE:
			break;
			
		case MOVE:
			
			CPacket02Move moveRequest = new CPacket02Move(dataAsString);
			
			handleMoveRequest(moveRequest, e);
			
			break;
			
		case INVALID:
		default:
			Logger.log(Logger.WARNING, "Invalid packet: \"" + data + "\".");
			break;
		}
		
	}
	
	private void handleMoveRequest(CPacket02Move moveRequest, MovementComponent movementComp) {
		
		movementComp.setUp(moveRequest.isUp());
		movementComp.setDown(moveRequest.isDown());
		movementComp.setRight(moveRequest.isRight());
		movementComp.setLeft(moveRequest.isLeft());
		
	}
	
	public void sendMoveUpdate(EntityRotatingBase entity) {
		
		sendPacketToAllClients(new SPacket02Move(entity.getName(), entity.getX(), entity.getY(), entity.getVx(), 
				entity.getVy(), entity.getAx(), entity.getAy(), entity.getTheta()));
		
	}
	
	public void sendPacketToAllClients(APacket packet) {
		
		ArrayList<Socket> clients = networkManager.getConnectionManager().getClients();
		
		for(Socket client: clients) {
			
			sendPacket(packet, client.getInetAddress().getHostAddress(), client.getPort());
			
		}
		
	}
	
	public void sendPacket(APacket packet, String address, int port) {
		
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
			try {
				
				while(networkManager.getServer().getGame().isRunning()) {
					
					byte[] dataBuffer = new byte[APacket.BUFFER_SIZE];
					
					DatagramPacket packet = new DatagramPacket(dataBuffer, dataBuffer.length);
					
					socket.receive(packet);
					
					handlePacket(packet.getData(), packet.getAddress().getHostAddress(), packet.getPort());
					
				}
				
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, ex.getMessage() + "; bad packet or client disconnected.");
//				ex.printStackTrace();
			}
			
		}
	}
	
}
