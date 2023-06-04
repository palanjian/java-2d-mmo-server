package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Player extends Thread {
	
	private Socket socket;	
	private Server server;
	
	//printwriter test
	InputStream inputStream;
	ObjectInputStream objectInputStream;
	
	public Player(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
		
		//printwriter test
		try {
			inputStream = socket.getInputStream();
			objectInputStream = new ObjectInputStream(inputStream);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void run() {
		/*
		 * TEST CODE
		 * 
		 */	
		while(true) {
			String line;
			try {
				PlayerInfo playerInfo = (PlayerInfo) objectInputStream.readObject();
				System.out.println(playerInfo.username + "'s position: X=" + playerInfo.playerX + " Y=" + playerInfo.playerY);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		/*
		 * TEST CODE
		 * 
		 */	
	}
}
