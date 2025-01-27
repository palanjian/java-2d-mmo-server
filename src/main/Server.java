package main;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import packets.ChatMessage;
import packets.EntityInfo;
import packets.TileMap;
import worlds.TempWorldHandler;
import worlds.World;

import static enums.EntityType.PLAYER;

public class Server {

	private Vector<Player> playerThreads;

	//on the client, we treat all entities the same, as all we need to do
	//is render them. however, on the server, we store players in their own container
	//as we may need quick lookup of explicitly players for things like combat
	private Map<Integer, EntityInfo> allEntityInfos;
	private Map<Integer, EntityInfo> allPlayerInfos;
	private Map<Integer, byte[]> allSpriteSheets;

	private Map<String, World> allWorlds;
	public static World DEFAULT_WORLD;

	public Server(int port) {
		try {
			System.out.println("Binding to port " + port);
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Bound to port " + port + ". Now waiting for connections.");
			playerThreads = new Vector<Player>();
			allPlayerInfos = new HashMap<Integer, EntityInfo>();
			allEntityInfos = new HashMap<Integer, EntityInfo>();
			allSpriteSheets = new HashMap<>();

			allWorlds = TempWorldHandler.initializeAllWorlds();
			DEFAULT_WORLD = allWorlds.get(TempWorldHandler.getDefaultWorldName());

			CommandHandler commandHandler = new CommandHandler(this);
			commandHandler.start();
			
			while(true) {
				Socket socket = serverSocket.accept(); // blocking function
				System.out.println("Connection from: " + socket.getInetAddress());
				Player player = new Player(socket, this);
				addPlayerThread(player);
				player.start();
			}			
		}
		catch (Exception e) { System.out.println(e.getMessage()); }
	}
	
	public void sendEntityInfo(EntityInfo entityInfo, Player sender) {
		if(entityInfo.getType() == PLAYER) addPlayerInfo(entityInfo);
		else addEntityInfo(entityInfo);

		if(entityInfo.getSpritesheet() != null) allSpriteSheets.put(entityInfo.getId(), entityInfo.getSpritesheet());

		World world = sender.getWorld();
		for(Player reciever : world.getPlayerThreads()) {
			if(reciever != sender) {
				entityInfo.setSpritesheet(allSpriteSheets.get(entityInfo.getId()));
				reciever.sendToClient(entityInfo);
			}
		}
	}

	public void sendChatMessage(ChatMessage chatMessage, World world) {
		for(Player reciever : world.getPlayerThreads()) {
			reciever.sendToClient(chatMessage);
		}
		System.out.println(chatMessage.getSender() + " says: " + chatMessage.getMessage());
	}
	public void sendTileMap(TileMap tileMap, World world) {
		System.out.println("Sending tilemap to players in world: " + world.getName());
		world.setTileMap(tileMap);
		for(Player reciever : world.getPlayerThreads()) {
			reciever.sendToClient(tileMap);
		}
	}

	public void addPlayerThread(Player player) {
		player.getWorld().addPlayerThread(player);
		playerThreads.add(player);
	}

	public void removePlayerThread(Player player) {
		player.getWorld().removePlayerThread(player);
		playerThreads.remove(player);
	}

	public void addPlayerInfo(EntityInfo player) { allPlayerInfos.put(player.getId(), player); }
	
	public void removePlayerInfo(EntityInfo player) {
		allPlayerInfos.remove(player.getId()); //O(1)
	}

	public void addEntityInfo(EntityInfo entity) {
		allEntityInfos.put(entity.getId(), entity); //O(1)
	}
	public void removeEntityInfo(EntityInfo entity) { allEntityInfos.remove(entity.getId()); }


	public EntityInfo getEntityById(int id) {
		return allEntityInfos.get(id);
	}
	public EntityInfo getPlayerById(int id) {
		return allPlayerInfos.get(id);
	}
	public byte[] getSpriteSheetById(int id){ return allSpriteSheets.get(id); }
	public World getWorld(String worldName) { return allWorlds.get(worldName); }
}

