package main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

public class Player extends Thread {
	
	private Socket socket;	
	private Server server;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	PlayerInfo playerInfo;
	
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
		//begins by sending the prexisting information of all other players to the client
		initializePlayerInfos();
		
		while(true) {
			try {
				playerInfo = (PlayerInfo) objectInputStream.readObject();
				server.sendPlayerInfo(playerInfo, this);
				//System.out.println(playerInfo.username + "'s position: X=" + playerInfo.playerX + " Y=" + playerInfo.playerY);
			} catch (SocketException socketException) {
				System.out.println(socket.getInetAddress() + " has disconnected.");
				server.removePlayer(this); //removes from list of threads
				server.pRemovePlayer(playerInfo); //removes from list of all playerlocations
				return;
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public void sendToClient(PlayerInfo playerInfo) {
		try {
			objectOutputStream.writeUnshared(playerInfo);
			objectOutputStream.flush();
		} catch (Exception e) { e.printStackTrace(); }
	}	
	
	public void initializePlayerInfos(){
		for(PlayerInfo playerInfo : server.getAllPlayerInfos().values()) {
			sendToClient(playerInfo);
		}
	}
}
