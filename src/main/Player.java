package main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import packets.ChatMessage;
import packets.EntityInfo;
import packets.TileMap;

import static enums.EntityType.PLAYER;

public class Player extends Thread {
	
	private Socket socket;	
	private Server server;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	EntityInfo playerInfo;
	
	private boolean firstLogin = true; //flag
	
	public Player(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
		
		try {
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream()); 
			objectInputStream = new ObjectInputStream(socket.getInputStream());
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public void run() {
		//Begins by sending the prexisting information of all other players to the client
		initializePlayerInfos();
		initializeEntityInfos();
		initializeTileMap();

		while(true) {
			try {
				Object o = objectInputStream.readObject();
				if (o instanceof EntityInfo p && p.getType() == PLAYER) {
					playerInfo = p;
					server.sendEntityInfo(playerInfo, this);
					//System.out.println(playerInfo.getId() + "'s position: X=" + playerInfo.getPlayerX() + " Y=" + playerInfo.getPlayerY() + " DIR=" + playerInfo.getDirection());
					
					//sends join message using firstLogin flag
					if(firstLogin) {
						sendJoinMessage();
						firstLogin = false;
					}
				}
				else if(o instanceof EntityInfo entityInfo){
					server.sendEntityInfo(entityInfo, this);
				}

				else if (o instanceof ChatMessage chatMessage) {
					server.sendChatMessage(chatMessage);
				}

			} catch (SocketException socketException) {
				System.out.println(socket.getInetAddress() + " has disconnected.");
				//tells all clients that the player has disconnected
				
				server.removePlayerThread(this); //removes from list of threads
				server.removePlayerInfo(playerInfo); //removes from list of all playerinfos

				//removes players pet (if they have one)
				EntityInfo petInfo = server.getEntityById(playerInfo.getId() + 5000);
				if(petInfo != null){
					petInfo.setOnline(false);
					server.sendEntityInfo(petInfo, this);
					server.removeEntityInfo(petInfo);
				}

				playerInfo.setOnline(false);
				server.sendEntityInfo(playerInfo, this);
				server.removePlayerInfo(playerInfo);
				sendLeaveMessage();
				return;
				
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	private void sendJoinMessage() {
		String string = playerInfo.getUsername() + " has joined the server.";
		ChatMessage message = new ChatMessage(string, "Server");
		server.sendChatMessage(message);
	}
	
	private void sendLeaveMessage() {
		String string = playerInfo.getUsername() + " has left the server.";
		ChatMessage message = new ChatMessage(string, "Server");
		server.sendChatMessage(message);
	}

	public void sendToClient(Object o) {
		try {
			objectOutputStream.writeUnshared(o);
			objectOutputStream.flush();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void initializePlayerInfos(){
		for(EntityInfo playerInfo : server.getAllPlayerInfos().values()) {
			sendToClient(playerInfo);
		}
	}

	public void initializeEntityInfos(){
		for(EntityInfo entityInfo : server.getAllEntityInfos().values()) {
			sendToClient(entityInfo);
		}
		sendTileMap(server.getTileMap());
	}
	public void initializeTileMap(){
		sendTileMap(server.getTileMap());
	}

	public void sendTileMap(TileMap tileMap) {
		try {
			objectOutputStream.writeUnshared(tileMap);
			objectOutputStream.flush();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void sendChatMessage(ChatMessage chatMessage) {
		try {
			objectOutputStream.writeUnshared(chatMessage);
			objectOutputStream.flush();
		} catch (Exception e) { e.printStackTrace(); }
	}
}
