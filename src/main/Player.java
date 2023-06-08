package main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

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
				//System.out.println(playerInfo.getId() + "'s position: X=" + playerInfo.getPlayerX() + " Y=" + playerInfo.getPlayerY() + " DIR=" + playerInfo.getDirection());
			} catch (SocketException socketException) {
				System.out.println(socket.getInetAddress() + " has disconnected.");
				//tells all clients that the player has disconnected
				
				server.removePlayerThread(this); //removes from list of threads
				server.removePlayerInfo(playerInfo); //removes from list of all playerlocations
				
				playerInfo.setOnline(false);
				server.sendPlayerInfo(playerInfo, this);				
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
