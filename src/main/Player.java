package main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import packets.ChatMessage;
import packets.EntityInfo;
import packets.TileMap;
import worlds.TeleportPoint;
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
		initializeTileMap();
		initializeEntityInfos();
		initializePlayerInfos();

		world.addPlayerThread(this);

		while(true) {
			try {
				Object o = objectInputStream.readObject();
				if (o instanceof EntityInfo p && p.getType() == PLAYER) {

					playerInfo = p;

					//sends join message using firstLogin flag
					if(firstLogin) {
						sendJoinMessage();
						world.addPlayerId(playerInfo.getId());
						firstLogin = false;
					}
					checkForTeleport();

					server.sendEntityInfo(playerInfo, this);
					//System.out.println(playerInfo.getId() + "'s position: X=" + playerInfo.getPlayerX() + " Y=" + playerInfo.getPlayerY() + " DIR=" + playerInfo.getDirection());

				}
				else if(o instanceof EntityInfo entityInfo){
					world.addEntityId(entityInfo.getId());
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
			entityInfo.setSpritesheet(server.getSpriteSheetById(playerId));
			sendToClient(entityInfo);
			entityInfo.setSpritesheet(null);

		}
	}

	public void initializeEntityInfos(){
		for(Integer entityId : world.getEntitiesInWorld()) {
			EntityInfo entityInfo = server.getEntityById(entityId);
			if(entityInfo != null){
				entityInfo.setSpritesheet(server.getSpriteSheetById(entityId));
				sendToClient(entityInfo);
				entityInfo.setSpritesheet(null);
				System.out.println("SENT INFO FOR ID=" + entityId);
			}
			else System.out.println("ENTITYINFO IS NULL");
		}
	}

	public void initializeTileMap(){
		sendToClient(world.getTileMap());
	}


	//idk if this function is best suited for the player class
	public void checkForTeleport(){
		int size = 64;
		//System.out.println(playerInfo.getWorldX() / size + " " + playerInfo.getWorldY() / size);
		TeleportPoint point = world.getTeleportPoint(playerInfo.getWorldX() / size, playerInfo.getWorldY() / size);
		if(point != null){
			teleportPlayer(point.destinationWorld, point.destinationX*size, point.destinationY*size);
		}
	}

	public void teleportPlayer(String newWorldName, int newX, int newY){
		//remove him from world
		world.removePlayerId(playerInfo.getId());
		world.removePlayerThread(this);
		//send to all players that he's removed

		playerInfo.setOnline(false);
		server.sendEntityInfo(playerInfo, this);
		playerInfo.setOnline(true);

		//removes players pet (if they have one)
		EntityInfo petInfo = server.getEntityById(playerInfo.getId() + 5000);
		if(petInfo != null) {
			petInfo.setOnline(false);
			server.sendEntityInfo(petInfo, this);
			petInfo.setOnline(true);
			petInfo.setWorldX(newX);
			petInfo.setWorldY(newY);
			world.removeEntityId(petInfo.getId());
		}
		//add him to new world
		world = server.getWorld(newWorldName);
		world.addPlayerThread(this);
		world.addPlayerId(playerInfo.getId());

		//add the new info
		playerInfo.setWorldX(newX);
		playerInfo.setWorldY(newY);

		//initialize all the shit
		initializeTileMap();
		initializePlayerInfos();
		initializeEntityInfos();

		sendToClient(playerInfo);
		if(petInfo != null) sendToClient(petInfo);

	}
	public World getWorld() { return world; }
}
