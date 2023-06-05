package main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Server {

	private Vector<Player> playerThreads;
	private Map<Integer, PlayerInfo> allPlayerInfos;
	
	public Server(int port) {
		try {
			System.out.println("Binding to port " + port);
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Bound to port " + port + ". Now waiting for connections.");
			playerThreads = new Vector<Player>();
			allPlayerInfos = new HashMap<Integer, PlayerInfo>();
			
			while(true) {
				Socket socket = serverSocket.accept(); // blocking function
				System.out.println("Connection from: " + socket.getInetAddress());
				Player player = new Player(socket, this);
				addPlayer(player);
				player.start();
			}			
		}
		catch (Exception e) { System.out.println(e.getMessage()); }
	}
	
	public void sendPlayerInfo(PlayerInfo playerInfo, Player sender) {
		pAddPlayer(playerInfo);
		for(Player reciever : playerThreads) {
			if(reciever != sender) {
				reciever.sendToClient(playerInfo);
			}
		}
	}
	public void removePlayer(Player player) {
		playerThreads.remove(player);
	}
	
	public void addPlayer(Player player) {
		playerThreads.add(player);
	}
	
	public void pAddPlayer(PlayerInfo player) {
		allPlayerInfos.put(player.getId(), player); //O(1)
	}
	
	public void pRemovePlayer(PlayerInfo player) {
		allPlayerInfos.remove(player.getId()); //O(1)
	}
	public Map<Integer, PlayerInfo> getAllPlayerInfos(){
		return allPlayerInfos;
	}
	
}

