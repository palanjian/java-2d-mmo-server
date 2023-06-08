package main;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import packets.PlayerInfo;
import packets.TileMap;

public class Server {

	private Vector<Player> playerThreads;
	private Map<Integer, PlayerInfo> allPlayerInfos;
	private TileMap tileMap;
	String DEFAULT_MAP = "/maps/map01.txt";
	
	public Server(int port) {
		try {
			System.out.println("Binding to port " + port);
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Bound to port " + port + ". Now waiting for connections.");
			playerThreads = new Vector<Player>();
			allPlayerInfos = new HashMap<Integer, PlayerInfo>();
			
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
	
	public void sendPlayerInfo(PlayerInfo playerInfo, Player sender) {
		addPlayerInfo(playerInfo);
		for(Player reciever : playerThreads) {
			if(reciever != sender) {
				reciever.sendToClient(playerInfo);
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
	
	public void removePlayerThread(Player player) {
		playerThreads.remove(player);
	}
	
	public void addPlayerThread(Player player) {
		playerThreads.add(player);
	}
	
	public void addPlayerInfo(PlayerInfo player) {
		allPlayerInfos.put(player.getId(), player); //O(1)
	}
	
	public void removePlayerInfo(PlayerInfo player) {
		allPlayerInfos.remove(player.getId()); //O(1)
	}
	public Map<Integer, PlayerInfo> getAllPlayerInfos(){
		return allPlayerInfos;
	}
	public TileMap getTileMap() { return tileMap; }
	
}

