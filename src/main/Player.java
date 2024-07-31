package main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import packets.ChatMessage;
import packets.EntityInfo;
import packets.TileMap;
import worlds.World;

import javax.swing.text.html.parser.Entity;

import static enums.EntityType.PLAYER;

public class Player extends Thread {
	
	private Socket socket;	
	private Server server;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private EntityInfo playerInfo;
	private World world;

	private boolean firstLogin = true; //flag
	
	public Player(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
		this.world = Server.DEFAULT_WORLD;

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
		world.addPlayerThread(this);

		while(true) {
			try {
				Object o = objectInputStream.readObject();
				if (o instanceof EntityInfo p && p.getType() == PLAYER) {

					playerInfo = p;

					//sends join message using firstLogin flag
					if(firstLogin) {
						sendJoinMessage();
						firstLogin = false;
					}
					world.addPlayerId(playerInfo.getId());
					server.sendEntityInfo(playerInfo, this);
					//System.out.println(playerInfo.getId() + "'s position: X=" + playerInfo.getPlayerX() + " Y=" + playerInfo.getPlayerY() + " DIR=" + playerInfo.getDirection());

				}
				else if(o instanceof EntityInfo entityInfo){
					world.addEntityId(playerInfo.getId());
					server.sendEntityInfo(entityInfo, this);
				}

				else if (o instanceof ChatMessage chatMessage) {
					server.sendChatMessage(chatMessage, world);
				}

			} catch (SocketException socketException) {
				System.out.println(socket.getInetAddress() + " has disconnected.");
				//tells all clients that the player has disconnected
				
				server.removePlayerThread(this); //removes from list of threads
				server.removePlayerInfo(playerInfo); //removes from list of all playerinfos
				world.removePlayerId(playerInfo.getId());

				//removes players pet (if they have one)
				EntityInfo petInfo = server.getEntityById(playerInfo.getId() + 5000);
				if(petInfo != null){
					petInfo.setOnline(false);
					server.sendEntityInfo(petInfo, this);
					server.removeEntityInfo(petInfo);
					world.removeEntityId(petInfo.getId());
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
		server.sendChatMessage(message, world);
	}
	
	private void sendLeaveMessage() {
		String string = playerInfo.getUsername() + " has left the server.";
		ChatMessage message = new ChatMessage(string, "Server");
		server.sendChatMessage(message, world);
	}

	public void sendToClient(Object o) {
		try {
			objectOutputStream.writeUnshared(o);
			objectOutputStream.flush();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void initializePlayerInfos(){
		for(Integer playerId : world.getPlayersInWorld()) {
			//adds the sprite sheet back, then removes. We don't need to keep sending over the spritesheet
			//(waste of bytes)
			EntityInfo entityInfo = server.getPlayerById(playerId);

			if(entityInfo != null){
				entityInfo.setSpritesheet(server.getSpriteSheetById(entityInfo.getId()));

				sendToClient(entityInfo);
				entityInfo.setSpritesheet(null);
			}
		}
	}

	public void initializeEntityInfos(){
		for(Integer entityId : world.getEntitiesInWorld()) {
			EntityInfo entityInfo = server.getEntityById(entityId);
			if(entityInfo != null){
				entityInfo.setSpritesheet(server.getSpriteSheetById(entityInfo.getId()));
				sendToClient(entityInfo);
				entityInfo.setSpritesheet(null);
			}
		}
	}

	public void initializeTileMap(){
		sendToClient(world.getTileMap());
	}
	public World getWorld() { return world; }
}
