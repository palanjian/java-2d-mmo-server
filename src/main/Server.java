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

import static enums.EntityType.PLAYER;

public class Server {

	private Vector<Player> playerThreads;

	//on the client, we treat all entities the same, as all we need to do
	//is render them. however, on the server, we store players in their own container
	//as we may need quick lookup of explicitly players for things like combat
	private Map<Integer, EntityInfo> allEntityInfos;
	private Map<Integer, EntityInfo> allPlayerInfos;

	private TileMap tileMap;
	private String DEFAULT_MAP = "/maps/island.txt";
	
	public Server(int port) {
		try {
			System.out.println("Binding to port " + port);
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Bound to port " + port + ". Now waiting for connections.");
			playerThreads = new Vector<Player>();
			allPlayerInfos = new HashMap<Integer, EntityInfo>();
			allEntityInfos = new HashMap<Integer, EntityInfo>();

			//Sets default tileMap
			InputStream is = getClass().getResourceAsStream(DEFAULT_MAP);
			tileMap = new TileMap(is);
			
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

		for(Player reciever : playerThreads) {
			if(reciever != sender) {
				reciever.sendToClient(entityInfo);
			}
		}
	}

	public void sendTileMap(TileMap tileMap) {
		System.out.println("Sending tilemap to all players.");
		this.tileMap = tileMap;
		for(Player reciever : playerThreads) {
			reciever.sendTileMap(tileMap);
		}
	}
	
	public void sendChatMessage(ChatMessage chatMessage) {	
		for(Player reciever : playerThreads) {
			reciever.sendChatMessage(chatMessage);
		}
		System.out.println(chatMessage.getSender() + " says: " + chatMessage.getMessage());
	}
	
	public void removePlayerThread(Player player) {
		playerThreads.remove(player);
	}
	
	public void addPlayerThread(Player player) {
		playerThreads.add(player);
	}
	
	public void addPlayerInfo(EntityInfo player) {
		allPlayerInfos.put(player.getId(), player); //O(1)
	}
	
	public void removePlayerInfo(EntityInfo player) {
		allPlayerInfos.remove(player.getId()); //O(1)
	}
	
	public Map<Integer, EntityInfo> getAllPlayerInfos(){
		return allPlayerInfos;
	}
	
	public TileMap getTileMap() { return tileMap; }

	public void addEntityInfo(EntityInfo entity) {
		allEntityInfos.put(entity.getId(), entity); //O(1)
	}
	public void removeEntityInfo(EntityInfo entity) { allEntityInfos.remove(entity.getId()); }

	public Map<Integer, EntityInfo> getAllEntityInfos(){
		return allEntityInfos;
	}

	public EntityInfo getEntityById(int id) {
		return allEntityInfos.get(id);
	}
}

