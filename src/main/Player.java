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
	
	public Player(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
		
		try {
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream()); 
			objectOutputStream.flush();
			objectInputStream = new ObjectInputStream(socket.getInputStream());
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public void run() {
		while(true) {
			try {
				PlayerInfo playerInfo = (PlayerInfo) objectInputStream.readObject();
				server.sendPlayerInfo(playerInfo, this);
				//System.out.println(playerInfo.username + "'s position: X=" + playerInfo.playerX + " Y=" + playerInfo.playerY);
			} catch (SocketException socketException) {
				System.out.println(socket.getInetAddress() + " has disconnected.");
				server.removePlayer(this);
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
}
