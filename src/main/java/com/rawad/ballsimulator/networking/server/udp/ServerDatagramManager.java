package com.rawad.ballsimulator.networking.server.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.rawad.ballsimulator.entity.MovementComponent;
import com.rawad.ballsimulator.game.MovementRequest;
import com.rawad.ballsimulator.game.event.MovementEvent;
import com.rawad.ballsimulator.networking.APacket;
import com.rawad.ballsimulator.networking.UDPPacket;
import com.rawad.ballsimulator.networking.UDPPacketType;
import com.rawad.ballsimulator.networking.client.udp.CPacket02Move;
import com.rawad.ballsimulator.networking.client.udp.CPacket03Ping;
import com.rawad.ballsimulator.networking.entity.NetworkComponent;
import com.rawad.ballsimulator.networking.entity.UserComponent;
import com.rawad.ballsimulator.networking.server.ServerNetworkManager;
import com.rawad.ballsimulator.networking.server.tcp.ServerConnectionManager.ClientInputManager;
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
		
		Entity e = networkManager.getServer().getEntityById(UDPPacket.getEntityIdFromString(dataAsString));
		
		switch(type) {
		
		case DAMAGE:
			break;
			
		case MOVE:
			
			CPacket02Move moveRequest = new CPacket02Move(dataAsString);
			
			if(e.getComponent(MovementComponent.class) != null) handleMoveRequest(moveRequest, e);
			
			break;
			
		case PING:
			
			CPacket03Ping pingResponse = new CPacket03Ping(dataAsString);
			
			NetworkComponent networkComp = e.getComponent(NetworkComponent.class);
			UserComponent userComp = e.getComponent(UserComponent.class);
			
			long curTime = System.nanoTime();
			
			userComp.setPing((int) (TimeUnit.NANOSECONDS.toMillis(curTime - pingResponse.getTimeStamp()) / 2));
			
			sendPacketToAllClients(new SPacket03Ping(networkComp, userComp, 0, false));
			
			break;
			
		case INVALID:
		default:
			Logger.log(Logger.WARNING, "Invalid packet: \"" + data + "\".");
			break;
		}
		
	}
	
	private void handleMoveRequest(CPacket02Move moveRequest, Entity entity) {
		
		networkManager.getServer().getGame().getGameEngine().getEventManager().submitEvent(new MovementEvent(entity, 
				new MovementRequest(moveRequest.isUp(), moveRequest.isDown(), moveRequest.isRight(), moveRequest.isLeft())));
		
	}
	
	public void sendPacketToAllClients(UDPPacket packet) {
		
		ArrayList<ClientInputManager> clients = networkManager.getConnectionManager().getClientInputManagers();
		
		synchronized(clients) {
			for(ClientInputManager cim: clients) {
				
				Socket client = cim.getClient();
				
				sendPacket(packet, client.getInetAddress().getHostAddress(), client.getPort());
				
			}
		}
		
	}
	
	public void sendPacket(UDPPacket packet, String address, int port) {
		
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
					
					if(packet.getData() != null)
						handlePacket(packet.getData(), packet.getAddress().getHostAddress(), packet.getPort());
					
				}
				
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, ex.getMessage() + ".");
			}
			
		}
	}
	
}
