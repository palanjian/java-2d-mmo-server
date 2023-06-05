package main;

import java.io.Serializable;

public class PlayerInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public int playerX;
	public int playerY;
	public String username;
	public int id;
	
	public PlayerInfo(int id, String username, int playerX, int playerY) {
		this.id = id;
		this.username = username;
		this.playerX = playerX;
		this.playerY = playerY;
	}
	
	public void updatePosition(int playerX, int playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
	}
}
