package main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

	private Vector<Player> playerThreads;
	
	public Server(int port) {
		try {
			System.out.println("Binding to port " + port);
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Bound to port " + port + ". Now waiting for connections.");
			playerThreads = new Vector<Player>();
			
			while(true) {
				Socket socket = serverSocket.accept(); // blocking function
				System.out.println("Connection from: " + socket.getInetAddress());
				Player player = new Player(socket, this);
				playerThreads.add(player);
				player.start();
			}			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void sendPlayerInfo(PlayerInfo playerInfo, Player sender) {
		for(Player reciever : playerThreads) {
			if(reciever != sender) {
				reciever.sendToClient(playerInfo);
			}
		}
	}
	public void removePlayer(Player player) {
		playerThreads.remove(player);
	}
}

